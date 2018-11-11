package com.example.anuja.trendingnewsapp.webservice;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRetrofitClient {

    private static NewsWebserviceInterface mWebserviceInterface;
    private static NewsRetrofitClient mRetrofitClient;

    public synchronized static NewsRetrofitClient getInstance() {
        if(mRetrofitClient == null) {
            mRetrofitClient = new NewsRetrofitClient();
        }

        return mRetrofitClient;
    }

    private NewsRetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsUtils.NEWS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mWebserviceInterface = retrofit.create(NewsWebserviceInterface.class);

    }

    public NewsWebserviceInterface getNewsWebservice() {
        return mWebserviceInterface;
    }
}
