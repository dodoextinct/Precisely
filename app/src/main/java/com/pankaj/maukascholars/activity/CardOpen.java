package com.pankaj.maukascholars.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.util.CustomTabHelper;
import com.pankaj.maukascholars.util.EventDetails;
import com.squareup.picasso.Picasso;

import static com.pankaj.maukascholars.util.Constants.months;

public class CardOpen extends AppCompatActivity {
    private TextView post_title, post_description, eligibility, requirements, benefits, deadline, link;
    ImageView post_image;
    EventDetails singleEventDetails;
    String link_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardopen);
        if (getIntent()!= null && getIntent().hasExtra("event"))
            singleEventDetails = (EventDetails)getIntent().getSerializableExtra("event");
        else
            finish();
        post_title = findViewById(R.id.post_title);
        post_description = findViewById(R.id.description);
        eligibility = findViewById(R.id.eligibility_content);
        requirements = findViewById(R.id.requirements_content);
        benefits = findViewById(R.id.benefit_content);
        deadline = findViewById(R.id.post_deadline);
        link = findViewById(R.id.post_link);
        post_image = findViewById(R.id.post_image);
        init();
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open(link_url);
            }
        });
    }

    private void open(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        CustomTabHelper mCustomTabHelper = new CustomTabHelper();
        if (mCustomTabHelper.getPackageName(this).size() != 0) {
            CustomTabsIntent customTabsIntent =
                    new CustomTabsIntent.Builder()
                            .build();
            customTabsIntent.intent.setPackage(mCustomTabHelper.getPackageName(this).get(0));
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    private void init() {
        post_title.setText(singleEventDetails.getTitle());
        post_description.setText(singleEventDetails.getDescription());
        setDisplay(findViewById(R.id.eligibility_wrapper), eligibility, singleEventDetails.getEligibility());
        setDisplay(findViewById(R.id.requirements_wrapper), requirements, singleEventDetails.getRequirements());
        setDisplay(findViewById(R.id.benefit_wrapper), benefits, singleEventDetails.getBenifits());
        Picasso.with(this).load(singleEventDetails.getImage()).fit().error(R.drawable.card).into(post_image);
        String date = singleEventDetails.getDeadline();
        String final_date = date.substring(0, 5) + months[Integer.parseInt(date.substring(5, 7))-1] + date.substring(7);
        deadline.setText(final_date);
        link_url = singleEventDetails.getLink();
    }

    private void setDisplay(View parent, View view, String text){
        if (text.length()==0){
            parent.setVisibility(View.GONE);
        }else{
            ((TextView)view).setText(text);
        }
    }
}
