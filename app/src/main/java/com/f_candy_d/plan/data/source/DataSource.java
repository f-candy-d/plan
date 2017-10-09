package com.f_candy_d.plan.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.plan.data.model.BaseModel;

import java.util.List;

/**
 * Created by daichi on 10/9/17.
 */

public interface DataSource {

    long INVALID_ID = -1;

    boolean checkIfIdIsValid(@NonNull String modelName, long id);
    @Nullable <T extends BaseModel> T load(@NonNull String modelName, long id);
    @Nullable <T extends BaseModel> List<T> loadAll(@NonNull String modelName);
    <T extends BaseModel> boolean save(@NonNull T model);
    <T extends BaseModel> boolean saveAll(@NonNull List<T> models);
    <T extends BaseModel> boolean delete(@NonNull T model);
    <T extends BaseModel> boolean deleteAll(@NonNull List<T> models);
}
