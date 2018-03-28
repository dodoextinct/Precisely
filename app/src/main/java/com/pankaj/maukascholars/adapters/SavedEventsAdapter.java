package com.pankaj.maukascholars.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.util.EventDetails;

import java.util.List;

public class SavedEventsAdapter extends RecyclerView.Adapter<SavedEventsAdapter.ViewHolder> {

    private final List<EventDetails> mValues;

    public SavedEventsAdapter(List<EventDetails> items) {
        mValues = items;
    }

    @Override
    public SavedEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_event, parent, false);
        return new SavedEventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SavedEventsAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getTitle());
        holder.description.setText(mValues.get(position).getDescription());
        holder.deadline.setText(mValues.get(position).getDeadline());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView title;
        final TextView description;
        final TextView deadline;
        EventDetails mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            title =  view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            deadline = view.findViewById(R.id.deadline);
        }
    }
}


