package com.f_candy_d.plan.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.f_candy_d.plan.data.model.Plan;
import com.f_candy_d.plan.data.source.local.table.PlanTable;
import com.f_candy_d.sqliteutils.TableUtils;

/**
 * Created by daichi on 10/9/17.
 */

class SqliteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "plan_contents_db";
    private static final int DATABASE_VERSION = 1;

    public SqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        TableUtils.createTable(sqLiteDatabase, PlanTable.getTableSource());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        TableUtils.resetTable(sqLiteDatabase, PlanTable.getTableSource());
    }
}
