package com.pankaj.maukascholars.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.activity.CardOpen;
import com.pankaj.maukascholars.activity.VerticalViewPagerActivity;
import com.pankaj.maukascholars.util.EventDetails;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.pankaj.maukascholars.util.Constants.months;

/**
 * Project Name: 	<Visual Perception For The Visually Impaired>
 * Author List: 		Pankaj Baranwal
 * Filename: 		<>
 * Functions: 		<>
 * Global Variables:	<>
 */
public class VerticalPagerAdapter extends PagerAdapter {
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    public List<EventDetails> cards;
    private EventDetails singleEventDetail;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    public int height;
    int width;
    private TextView title, name, deadline;
    public TextView description;
    private ImageView event_image;
    public Button stalk;

    public VerticalPagerAdapter(Activity context, List<EventDetails> cards) {
        this.cards = cards;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    @Override
    public int getCount() {
        return cards == null ? 0 : cards.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i)
    {
        final View itemView = mLayoutInflater.inflate(R.layout.item_card_inshorts, container, false);
        singleEventDetail = cards.get(i);
//        cardView = itemView.findViewById(R.id.card_view);
        stalk = itemView.findViewById(R.id.stalk);
        stalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CardOpen.class);
                intent.putExtra("event", ((VerticalViewPagerActivity)mContext).getCurrentItemDetails());
                mContext.startActivity(intent);
            }
        });
        title = itemView.findViewById(R.id.event_title);
        description = itemView.findViewById(R.id.event_description);
        name = itemView.findViewById(R.id.name_poster);
        deadline = itemView.findViewById(R.id.date_posted);
        event_image = itemView.findViewById(R.id.event_image);
        title.setText(singleEventDetail.getTitle());
        description.setText(singleEventDetail.getDescription());
        name.setText(singleEventDetail.getName());
        Picasso.with(mContext).load(singleEventDetail.getImage()).fit().error(R.drawable.card).into(event_image);
        String date = singleEventDetail.getDeadline();
        String final_date ="Deadline: " + date.substring(0, 4) + " - " + months[Integer.parseInt(date.substring(5, 7))-1] + " - " + date.substring(8);
        deadline.setText(final_date);
        if (singleEventDetail.getTags().length()>0){
            String tags = singleEventDetail.getTags();
            LinearLayout tag_wrapper = itemView.findViewById(R.id.tag_wrapper);
            while (tags.contains(",")){
                TextView valueTV = new TextView(mContext);
                valueTV.setBackgroundResource(R.drawable.tag_button_bg);
                valueTV.setText(tags.substring(0, tags.indexOf(",")>0?tags.indexOf(","):tags.length()));
                LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMarginEnd(5);
                params.setMarginStart(5);
                valueTV.setLayoutParams(params);
                valueTV.setPadding(15, 5, 15, 5);
                valueTV.setTextColor(0xFFFFFFFF);
                valueTV.setTextSize(12);
                tag_wrapper.addView(valueTV);
                tags = tags.substring(tags.indexOf(",")+1);
            }
            TextView valueTV = new TextView(mContext);
            valueTV.setBackgroundResource(R.drawable.tag_button_bg);
            valueTV.setText(tags.substring(0, tags.indexOf(",")>0?tags.indexOf(","):tags.length()));
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(5);
            params.setMarginStart(5);
            valueTV.setLayoutParams(params);
            valueTV.setTextColor(0xFFFFFFFF);
            valueTV.setPadding(10, 5, 10, 5);
            valueTV.setTextSize(12);
            tag_wrapper.addView(valueTV);
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}