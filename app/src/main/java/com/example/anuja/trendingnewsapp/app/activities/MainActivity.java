package com.example.anuja.trendingnewsapp.app.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.anuja.trendingnewsapp.R;
import com.example.anuja.trendingnewsapp.app.fragments.AboutUsFragment;
import com.example.anuja.trendingnewsapp.app.fragments.NewsFragment;
import com.example.anuja.trendingnewsapp.app.fragments.FavoritesFragment;
import com.example.anuja.trendingnewsapp.databinding.ActivityMainBinding;
import com.example.anuja.trendingnewsapp.databinding.NavHeaderBinding;
import com.example.anuja.trendingnewsapp.model.MainModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Arrays;

/**
 * This is the MainActivity - the Dashboard Activity.
 * This activity is responsible for handling the following:
 * 1) Firebase Authentication
 */
public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1; // request code
    private static final String ANONYMOUS = "Anonymous";
    private static final String KEY_MODEL_POSITION = "model_position";
    private static final String KEY_MODEL_POSITION_TITLE = "model_position_title";

    // firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mFirebaseUser = null;
    private String username;
    private Uri userPhotoUri;

    private ActionBar mActionBar;

    private ActionBarDrawerToggle toggle;
    private Fragment fragment = null;
    private MainModel mainModel;

    private ActivityMainBinding mBinding;
    private NavHeaderBinding mNavHeaderBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header, mBinding.navView, false);
        mBinding.navView.addHeaderView(mNavHeaderBinding.getRoot());

        initializeModel(savedInstanceState);
        initiateFirebaseAuthentication();
    }

    private void initializeModel(Bundle savedInstanceState) {
        mainModel = new MainModel();

        if(savedInstanceState != null) {
            mainModel.setSelectedPosition(savedInstanceState.getInt(KEY_MODEL_POSITION));
            mainModel.setSelectedPositionTitle(savedInstanceState.getString(KEY_MODEL_POSITION_TITLE));
        }
        else {
            mainModel.setSelectedPosition(R.id.menu_all_news);
            mainModel.setSelectedPositionTitle(getResources().getString(R.string.str_all_news));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

        if(mFirebaseUser != null) {
            displaySelectedFragment();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    /**
     * function used to initiate firebase authentication
     */
    private void initiateFirebaseAuthentication() {
        this.username = ANONYMOUS;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    // user is signed in
                    onSignedIn(mFirebaseUser.getDisplayName(), mFirebaseUser.getPhotoUrl());
                } else {
                    // user is signed out
                    onSignedOut();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    /**
     * function used when the user is signed in
     * @param user - logged in user name
     */
    private void onSignedIn(String user, Uri userPhotoUri) {
        this.username = user;
        this.userPhotoUri = userPhotoUri;
        Log.i("Test", "user signed in");
        initializeToolbar();
        initializeNavigationDrawer();
    }

    /**
     * function used when the user is signed out
     */
    private void onSignedOut() {
        this.username = ANONYMOUS;
    }

    /**
     * function used to create navigation drawer
     */
    private void initializeNavigationDrawer() {
        Log.i("Test", "initialize navigationview: " + mNavHeaderBinding.tvUserName);

        setUsernameDetails();
        performDrawerToggle();

        mBinding.navView.setCheckedItem(mainModel.getSelectedPosition());
        displaySelectedFragment();

        mBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mBinding.drawerLayout.closeDrawers();

                mainModel.setSelectedPosition(menuItem.getItemId());
                mainModel.setSelectedPositionTitle(menuItem.getTitle().toString());
                displaySelectedFragment();
                // swap fragments here

                return true;
            }
        });
    }

    private void setUsernameDetails() {
        if(!TextUtils.isEmpty(username)) {
            Log.i("Test", "navigationheader: " + mNavHeaderBinding.tvUserName + "      " + username);
            mNavHeaderBinding.tvUserName.setText(username);
        }

        if(userPhotoUri != null)
            uriToBitmap(userPhotoUri);
    }

    /**
     * function used to convert the uri to bitmap
     * @param selectedFileUri
     */
    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            mNavHeaderBinding.imgUserPhoto.setImageBitmap(image);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function used to perform the actionbar drawer toggle
     */
    private void performDrawerToggle() {
        toggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, mBinding.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * function called to display the fragment based on the menu
     * item selected from the navigation drawer
     */
    private void displaySelectedFragment() {
        mBinding.navView.setCheckedItem(mainModel.getSelectedPosition());
        mActionBar.setTitle(mainModel.getSelectedPositionTitle());

        int menuItemId = mainModel.getSelectedPosition();
        switch(menuItemId) {
            case R.id.menu_all_news:
                fragment = new NewsFragment();
                break;
            case R.id.menu_favorites:
                fragment = new FavoritesFragment();
                break;
            case R.id.menu_about_us:
                fragment = new AboutUsFragment();
                break;
            default:
                break;
        }

        if(fragment != null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * function to initialize the toolbar
     */
    private void initializeToolbar() {
        setSupportActionBar(mBinding.toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mBinding.drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_signout:
                AuthUI.getInstance().signOut(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "User Signed IN!!", Toast.LENGTH_SHORT).show();
            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "User Signed OUT!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        toggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_MODEL_POSITION, mainModel.getSelectedPosition());
        outState.putString(KEY_MODEL_POSITION_TITLE, mainModel.getSelectedPositionTitle());
    }
}
