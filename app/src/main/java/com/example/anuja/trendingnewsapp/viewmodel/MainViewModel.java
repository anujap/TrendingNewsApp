package com.example.anuja.trendingnewsapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.anuja.trendingnewsapp.app.activities.NewsDetailsActivity;
import com.example.anuja.trendingnewsapp.model.Articles;
import com.example.anuja.trendingnewsapp.model.NewsModel;
import com.example.anuja.trendingnewsapp.webservice.NewsRetrofitClient;
import com.example.anuja.trendingnewsapp.webservice.NewsUtils;
import com.example.anuja.trendingnewsapp.webservice.NewsWebserviceInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The Main View Model class
 */
public class MainViewModel extends ViewModel {
    private static final String DB_REFERENCE_CHILD_NAME = "TrendingNews";

    private NewsWebserviceInterface mWebserviceInterface = NewsRetrofitClient.getInstance().getNewsWebservice();

    private MutableLiveData<List<Articles>> allNewsList;
    private MutableLiveData<List<Articles>> allFavNews;

    private MutableLiveData<List<Articles>> localNews;

    private DatabaseReference databaseReference;

    private ArrayList<Articles> storedNewsArticlesList = new ArrayList<>();;

    public MutableLiveData<List<Articles>> getAllNewsList() {
        if(allNewsList == null)
            allNewsList = new MutableLiveData<>();
        return allNewsList;
    }

    public MutableLiveData<List<Articles>> getAllFavNews() {
        if(allFavNews == null)
            allFavNews = new MutableLiveData<>();
        return allFavNews;
    }

    public MutableLiveData<List<Articles>> getNewsLocallyList() {
        if(localNews == null)
            localNews = new MutableLiveData<>();
        return localNews;
    }

    public void initializingFirbase() {
        databaseReference = FirebaseDatabase.getInstance().getReference(DB_REFERENCE_CHILD_NAME);
        databaseReference.keepSynced(true);
    }

    /**
     * function used to download all the news (with country = us)
     */
    public void displayAllNews() {
        if(allNewsList == null) {
            mWebserviceInterface.getNews(NewsUtils.ENDPOINT_NEWS_TOP_HEADLINES, NewsUtils.COUNTRY_PARAM_VALUE, NewsUtils.API_KEY).enqueue(new Callback<NewsModel>() {
                @Override
                public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                    if(response.isSuccessful()) {
                        ArrayList<Articles> newsArticlesList = response.body().getArticlesArrayList();
                        if(newsArticlesList != null && newsArticlesList.size() > 0)
                            getAllNewsList().postValue(insertAndRetrieveFromDatabase(newsArticlesList));
                    }
                }

                @Override
                public void onFailure(Call<NewsModel> call, Throwable t) {
                }
            });
        }
    }

    /**
     * function called to insert the data into the database
     */
    private ArrayList<Articles> insertAndRetrieveFromDatabase(List<Articles> newsArticlesList) {
        for (Articles article : newsArticlesList) {
            String id = databaseReference.push().getKey();
            article.setArticleId(id);
            article.setIsFav("false");
            if(!storedNewsArticlesList.contains(article)){
                storedNewsArticlesList.add(article);
                databaseReference.child(id).setValue(article);
            }
        }
        return storedNewsArticlesList;
    }

    // change the list
    public void retrieveLocally() {
        ArrayList<Articles> retrievedList = new ArrayList<>();
        if(databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
                        Articles article = articleSnapshot.getValue(Articles.class);
                        Log.i("Test", "article: " + article);
                        retrievedList.add(article);
                    }

                    Log.i("Test", "article: " + retrievedList.size());
                    getNewsLocallyList().postValue(retrievedList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void retrieveFavNews() {
        ArrayList<Articles> retrievedList = new ArrayList<>();
        if(databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
                        Articles article = articleSnapshot.getValue(Articles.class);
                        if(article.getIsFav().equals("true"))
                            retrievedList.add(article);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            getAllFavNews().postValue(retrievedList);
        }
    }
}
