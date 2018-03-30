package com.pankaj.maukascholars.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.application.VolleyHandling;
import com.pankaj.maukascholars.database.DBHandler;
import com.pankaj.maukascholars.util.Constants;
import com.pankaj.maukascholars.util.EventDetails;
import com.rey.material.widget.ProgressView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pankaj.maukascholars.util.Constants.imageResources;
import static com.pankaj.maukascholars.util.Constants.normal;
import static com.pankaj.maukascholars.util.Constants.normal_color;
import static com.pankaj.maukascholars.util.Constants.sub;


public class Cards extends AppCompatActivity {
    private CardViewAdapter mAdapter;
    private List<EventDetails> mItems = new ArrayList<>();
    RelativeLayout loading;
    ProgressView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        loading = findViewById(R.id.progress_rl);
        progress = findViewById(R.id.progress);
        progress.start();
        loading.setVisibility(View.VISIBLE);
        getData();
    }

    private void init() {
        OnItemTouchListener itemTouchListener = new OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                String url = mItems.get(position).getLink();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        };

        mAdapter = new CardViewAdapter(this, mItems, itemTouchListener);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        loading.setVisibility(View.GONE);
        progress.stop();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        TODO: Add items for card adapter
        recyclerView.setAdapter(mAdapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    deleteData(position);
                                    mItems.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(Cards.this, "Deleted!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mItems.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(Cards.this, "Deleted!", Toast.LENGTH_SHORT).show();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    private String removeEscapeChars(String input){
        Matcher matcher = Pattern.compile("\\&([^;]{6})", Pattern.CASE_INSENSITIVE).matcher(input);
        while (matcher.find()){
            String before = input.substring(0, matcher.start());
            String after = input.substring(matcher.start() + 1);
            input = before + after;
        }
        return input;
    }

    void getData(){
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_event_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0){
                    try {
                        if (response.contains("[[\""))
                            response = response.substring(response.indexOf("[[\""));
//                        response = removeEscapeChars(response);
                        JSONArray jA = new JSONArray(response);
                        for (int i = 0; i < jA.length(); i++){
                            mItems.add(new EventDetails(Integer.parseInt(jA.getJSONArray(i).get(0).toString()), jA.getJSONArray(i).get(1).toString(), jA.getJSONArray(i).get(2).toString(), jA.getJSONArray(i).get(3).toString(), jA.getJSONArray(i).get(4).toString(), jA.getJSONArray(i).get(5).toString(), jA.getJSONArray(i).get(6).toString(), jA.getJSONArray(i).get(7).toString()));
                        }
                        init();
                    } catch (JSONException e) {
                        loading.setVisibility(View.GONE);
                        progress.stop();
                        Toast.makeText(Cards.this, "ERRORORROROR", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else{
                    loading.setVisibility(View.GONE);
                    progress.stop();
                    Toast.makeText(Cards.this, "Server is no longer speaking to you", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cards.this, "Couldn't connect to server", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                getData();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                JSONArray jO = new JSONArray();
                for (int i = 0; i < Constants.clickedFilters.size(); i++){
                    jO.put(Constants.filters.get(Constants.clickedFilters.get(i)));
                }
                params.put("id", Constants.user_id);
                params.put("page", "0");
                params.put("tags", jO.toString());
                return params;
            }
        };

        VolleyHandling.getInstance().addToRequestQueue(request, "signin");
    }

    void deleteData(final int position){
        StringRequest request = new StringRequest(Request.Method.POST, Constants.url_event_deletion, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Cards.this, "Deleted!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cards.this, "Couldn't connect to server", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("event_id", String.valueOf(mItems.get(position).getId()));
                params.put("user_id", Constants.user_id);
                return params;
            }
        };
        VolleyHandling.getInstance().addToRequestQueue(request, "signin");
    }

    /**
     * Interface for the touch events in each item
     */
    public interface OnItemTouchListener {
        /**
         * Callback invoked when the user Taps one of the RecyclerView items
         *
         * @param view     the CardView touched
         * @param position the index of the item touched in the RecyclerView
         */
        void onCardViewTap(View view, int position);
    }

    /**
     * A simple adapter that loads a CardView layout with one TextView and two Buttons, and
     * listens to clicks on the Buttons or on the CardView
     */
    public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
        private List<EventDetails> cards;
        private OnItemTouchListener onItemTouchListener;
        private Context mContext;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int height;
        int width;

        CardViewAdapter(Context context, List<EventDetails> cards, OnItemTouchListener onItemTouchListener) {
            this.cards = cards;
            this.onItemTouchListener = onItemTouchListener;
            mContext = context;
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
        }

        HamButton.Builder getHamButtonBuilder(int h, final int position) {
            return new HamButton.Builder()
                    .normalImageRes(imageResources[h])
                    .normalText(normal[h])
                    .subNormalText(sub[h])
                    .normalColor(normal_color[h])
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if (index == 0) {
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Found a great opportunity for you!\n" + cards.get(position).getLink());
                                sendIntent.setType("text/plain");
                                startActivity(sendIntent);
                            }else if (index == 1){
                                starEvent(position);
                            }else if (index == 2){
                                saveEvent(position);
                            }else if (index == 3){
                                composeEmail(position);
                            }
                        }
                    });
        }

        private void starEvent(int position) {
            DBHandler db = new DBHandler(mContext);
            if (db.getEvent(cards.get(position).getId()) != null && db.getEvent(cards.get(position).getId()).getStarred() == 1){
                Toast.makeText(mContext, "Already starred!", Toast.LENGTH_SHORT).show();
            }else{
                if (db.getEvent(cards.get(position).getId()) != null)
                    cards.get(position).setSaved(1);
                cards.get(position).setStarred(1);
                db.addEvent(cards.get(position));
                Toast.makeText(mContext, "Event Starred!", Toast.LENGTH_SHORT).show();
            }
        }

        private void saveEvent(int position) {
            DBHandler db = new DBHandler(mContext);
            if (db.getEvent(cards.get(position).getId()) != null && db.getEvent(cards.get(position).getId()).getSaved() == 1){
                Toast.makeText(mContext, "Already saved!", Toast.LENGTH_SHORT).show();
            }else {
                if (db.getEvent(cards.get(position).getId()) != null)
                    cards.get(position).setStarred(1);
                cards.get(position).setSaved(1);
                db.addEvent(cards.get(position));
                Toast.makeText(mContext, "Event Saved!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int position = i;
            viewHolder.title.setText(cards.get(position).getTitle());
            viewHolder.description.setText(cards.get(position).getDescription());
            viewHolder.name.setText(cards.get(position).getName());
            viewHolder.deadline.setText(cards.get(position).getDeadline());
            Picasso.with(mContext).load(cards.get(position).getImage()).error(R.mipmap.j_bezos).fit().into(viewHolder.event_image);
            viewHolder.bmb.clearBuilders();
            for (int h = 0; h < viewHolder.bmb.getPiecePlaceEnum().pieceNumber(); h++) {
                viewHolder.bmb.addBuilder(getHamButtonBuilder(h, position));
            }
        }

        @Override
        public int getItemCount() {
            return cards == null ? 0 : cards.size();
        }

        private void composeEmail(int position) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, Constants.user_name);
            intent.putExtra(Intent.EXTRA_SUBJECT, cards.get(position).getTitle());
            intent.putExtra(Intent.EXTRA_TEXT,  "Spot On Opportunities presents to you:\n\n" + cards.get(position).getLink() + "\n\n" + cards.get(position).getDescription());
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }else
                Toast.makeText(Cards.this, "No Email app found!", Toast.LENGTH_SHORT).show();

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title, description, name, deadline;
            private ImageView event_image;
            private BoomMenuButton bmb;

            ViewHolder(final View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.event_title);
                description = itemView.findViewById(R.id.event_description);
                name = itemView.findViewById(R.id.name_poster);
                deadline = itemView.findViewById(R.id.date_posted);
                bmb = itemView.findViewById(R.id.bmb);
                event_image = itemView.findViewById(R.id.event_image);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemTouchListener.onCardViewTap(v, getLayoutPosition());
                    }
                });
            }
        }
    }
}
