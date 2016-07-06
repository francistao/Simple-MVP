package com.francis.simple_mvp.mvp.model;


import retrofit.http.GET;
import rx.Observable;

/**
 * Created by dream on 16/7/6.
 */
public interface FactRestService {

    final String SERVICE_ENDPOINT = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    /**
     * Use Retrofit to get JSON from URL, then parse it.
     * @return Observable
     */
    @GET("/")
    Observable<Country> getCountry();
}
