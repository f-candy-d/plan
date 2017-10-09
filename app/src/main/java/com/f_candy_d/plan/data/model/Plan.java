package com.f_candy_d.plan.data.model;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 10/9/17.
 */

public class Plan extends BaseModel {

    public static final String MODEL_NAME = Plan.class.getSimpleName();

    public static final String DEFAULT_TITLE = null;

    private String mTitle;

    private Plan() {}

    @NonNull
    @Override
    public String getModelName() {
        return MODEL_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plan plan = (Plan) o;

        return mTitle != null ? mTitle.equals(plan.mTitle) : plan.mTitle == null;

    }

    @Override
    public int hashCode() {
        return mTitle != null ? mTitle.hashCode() : 0;
    }

    /**
     * FACTORY METHODS
     * ----------------------------------------------------------------------------- */

    public static Plan createAsDefault() {
        Plan plan = new Plan();
        plan.setId(DEFAULT_ID);
        plan.setTitle(DEFAULT_TITLE);

        return plan;
    }

    /**
     * GETTERS & SETTERS
     * ----------------------------------------------------------------------------- */

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
