package com.example.githubapi.data.remote;

import com.example.githubapi.data.model.RepoResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiInterface {


        @GET("/search/repositories?")
        Call<RepoResponse> getTrendingRepositories(@QueryMap Map<String, String> options);




}
