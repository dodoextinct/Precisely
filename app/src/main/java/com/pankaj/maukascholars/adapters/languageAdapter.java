package com.pankaj.maukascholars.adapters;

/**
 * Created by hitesh on 19/04/18.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.util.Constants;
import com.pankaj.maukascholars.util.LanguageDetails;

import java.util.List;

public class languageAdapter extends RecyclerView.Adapter<languageAdapter.MyViewHolder> {

    private List<LanguageDetails> languagesList;
    private ImageView checkedIV = null;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        public int position = 0;

        public MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.correct_symbol);

        }
        public void bindTo(String text){
            title.setText(text);

        }

    }

    public languageAdapter(List<LanguageDetails> languagesList) {
        this.languagesList = languagesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LanguageDetails languageDetails = languagesList.get(position);
        holder.bindTo(languageDetails.getLanguage());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (holder.imageView.getVisibility() == View.GONE)
            {
                if (checkedIV!= null)
                    checkedIV.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                Constants.language_id = languageDetails.getLanguage_id();
                checkedIV = holder.imageView;
            }
            }
        });
    }

    @Override
    public int getItemCount() {
        return languagesList.size();
    }
}