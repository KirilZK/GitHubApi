package com.example.githubapi.data;

import com.example.githubapi.data.ApiInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private ApiInterface apiService;
    private Retrofit retrofit;

    public RestClient(String baseUrl) {
        init(baseUrl);
    }

    private void init(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient())
                .build();
        this.retrofit = retrofit;
        apiService = retrofit.create(ApiInterface.class);
    }

    private OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }

    public ApiInterface getUrlService() {
        return apiService;
    }
    public Retrofit getRetrofit() {
        return retrofit;
    }
}
