package com.example.githubapi.trending;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.TransformationsKt;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import com.example.githubapi.GitHubApiApplication;
import com.example.githubapi.Utils;
import com.example.githubapi.data.Filter;
import com.example.githubapi.data.GithubApiRepository;
import com.example.githubapi.data.Result;

import com.example.githubapi.data.model.RepoResponse;

import java.util.HashMap;

import java.util.Map;


public class TrendingReposViewModel extends ViewModel {



    private MutableLiveData<Filter> filter = new MutableLiveData<>(Filter.MONTHLY);


    public void setFilter(Filter filter) {
        this.filter.setValue(filter);
        fetchRepos(filter);

    }

    public LiveData<Filter> getFilter(){

        return filter;
    }

    private int currentPage = 1;

    public TrendingReposViewModel(GithubApiRepository githubApiRepository) {
        repository = githubApiRepository;
        fetchRepos(filter.getValue());



    }

    private GithubApiRepository repository;

    private MutableLiveData<Result<RepoResponse>> trendingRepos = new MutableLiveData<>() ;

    public LiveData<Result<RepoResponse>> getTrendingRepos() {
        return trendingRepos;
    }



    public void fetchRepos(Filter filter) {

        repository.getTrendingRepos(buildQuery(filter), new GithubApiRepository.RepositoryCallback<RepoResponse>() {
            @Override
            public void onComplete(Result<RepoResponse> result) {
                trendingRepos.postValue(result);

            }
        });


    }


    private Map<String, String> buildQuery(Filter filter) {

        Map<String, String> data = new HashMap<>();
        data.put("q", Utils.formatTimeFilterQuery(filter));
        data.put("sort", "stars");
        data.put("order", "desc");
        data.put("per_page", "30");
        data.put("page", String.valueOf(currentPage));
        return data;

    }


    static final ViewModelInitializer<TrendingReposViewModel> initializer = new ViewModelInitializer<>(
            TrendingReposViewModel.class,
            creationExtras -> {
                GitHubApiApplication app = (GitHubApiApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;

                return new TrendingReposViewModel(app.getRepository());
            }
    );


}