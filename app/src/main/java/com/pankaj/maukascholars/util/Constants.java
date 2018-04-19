package com.pankaj.maukascholars.util;

import com.pankaj.maukascholars.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankaj on 17/11/17.
 */

public class Constants {

    public static String user_name = null;
    public static String user_id = null;
    public static String quote = "";
    public static int count_nav_order = 1;
    public static String toolbar_title = "Filter Your Opportunities";
    public static String language_id = "1";

    public static String url_signup = "https://www.lithics.in/apis/mauka/signup.php";
    public static String url_event_details = "https://www.lithics.in/apis/mauka/getOpportunities.php";
    public static String url_event_deletion = "https://www.lithics.in/apis/mauka/delete_event.php";
    public static String url_verify_user = "https://www.lithics.in/apis/mauka/verify.php";
    public static String url_get_filters = "https://www.lithics.in/apis/mauka/get_filters.php";
    public static String url_get_quote = "https://www.lithics.in/apis/mauka/get_quote.php";
    public static String url_verify_coupon= "https://www.lithics.in/apis/mauka/verify_coupon.php";
    public static String url_get_languages= "https://www.lithics.in/apis/mauka/get_languages.php";
    public static String url_my_language= "https://www.lithics.in/apis/mauka/set_my_language.php";

    public static int[] imageResources = new int[]{
            R.mipmap.share,
            R.mipmap.star,
            R.mipmap.save,
            R.mipmap.reply,
    };
    public static String[] normal = new String[]{
            "SHARE",
            "STAR",
            "SAVE",
            "SEND"
    };
    public static String[] sub = new String[]{
            "I should share it with others",
            "Seems interesting! I'll decide later though",
            "Notify me of important updates",
            "Send it to me via mail"
    };

    public static int[] normal_color = new int[]{
            0xff009688,
            0xff03a9f4,
            0xff795548,
            0xffe91e63
    };


    public static List<String> filters = new ArrayList<>();
    public static List<String> filters_image_urls = new ArrayList<>();

    public static List<Integer> clickedFilters = new ArrayList<>();

    public static String key = "clickFilters";

    public static String[] months = new String[]{
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };
}
