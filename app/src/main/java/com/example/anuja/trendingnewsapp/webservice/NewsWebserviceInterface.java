package com.example.anuja.trendingnewsapp.webservice;

import com.example.anuja.trendingnewsapp.model.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * An Interface (Retrofit turns your HTTP API into a Java interface.)
 */
public interface NewsWebserviceInterface {

    /**
     * @Get declares an HTTP GET request
     * @Path("endpoint") annotation on the endpoint (top-headlines) parameter
     * marks it as a replacement for the {endpoint} placeholder in the @GET path
     * @Query annotation is used to get the api key in order to access the news
     * data
     * @param endpoint - placeholder for ( top-headlines )
     * @param apiKey - api key
     * @return a news object based on the specified criteria
     */
    @GET(NewsUtils.NEWS_URL_VERSION + "{endpoint}")
    Call<NewsModel> getNews(@Path(NewsUtils.ENDPOINT_PARAM) String endpoint,
                            @Query(NewsUtils.COUNTRY_PARAM) String country,
                            @Query(NewsUtils.API_KEY_PARAM) String apiKey);

    @GET(NewsUtils.NEWS_URL_VERSION + "{endpoint}")
    Call<NewsModel> getNewsByCategory(@Path(NewsUtils.ENDPOINT_PARAM) String endpoint,
                                      @Query(NewsUtils.COUNTRY_PARAM) String country,
                                      @Query(NewsUtils.CATEGORY_PARAM) String category,
                                      @Query(NewsUtils.API_KEY_PARAM) String apiKey);
}
