package com.f_candy_d.plan.presentation.component;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.plan.R;
import com.f_candy_d.plan.data.model.Plan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/11/17.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    List<Plan> mPlans;

    public PlanAdapter() {
        mPlans = new ArrayList<>();
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan_card, parent, false);

        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {

        PlanViewHolder(View view) {
            super(view);
        }
    }
}
