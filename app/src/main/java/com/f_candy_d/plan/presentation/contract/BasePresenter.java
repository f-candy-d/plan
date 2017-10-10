package com.f_candy_d.plan.presentation.contract;

/**
 * Created by daichi on 10/11/17.
 */

interface BasePresenter<T extends BaseView> {
    void onStart(T view);
}