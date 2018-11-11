package com.example.anuja.trendingnewsapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.example.anuja.trendingnewsapp.model.Articles;
import com.example.anuja.trendingnewsapp.utilities.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a widget service class
 */
public class WidgetService extends IntentService {
    private static final String ACTION_UPDATE_WIDGETS = "com.example.anuja.trendingnewsapp.action.update.widgets";
    private static final String ACTION_UPDATE_NEWS = "com.example.anuja.trendingnewsapp.action.update.news";
    private static final String EXTRA_NEWS = "com.example.anuja.trendingnewsapp.extra.news";

    public WidgetService() {
        super("WidgetService");
    }

    /**
     * function used to start the intent service to update the widgets.
     */
    public static void startActionUpdateNewsWidget(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGETS);
        context.startService(intent);
    }

    /**
     * function used to start intent service to store in
     * shared preferences and update the widgets.
     */
    public static void startActionToUpdateNews(Context context, List<Articles> newsArticle) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_NEWS);
        ArrayList<Articles> arrayListArticles  = new ArrayList<>(newsArticle.size());
        arrayListArticles.addAll(newsArticle);
        intent.putParcelableArrayListExtra(EXTRA_NEWS, arrayListArticles);
        context.startService(intent);
    }

    // Method to handle the news object and start widget update service.
    private void handleActionUpdateNews(ArrayList<Articles> newsArticle) {
        SharedPreferenceUtils.storeNews(this, newsArticle);
        startActionUpdateNewsWidget(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(ACTION_UPDATE_WIDGETS)) {
                handleActionUpdateWidgets();
            } else if (action.equals(ACTION_UPDATE_NEWS)) {
                ArrayList<Articles> newsArticle = intent.getParcelableArrayListExtra(EXTRA_NEWS);
                handleActionUpdateNews(newsArticle);
            }
        }
    }

    // Method to handle the update of widgets.
    private void handleActionUpdateWidgets() {
        // Getting the news from the shared preference.
        ArrayList<Articles> newsArticle = SharedPreferenceUtils.getNews(this);

        if (newsArticle != null) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TrendingNewsWidget.class));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_news_widget);
            TrendingNewsWidget.updateBakingAppWidgets(this, appWidgetManager, newsArticle, appWidgetIds);
        }
    }
}