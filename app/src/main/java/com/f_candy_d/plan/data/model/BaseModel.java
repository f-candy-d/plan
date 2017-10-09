package com.f_candy_d.plan.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.plan.data.source.DataSource;
import com.f_candy_d.plan.data.source.Repository;

import java.util.List;

/**
 * Created by daichi on 10/9/17.
 */

abstract public class BaseModel {

    public static long DEFAULT_ID = DataSource.INVALID_ID;

    private long mId;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    /**
     * Return a stable name of the model.
     */
    @NonNull
    abstract public String getModelName();

    @Override
    abstract public int hashCode();

    @Override
    abstract public boolean equals(Object obj);
}
