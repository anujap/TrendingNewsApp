package com.example.anuja.trendingnewsapp.app.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.anuja.trendingnewsapp.R;
import com.example.anuja.trendingnewsapp.WidgetService;
import com.example.anuja.trendingnewsapp.app.activities.NewsDetailsActivity;
import com.example.anuja.trendingnewsapp.app.adapters.NewsAdapter;
import com.example.anuja.trendingnewsapp.databinding.FragmentNewsBinding;
import com.example.anuja.trendingnewsapp.model.Articles;
import com.example.anuja.trendingnewsapp.viewmodel.MainViewModel;

public class NewsFragment extends BaseFragment implements NewsAdapter.ListItemClickListener {
    public static final String KEY_ARTICLE = "article_key";

    private MainViewModel mainViewModel;
    private NewsAdapter mNewsAdapter;

    private FragmentNewsBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // get the viewmodel
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mainViewModel.initializingFirbase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.rvAllNews.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mNewsAdapter = new NewsAdapter(null, this);
        mBinding.rvAllNews.setAdapter(mNewsAdapter);
        mBinding.pbList.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onConnected() {
        retrieveAllNews();
    }

    @Override
    protected void onDisconnected() {
        mainViewModel.retrieveLocally();
        mainViewModel.getNewsLocallyList().observe(this, allNews -> {
            Log.i("Test", "allNews: locally: " + allNews.size());
            mNewsAdapter.swapLists(allNews);
            WidgetService.startActionToUpdateNews(getContext(), allNews);
        });
    }

    /**
     * function called to get all the news
     */
    private void retrieveAllNews() {
        mainViewModel.displayAllNews();
        mainViewModel.getAllNewsList().observe(this, allNews -> {
            mNewsAdapter.swapLists(allNews);
            WidgetService.startActionToUpdateNews(getContext(), allNews);
        });
        mBinding.pbList.setVisibility(View.GONE);
    }

    @Override
    public void onListItemClicked(Articles article) {
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        intent.putExtra(KEY_ARTICLE, article);
        startActivity(intent);
    }
}
