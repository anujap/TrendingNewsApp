package com.example.anuja.trendingnewsapp.app.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anuja.trendingnewsapp.R;
import com.example.anuja.trendingnewsapp.app.activities.NewsDetailsActivity;
import com.example.anuja.trendingnewsapp.app.adapters.NewsAdapter;
import com.example.anuja.trendingnewsapp.model.Articles;
import com.example.anuja.trendingnewsapp.viewmodel.MainViewModel;

import static com.example.anuja.trendingnewsapp.app.fragments.NewsFragment.KEY_ARTICLE;

public class FavoritesFragment extends Fragment implements NewsAdapter.ListItemClickListener {

    private MainViewModel mainViewModel;
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the viewmodel
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        retrieveFavoriteNews(); // should be called in the onConnected method
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.rv_fav_news);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mNewsAdapter = new NewsAdapter(null, this);
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    /**
     * function called to retrieve news that are marked as favorite by the user
     */
    private void retrieveFavoriteNews() {
        mainViewModel.retrieveFavNews();

        mainViewModel.getAllFavNews().observe(this, allNews -> {
            mNewsAdapter.swapLists(allNews);
        });
    }

    @Override
    public void onListItemClicked(Articles article) {
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        intent.putExtra(KEY_ARTICLE, article);
        startActivity(intent);
    }
}
