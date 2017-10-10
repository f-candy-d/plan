package com.f_candy_d.plan.presentation.component;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.plan.R;

/**
 * Created by daichi on 10/11/17.
 */

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkViewHolder> {

    @Override
    public WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_work_with_stepper, parent, false);

        return new WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkViewHolder holder, int position) {
        holder.commentList.setAdapter(new CommentAdapter());
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    static class WorkViewHolder extends RecyclerView.ViewHolder {

        RecyclerView commentList;

        WorkViewHolder(View view) {
            super(view);
            commentList = view.findViewById(R.id.comment_list);
            commentList.setHasFixedSize(true);
            commentList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }
    }
}
