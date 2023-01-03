package com.example.githubapi.trending;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import com.example.githubapi.GitHubApiApplication;
import com.example.githubapi.Utils;
import com.example.githubapi.data.Filter;
import com.example.githubapi.data.GithubApiRepository;
import com.example.githubapi.data.Result;

import com.example.githubapi.data.model.Item;

import java.util.HashMap;

import java.util.List;
import java.util.Map;


public class TrendingReposViewModel extends ViewModel {

    private GithubApiRepository repository;
    private MutableLiveData<Filter> filter = new MutableLiveData<>(Filter.MONTHLY);

    public LiveData<Filter> getFilter() {
        return filter;
    }

    public TrendingReposViewModel(GithubApiRepository githubApiRepository) {
        repository = githubApiRepository;
        getRepos(filter.getValue(),true);


    }

    private MutableLiveData<Result<List<Item>>> trendingRepos = new MutableLiveData<>();

    public LiveData<Result<List<Item>>> getTrendingRepos() {
        return trendingRepos;
    }


    public void filterTrendingRepos(Filter filter) {
        this.filter.setValue(filter);
        getRepos(filter,true);

    }

    public void getRepos(Filter filter,boolean refresh) {

        repository.getTrendingRepos(buildQuery(filter), new GithubApiRepository.RepositoryCallback<List<Item>>() {
            @Override
            public void onComplete(Result<List<Item>> result) {
                trendingRepos.postValue(result);


            }
        },refresh);


    }


    public void setFavoriteRepo( Item item,boolean favorite) {

         repository.addRemoveToFavorite(item,favorite);
         getRepos(filter.getValue(),false);

    }


    private Map<String, String> buildQuery(Filter filter) {
        Map<String, String> data = new HashMap<>();
        data.put("q", Utils.formatTimeFilterQuery(filter));
        data.put("sort", "stars");
        data.put("order", "desc");
        data.put("per_page", "50");
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