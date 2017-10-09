package com.f_candy_d.plan.data.source.local;

import android.support.annotation.NonNull;

import com.f_candy_d.plan.data.model.BaseModel;
import com.f_candy_d.plan.data.model.Plan;
import com.f_candy_d.plan.data.source.local.table.PlanTable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daichi on 10/10/17.
 */

final class ModelCreatorFactory {

    private ModelCreatorFactory() {}

    @NonNull
    static <T extends BaseModel> SqliteDataSource.ModelCreator<T>
    createModelCreator(@NonNull String modelName) {
        modelName = checkNotNull(modelName);

        if (modelName.equals(Plan.MODEL_NAME)) {
            return PLAN_MODEL_CREATOR.getClass().cast(PLAN_MODEL_CREATOR);
        } else {
            throw new IllegalArgumentException("Cannot recognize this model name -> " + modelName);
        }
    }

    private static final SqliteDataSource.ModelCreator<Plan> PLAN_MODEL_CREATOR =
            contentValues -> {
                Plan plan = Plan.createAsDefault();
                plan.setId(contentValues.getAsLong(PlanTable._ID));
                plan.setTitle(contentValues.getAsString(PlanTable._TITLE));

                return plan;
            };
}
