package com.example.anuja.trendingnewsapp.app.activities;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.anuja.trendingnewsapp.BR;
import com.example.anuja.trendingnewsapp.R;
import com.example.anuja.trendingnewsapp.app.fragments.NewsFragment;
import com.example.anuja.trendingnewsapp.databinding.ActivityNewsDetailsBinding;
import com.example.anuja.trendingnewsapp.model.Articles;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Reference: - https://www.learn2crack.com/2015/10/android-floating-action-button-animations.html
 */
public class NewsDetailsActivity extends AppCompatActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private ActivityNewsDetailsBinding mBinding;
    private Articles mArticle;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private static final String DB_REFERENCE_CHILD_NAME = "TrendingNews";

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    private ActionBar actionBar;
    // snackbar
    private Snackbar snackbar;

    private boolean isFavorite = false;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_details);

        dbReference = FirebaseDatabase.getInstance().getReference(DB_REFERENCE_CHILD_NAME);
        dbReference.keepSynced(true);

        setUpActionBar();
        retrieveIntent();
        initializeFAB();
    }

    /**
     * Function called to handle the action bar
     */
    private void setUpActionBar() {
        setSupportActionBar(mBinding.toolbarDetails);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        startAlphaAnimation(mBinding.tvToolbarTitle, 0, View.INVISIBLE);
        mBinding.appbar.addOnOffsetChangedListener(this);

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mBinding.tvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
                if(actionBar != null) {
                    mBinding.tvToolbarTitle.setText(getResources().getString(R.string.str_full_coverage));
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mBinding.tvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mBinding.linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mBinding.linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    /**
     * Function called to get the intent - this intent
     * has the Article that was been clicked
     */
    private void retrieveIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra(NewsFragment.KEY_ARTICLE)) {
            mArticle = intent.getParcelableExtra(NewsFragment.KEY_ARTICLE);
            mBinding.setNews(mArticle);
            mBinding.setVariable(BR.news, mArticle);
            mBinding.executePendingBindings();
        }
    }

    // to register a custom converter
    @BindingAdapter({"newsImage"})
    public static void loadImage(ImageView imageView, String newsImageUrl) {

        if(!TextUtils.isEmpty(newsImageUrl)) {
            // add a progressbar
            Picasso.with(imageView.getContext())
                    .load(newsImageUrl)
                    .fit()
                    .centerCrop()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
    }

    /**
     * function to initialize the FAB
     */
    private void initializeFAB() {
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        mBinding.fab.setOnClickListener(this);
        mBinding.fabFavorite.setOnClickListener(this);
        mBinding.fabLaunch.setOnClickListener(this);
        mBinding.fabShare.setOnClickListener(this);

        retrieveNewsFavorite();
    }

    /**
     * function to animate the FAB
     */
    private void animateFAB(){
        if(isFabOpen){
            mBinding.fab.startAnimation(rotate_backward);

            mBinding.fabLaunch.startAnimation(fab_close);
            mBinding.fabFavorite.startAnimation(fab_close);
            mBinding.fabShare.startAnimation(fab_close);

            mBinding.fabLaunch.setClickable(false);
            mBinding.fabFavorite.setClickable(false);
            mBinding.fabShare.setClickable(false);

            isFabOpen = false;

        } else {
            mBinding.fab.startAnimation(rotate_forward);

            mBinding.fabLaunch.startAnimation(fab_open);
            mBinding.fabFavorite.startAnimation(fab_open);
            mBinding.fabShare.startAnimation(fab_open);

            mBinding.fabLaunch.setClickable(true);
            mBinding.fabFavorite.setClickable(true);
            mBinding.fabShare.setClickable(true);

            isFabOpen = true;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab_share:
                shareNews();
                animateFAB();
                break;
            case R.id.fab_launch:
                launchURI();
                animateFAB();
                break;
            case R.id.fab_favorite:
                toggleFabFavorites();
                break;
        }
    }

    /**
     * function called to like/unlike news
     */
    private void toggleFabFavorites() {
        updateNewsToDatabase();
    }

    private void toggleFavUI() {
        if(isFavorite)
            mBinding.fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_fav_selected));
        else
            mBinding.fabFavorite.setImageDrawable(getDrawable(R.drawable.ic_fav_unselected));
    }

    private void retrieveNewsFavorite() {
        DatabaseReference dbRefKey = dbReference.child(mArticle.getArticleId());
        DatabaseReference dbRefChild = dbRefKey.child("isFav");
        dbRefChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                isFavorite = Boolean.valueOf(dataSnapshot.getValue(String.class));
                toggleFavUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * function called to update the liked news to the database
     */
    private void updateNewsToDatabase() {
        DatabaseReference dbRef = dbReference.child(mArticle.getArticleId());
        if(isFavorite) {
            isFavorite = false;
            mArticle.setIsFav("false");
            showSnackbar(R.string.str_news_disliked);
        }
        else {
            isFavorite = true;
            mArticle.setIsFav("true");
            showSnackbar(R.string.str_news_liked);
        }
        dbRef.setValue(mArticle);
        toggleFavUI();
    }

    /**
     * function to share the news
     */
    private void shareNews() {
        if(!TextUtils.isEmpty(mArticle.getTitle())) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mArticle.getTitle());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.str_send_to)));
        }
        else {
            showSnackbar(R.string.str_unable_share);
        }
    }

    /**
     * function called to initiate a call to the browser to display
     * news URL
     */
    private void launchURI() {
        if(!TextUtils.isEmpty(mArticle.getUrl())) {
            Uri launchUri = Uri.parse(mArticle.getUrl());
            Intent webIntent = new Intent(Intent.ACTION_VIEW, launchUri);
            startActivity(webIntent);
        }
        else {
            showSnackbar(R.string.str_url_unavailable);
        }
    }

    /**
     * function to display snackbar
     */
    private void showSnackbar(int strResId) {
        if(snackbar == null) {
            snackbar = Snackbar.make(mBinding.coordinatorLayout, strResId, Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar = null;
        }
    }
}
