package com.pankaj.maukascholars.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.fragments.SavedFragment.OnListFragmentInteractionListener;
import com.pankaj.maukascholars.util.EventDetails;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link EventDetails} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SavedEventsAdapter extends RecyclerView.Adapter<SavedEventsAdapter.ViewHolder> {

    private final List<EventDetails> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SavedEventsAdapter(List<EventDetails> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
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


