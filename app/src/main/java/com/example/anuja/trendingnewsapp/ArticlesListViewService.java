package com.example.anuja.trendingnewsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.anuja.trendingnewsapp.app.activities.NewsDetailsActivity;
import com.example.anuja.trendingnewsapp.app.common.Utils;
import com.example.anuja.trendingnewsapp.app.fragments.NewsFragment;
import com.example.anuja.trendingnewsapp.model.Articles;
import com.example.anuja.trendingnewsapp.utilities.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticlesListViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ArticleWidgetListViewFactory(getApplicationContext());
    }
}

class ArticleWidgetListViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    List<Articles> articlesList = new ArrayList<>();
    List<Articles> tempArticlesList = new ArrayList<>();

    public ArticleWidgetListViewFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        articlesList = SharedPreferenceUtils.getNews(context);

        if(tempArticlesList != null) {
            tempArticlesList.clear();
        }

        tempArticlesList.addAll(articlesList);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return tempArticlesList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Articles article = tempArticlesList.get(position);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.news_list_widget_item);
        remoteViews.setTextViewText(R.id.tv_news_widget_item,
                Utils.getStringByArticle(article));

        Bundle extras = new Bundle();
        extras.putParcelable(NewsFragment.KEY_ARTICLE, article);
        Intent intent = new Intent();
        intent.putExtras(extras);
        Log.i("Test", "&&&&&&&&&&&&&&&: " + article);
        remoteViews.setOnClickFillInIntent(R.id.ll_item, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
