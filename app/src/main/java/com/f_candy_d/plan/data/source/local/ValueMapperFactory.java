package com.f_candy_d.plan.data.source.local;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.f_candy_d.plan.data.model.BaseModel;
import com.f_candy_d.plan.data.model.Plan;
import com.f_candy_d.plan.data.source.local.table.PlanTable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daichi on 10/10/17.
 */

final class ValueMapperFactory {

    private ValueMapperFactory() {}

    @NonNull
    static <T extends BaseModel> SqliteDataSource.ValueMapper<T>
    createValueMapper(@NonNull String modelName) {
        modelName = checkNotNull(modelName);

        if (modelName.equals(Plan.MODEL_NAME)) {
            return PLAN_VALUE_MAPPER.getClass().cast(PLAN_VALUE_MAPPER);
        } else {
            throw new IllegalArgumentException("Cannot recognize this model name -> " + modelName);
        }
    }

    private static final SqliteDataSource.ValueMapper<Plan> PLAN_VALUE_MAPPER =
            (model, containId) -> {
                ContentValues contentValues = new ContentValues();
                if (containId) {
                    contentValues.put(PlanTable._ID, model.getId());
                }

                contentValues.put(PlanTable._TITLE, model.getTitle());

                return contentValues;
            };
}
