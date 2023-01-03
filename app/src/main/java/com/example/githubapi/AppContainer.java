package com.example.githubapi;

import com.example.githubapi.data.ApiInterface;
import com.example.githubapi.data.GithubApiRepository;
import com.example.githubapi.data.RepoDataSource;
import com.example.githubapi.data.source.local.GitRepoLocalDataSource;
import com.example.githubapi.data.source.local.GitRepoRoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppContainer {

    private GithubApiRepository repository;

    private  ApiInterface apiService= new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient.Builder().build())
            .build()
            .create(ApiInterface .class);


    public ApiInterface getApiService(){

        return apiService;
    }


    public GithubApiRepository getRepository() {
        if (repository == null) {
            ExecutorService executorService = Executors.newFixedThreadPool(4);

            RepoDataSource localDataSource =
                    new GitRepoLocalDataSource(GitRepoRoomDatabase.getDatabase(GitHubApiApplication.getAppContext()).gitRepoDao());
            repository = new GithubApiRepository(executorService,localDataSource,apiService);
        }

        return repository;

    }


}
