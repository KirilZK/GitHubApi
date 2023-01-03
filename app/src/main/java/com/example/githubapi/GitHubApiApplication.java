package com.example.githubapi;

import android.app.Application;
import android.content.Context;

import com.example.githubapi.data.GithubApiRepository;
import com.example.githubapi.data.source.local.GitRepoLocalDataSource;
import com.example.githubapi.data.source.local.GitRepoRoomDatabase;
import com.example.githubapi.data.RestClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GitHubApiApplication extends Application {
    private static RestClient restClient;
    private static GithubApiRepository repository;
    private static Context context ;

    @Override
    public void onCreate() {
        super.onCreate();
        initRestClient();
        repository = getRepository();
        context = getApplicationContext();

    }

    public static Context getAppContext(){
        return context;

    }
    public void initRestClient() {
        restClient = new RestClient(Constants.BASE_URL);
    }


    public static RestClient getRestClient() {
        return restClient;
    }

    public GithubApiRepository getRepository() {
        if (repository == null) {
            ExecutorService executorService = Executors.newFixedThreadPool(4);

            GitRepoLocalDataSource localDataSource =
                    new GitRepoLocalDataSource(GitRepoRoomDatabase.getDatabase(getApplicationContext()).gitRepoDao());
            repository = new GithubApiRepository(executorService,localDataSource,getRestClient());
        }

        return repository;

    }
}
