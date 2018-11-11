package com.example.anuja.trendingnewsapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.anuja.trendingnewsapp.model.Articles;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceUtils {

    private static final String APP_SHARED_PREF_FILE_NAME = "news_app_shared_pref";
    private static final String NEWS_CHANGED = "shared_pref_recently_changed_news";

    private SharedPreferenceUtils() {
    }

    /**
     * function used to store the news object in the shared preference.
      */
    public static void storeNews(Context context, ArrayList<Articles> newsArticle) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String newsString = new Gson().toJson(newsArticle);
        editor.putString(NEWS_CHANGED, newsString);
        editor.apply();
    }

    /**
     * function used to get the news object from the shared preference.
     */
    public static ArrayList<Articles> getNews(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SHARED_PREF_FILE_NAME,
                Context.MODE_PRIVATE);

        String newsString = sharedPreferences.getString(NEWS_CHANGED, null);
        Type type = new TypeToken<List<Articles>>(){}.getType();
        return new Gson().fromJson(newsString, type);
    }
}