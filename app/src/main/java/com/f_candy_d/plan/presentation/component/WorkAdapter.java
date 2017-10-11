package com.f_candy_d.plan.presentation.component;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.plan.R;

import java.util.Arrays;

/**
 * Created by daichi on 10/11/17.
 */

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkViewHolder> {

    @Override
    public WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_work_card, parent, false);

        return new WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkViewHolder holder, int position) {

        String[] comments = new String[position];
        for (int i = 0; i < position; ++i) {
            comments[i] = "I have " + String.valueOf(i + 1) + " apples...";
        }

        holder.workCard.setComments(Arrays.asList(comments));

        if (position == 3 || position == 5) {
            holder.workCard.setIsActiveUi(true);
        } else {
            holder.workCard.setIsActiveUi(false);
        }

        holder.workCard.setStepNumber(position);
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    static class WorkViewHolder extends RecyclerView.ViewHolder {

        WorkCardView workCard;

        WorkViewHolder(View view) {
            super(view);
            workCard = view.findViewById(R.id.work_card);
        }
    }
}
