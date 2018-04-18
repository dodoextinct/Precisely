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
import com.pankaj.maukascholars.util.Language;

import java.util.List;

public class languageAdapter extends RecyclerView.Adapter<languageAdapter.MyViewHolder> {

    private List<Language> LanguagesList;
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

    public languageAdapter(List<Language> LanguagesList) {
        this.LanguagesList = LanguagesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Language language = LanguagesList.get(position);
        holder.bindTo(language.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                position = holder.getAdapterPosition();

                if (holder.imageView.getVisibility() == View.GONE)
                {
//                        Log.e("GEE", "INVISIBKLE");
                    if (checkedIV!= null) {

                        checkedIV.setVisibility(View.GONE);
                    }
                    holder.imageView.setVisibility(View.VISIBLE);
                    checkedIV = holder.imageView;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return LanguagesList.size();
    }
}