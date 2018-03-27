package com.pankaj.maukascholars.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pankaj.maukascholars.R;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.squareup.picasso.Picasso;

import static com.pankaj.maukascholars.util.Constants.clickedFilters;

/**
 * Created by pankaj on 11/11/17.
 */

public class FiltersViewHolder extends RecyclerView.ViewHolder {

    private ImageView filter_image;
    private TextView filter_text;
    private LinearLayout ll_wrapper;


    public FiltersViewHolder(View itemView) {
        super(itemView);

        filter_image = itemView.findViewById(R.id.filter_image);
        filter_text = itemView.findViewById(R.id.filter_text);
        ll_wrapper = itemView.findViewById(R.id.ll_filter_wrapper);
        ll_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickedFilters.contains(getAdapterPosition())){
                    clickedFilters.remove(Integer.valueOf(getAdapterPosition()));
                    filter_text.setBackgroundColor(0xFFFFFFFF);
                    filter_text.setTextColor(0xFF000000);
                }else{
                    clickedFilters.add(getAdapterPosition());
                    filter_text.setBackgroundColor(0xFF000000);
                    filter_text.setTextColor(0xFFFFFFFF);
                }
            }
        });

        ll_wrapper.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    public void bindTo(String filter, String url, int position, Context ctx) {
        Picasso.with(ctx).load(url).fit().error(R.mipmap.j_bezos).into(filter_image);
        if (clickedFilters.contains(position)){
            filter_text.setBackgroundColor(0xFF000000);
            filter_text.setTextColor(0xFFFFFFFF);
        }

        filter_text.setText(filter);
        ViewGroup.LayoutParams lp = filter_text.getLayoutParams();
        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
            FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
            flexboxLp.setFlexGrow(1.0f);
        }
    }
}
