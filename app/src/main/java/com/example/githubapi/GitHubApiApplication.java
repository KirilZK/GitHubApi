package com.example.githubapi;

import android.app.Application;
import android.content.Context;


public class GitHubApiApplication extends Application {
    private static Context context;


    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        appContainer = new AppContainer();
    }

    public static Context getAppContext() {
        return context;

    }

    public AppContainer getAppContainer() {
        return appContainer;
    }


}
