package com.f_candy_d.plan.presentation.component;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.plan.R;

/**
 * Created by daichi on 10/11/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        CommentViewHolder(View view) {
            super(view);
        }
    }
}
