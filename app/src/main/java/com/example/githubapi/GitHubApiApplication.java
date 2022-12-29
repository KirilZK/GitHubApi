package com.example.githubapi;

import android.app.Application;

import com.example.githubapi.data.remote.RestClient;

public class GitHubApiApplication extends Application {
    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        initRestClient();

    }


    public   void initRestClient(){

        restClient = new RestClient(Constants.BASE_URL);
    }



    public static RestClient getRestClient() {
        return restClient;
    }

}
