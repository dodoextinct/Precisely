package com.pankaj.maukascholars.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.util.CustomTabHelper;

import java.util.List;

public class StarredEventsAdapter extends RecyclerView.Adapter<StarredEventsAdapter.ViewHolder> {

    private final List<com.pankaj.maukascholars.util.EventDetails> mValues;
    private Context ctx;


    public StarredEventsAdapter(List<com.pankaj.maukascholars.util.EventDetails> items, Context ctx) {
        mValues = items;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.bind(position);
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
        com.pankaj.maukascholars.util.EventDetails mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            title =  view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            deadline = view.findViewById(R.id.deadline);
        }

        private void bind(final int position){
            title.setText(mValues.get(position).getTitle());
            description.setText(mValues.get(position).getDescription());
            deadline.setText(mValues.get(position).getDeadline());
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open(mValues.get(position).getLink(), ctx);
                }
            });
        }
    }

    private void open(String url, Context ctx) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
//        if (url.startsWith("https"))
//            url = url.replace("https://", "http://");
        CustomTabHelper mCustomTabHelper = new CustomTabHelper();
        if (mCustomTabHelper.getPackageName(ctx).size() != 0) {
            CustomTabsIntent customTabsIntent =
                    new CustomTabsIntent.Builder()
                            .build();
            customTabsIntent.intent.setPackage(mCustomTabHelper.getPackageName(ctx).get(0));
            customTabsIntent.launchUrl(ctx, Uri.parse(url));
        } else {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}

