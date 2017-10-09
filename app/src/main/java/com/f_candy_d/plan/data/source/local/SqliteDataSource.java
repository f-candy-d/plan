package com.f_candy_d.plan.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.plan.data.model.BaseModel;
import com.f_candy_d.plan.data.source.DataSource;
import com.f_candy_d.sqliteutils.BaseTable;
import com.f_candy_d.sqliteutils.QueryBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/9/17.
 */

public class SqliteDataSource implements DataSource {

    interface ValueMapper<T extends BaseModel> {
        ContentValues map(T model, boolean containId);
    }

    interface ModelCreator<T extends BaseModel> {
        T create(ContentValues contentValues);
    }

    @NonNull private final SQLiteOpenHelper mOpenHelper;

    public SqliteDataSource(@NonNull Context context) {
        mOpenHelper = new SqliteOpenHelper(checkNotNull(context));
    }

    @Override
    public boolean checkIfIdIsValid(@NonNull String modelName, long id) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        boolean isValid = checkIfDataExist(db, modelName, id);
        db.close();
        return isValid;
    }

    @Nullable
    @Override
    public <T extends BaseModel> T load(@NonNull String modelName, long id) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        QueryBuilder queryBuilder = new QueryBuilder()
                .table(modelName)
                .whereColumnEquals(BaseTable._ID, id);

        Cursor cursor = queryBuilder.query(db);
        ContentValues valueMap = new ContentValues();
        ArrayList<T> models = new ArrayList<>(cursor.getCount());
        ModelCreator<T> creator = ModelCreatorFactory.createModelCreator(modelName);
        boolean hasNext = cursor.moveToFirst();

        while (hasNext) {
            valueMap.clear();
            DatabaseUtils.cursorRowToContentValues(cursor, valueMap);
            models.add(creator.create(valueMap));
            hasNext = cursor.moveToNext();
        }

        db.close();
        return (models.size() == 1) ? models.get(0) : null;
    }

    @Nullable
    @Override
    public <T extends BaseModel> List<T> loadAll(@NonNull String modelName) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        QueryBuilder queryBuilder = new QueryBuilder().table(modelName);

        Cursor cursor = queryBuilder.query(db);
        ContentValues valueMap = new ContentValues();
        ArrayList<T> models = new ArrayList<>(cursor.getCount());
        ModelCreator<T> creator = ModelCreatorFactory.createModelCreator(modelName);
        boolean hasNext = cursor.moveToFirst();

        while (hasNext) {
            valueMap.clear();
            DatabaseUtils.cursorRowToContentValues(cursor, valueMap);
            models.add(creator.create(valueMap));
            hasNext = cursor.moveToNext();
        }

        db.close();
        return (models.size() != 0) ? models : null;
    }

    @Override
    public <T extends BaseModel> boolean save(@NonNull T model) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        ValueMapper<T> mapper = ValueMapperFactory.createValueMapper(model.getModelName());

        if (checkIfDataExist(db, model.getModelName(), model.getId())) {
            // Update
            boolean isError = true;
            db.beginTransaction();
            try {
                String where = new QueryBuilder()
                        .whereColumnEquals(BaseTable._ID, model.getId())
                        .getWhereClause();

                int affected = db.update(model.getModelName(), mapper.map(model, true), where, null);
                if (affected == 1) {
                    db.setTransactionSuccessful();
                    isError = false;
                }

            } finally {
                db.endTransaction();
                db.close();
            }

            return !isError;

        } else {
            // Insert
            long id = db.insert(model.getModelName(), null, mapper.map(model, false));
            db.close();
            // SQLiteDatabase#insert() method returns the row ID of the newly inserted row, or -1 if an error occurred.
            // See document -> https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#insert(java.lang.String, java.lang.String, android.content.ContentValues)
            if (id != -1) {
                model.setId(id);
                return true;

            } else {
                return false;
            }
        }
    }

    @Override
    public <T extends BaseModel> boolean saveAll(@NonNull List<T> models) {
        if (checkNotNull(models).size() == 0) {
            return true;
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        ValueMapper<T> mapper = ValueMapperFactory.createValueMapper(models.get(0).getModelName());
        String modelName = models.get(0).getModelName();
        ArrayList<Long> ides = new ArrayList<>(models.size());
        boolean isError = false;

        db.beginTransaction();
        try {
            for (T model : models) {
                if (checkIfDataExist(db, modelName, model.getId())) {
                    // Update
                    String where = new QueryBuilder()
                            .whereColumnEquals(BaseTable._ID, model.getId())
                            .getWhereClause();

                    int affected = db.update(modelName, mapper.map(model, true), where, null);
                    ides.add(model.getId());
                    if (affected != 1) {
                        isError = true;
                        break;
                    }

                } else {
                    // Insert
                    long id = db.insert(modelName, null, mapper.map(model, false));
                    ides.add(id);
                    if (id == -1) {
                        isError = true;
                        break;
                    }
                }
            }

            if (!isError) {
                db.setTransactionSuccessful();
                for (int i = 0; i < models.size(); ++i) {
                    // Set ides
                    models.get(i).setId(ides.get(0));
                }
            }

        } finally {
            db.endTransaction();
            db.close();
        }

        return !isError;
    }

    @Override
    public <T extends BaseModel> boolean delete(@NonNull T model) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String where = new QueryBuilder()
                .whereColumnEquals(BaseTable._ID, model.getId())
                .getWhereClause();

        boolean isError = true;
        db.beginTransaction();
        try {
            // Return true if the number of affected rows is 1, otherwise rollback and return false.
            final int affected = db.delete(model.getModelName(), where, null);
            if (affected < 2) {
                db.setTransactionSuccessful();
                isError = false;
            }
        } finally {
            db.endTransaction();
            db.close();
        }

        return !isError;
    }

    @Override
    public <T extends BaseModel> boolean deleteAll(@NonNull List<T> models) {
        if (checkNotNull(models).size() == 0) {
            return true;
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String modelName = models.get(0).getModelName();
        boolean isError = false;

        db.beginTransaction();
        try {
            for (T model : models) {
                String where = new QueryBuilder()
                        .whereColumnEquals(BaseTable._ID, model.getId())
                        .getWhereClause();

                int affected = db.delete(modelName, where, null);
                if (1 < affected) {
                    isError = true;
                    break;
                }
            }

            if (!isError) {
                db.setTransactionSuccessful();
            }

        } finally {
            db.endTransaction();
            db.close();
        }

        return !isError;
    }

    /**
     * This method does not close the database object.
     */
    private boolean checkIfDataExist(@NonNull SQLiteDatabase db, @NonNull String modelName, long id) {
        QueryBuilder queryBuilder = new QueryBuilder()
                .table(modelName)
                .whereColumnEquals(BaseTable._ID, id);

        Cursor cursor = queryBuilder.query(db);
        return (cursor.getCount() == 1);
    }
}
