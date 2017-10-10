package com.f_candy_d.plan.presentation.contract;

import com.f_candy_d.plan.data.model.Plan;

import java.util.List;

/**
 * Created by daichi on 10/11/17.
 */

public interface HomeContract {

    interface View extends BaseView {
        void appendPlans(List<Plan> plans);
    }

    interface Presenter extends BasePresenter<HomeContract.View> {

    }
}
