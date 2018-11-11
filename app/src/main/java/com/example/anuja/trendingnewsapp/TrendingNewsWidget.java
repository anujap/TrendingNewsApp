package com.example.anuja.trendingnewsapp;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.anuja.trendingnewsapp.app.activities.MainActivity;
import com.example.anuja.trendingnewsapp.app.activities.NewsDetailsActivity;
import com.example.anuja.trendingnewsapp.app.fragments.NewsFragment;
import com.example.anuja.trendingnewsapp.model.Articles;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class TrendingNewsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, ArrayList<Articles> newsArticle,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.trending_news_widget);
        Intent intent = null;
        if (newsArticle != null) {
            Intent listService = new Intent(context, ArticlesListViewService.class);
            views.setRemoteAdapter(R.id.lv_news_widget, listService);

            intent = new Intent(context, NewsDetailsActivity.class);
            //intent.putExtra(MainActivity.INTENT_RECIPE, recipe);
        }

        PendingIntent pendingIntent = TaskStackBuilder.create(context).addNextIntentWithParentStack(intent)
                                                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.lv_news_widget, pendingIntent);

        /*
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.trending_news_app_widget_layout, pendingIntent);
        */

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetService.startActionUpdateNewsWidget(context);
    }

    public static void updateBakingAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                              ArrayList<Articles> newsArticle, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, newsArticle, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

