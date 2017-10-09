package com.f_candy_d.plan.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;

import com.f_candy_d.plan.data.model.BaseModel;
import com.f_candy_d.plan.data.source.local.SqliteDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daichi on 10/9/17.
 */

public class Repository implements DataSource {

    private static Repository INSTANCE = null;

    private final DataSource mLocalDataSource;

    private Repository(@NonNull Context context) {
        mLocalDataSource = new SqliteDataSource(context);
    }

    public static Repository getInstance() {
        return checkNotNull(INSTANCE,
                "Call Repository#initializeRepository(Context) before call this method");
    }

    public static void initializeRepository(@NonNull Context context) {
        INSTANCE = new Repository(context);
    }

    public void refresh() {
        clearAllCache();
    }

    /**
     * CACHE
     * ----------------------------------------------------------------------------- */

    @NonNull private Map<String, LongSparseArray<BaseModel>> mCaches = new HashMap<>();

    @Nullable
    @SuppressWarnings("unchecked")
    private <T extends BaseModel> T findInCache(@NonNull String modelName, long id) {
        if (!mCaches.containsKey(modelName)) {
            return null;
        }

        return (T) mCaches.get(modelName).get(id, null);
    }

    private <T extends BaseModel> void cache(@NonNull String modelName, @NonNull T entity) {
        if (!mCaches.containsKey(modelName)) {
            mCaches.put(modelName, new LongSparseArray<>());
        }
        mCaches.get(modelName).put(entity.getId(), entity);
    }

    private <T extends BaseModel> void cacheAll(@NonNull String modelName, @NonNull List<T> entities) {
        if (!mCaches.containsKey(modelName)) {
            mCaches.put(modelName, new LongSparseArray<>());
        }
        for (T entity : entities) {
            mCaches.get(modelName).put(entity.getId(), entity);
        }
    }

    private <T extends BaseModel> void releaseCached(@NonNull String modelName, @NonNull T entity) {
        if (mCaches.containsKey(modelName)) {
            mCaches.get(modelName).remove(entity.getId());
            if (mCaches.get(modelName).size() == 0) {
                mCaches.remove(modelName);
            }
        }
    }

    private <T extends BaseModel> void releaseCachedAll(@NonNull String modelName, @NonNull List<T> entities) {
        if (mCaches.containsKey(modelName)) {
            for (T entity : entities) {
                mCaches.get(modelName).remove(entity.getId());
            }
            if (mCaches.get(modelName).size() == 0) {
                mCaches.remove(modelName);
            }
        }
    }

    private void clearCacheOf(@NonNull String modelName) {
        if (!mCaches.containsKey(modelName)) {
            mCaches.get(modelName).clear();
            mCaches.remove(modelName);
        }
    }

    private void clearAllCache() {
        mCaches.clear();
    }
    
    /**
     * LOAD & SAVE & DELETE DATA
     * ----------------------------------------------------------------------------- */

    @Override
    public boolean checkIfIdIsValid(@NonNull String modelName, long id) {
        return mLocalDataSource.checkIfIdIsValid(modelName, id);
    }

    @Nullable
    @Override
    public <T extends BaseModel> T load(@NonNull String modelName, long id) {
        T model = findInCache(modelName, id);
        if (model != null) {
            return model;
        }

        // Load from the local data source
        model = mLocalDataSource.load(modelName, id);
        if (model != null) {
            cache(modelName, model);
            return model;
        }

        return null;
    }

    @Nullable
    @Override
    public <T extends BaseModel> List<T> loadAll(@NonNull String modelName) {
        List<T> models = mLocalDataSource.loadAll(modelName);
        if (models != null) {
            clearCacheOf(modelName);
            cacheAll(modelName, models);
            return models;
        }

        return null;
    }

    @Override
    public <T extends BaseModel> boolean save(@NonNull T model) {
        boolean wasSuccessful = mLocalDataSource.save(model);
        if (wasSuccessful) {
            cache(model.getModelName(), model);
        }

        return wasSuccessful;
    }

    @Override
    public <T extends BaseModel> boolean saveAll(@NonNull List<T> models) {
        boolean wasSuccessful = mLocalDataSource.saveAll(models);
        if (wasSuccessful && 0 < models.size()) {
            cacheAll(models.get(0).getModelName(), models);
        }

        return wasSuccessful;
    }

    @Override
    public <T extends BaseModel> boolean delete(@NonNull T model) {
        boolean wasSuccessful = mLocalDataSource.delete(model);
        if (wasSuccessful) {
            releaseCached(model.getModelName(), model);
        }

        return wasSuccessful;
    }

    @Override
    public <T extends BaseModel> boolean deleteAll(@NonNull List<T> models) {
        boolean wasSuccessful = mLocalDataSource.deleteAll(models);
        if (wasSuccessful && 0 < models.size()) {
            releaseCachedAll(models.get(0).getModelName(), models);
        }

        return wasSuccessful;
    }
}
