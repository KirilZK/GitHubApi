package com.example.githubapi.favorites;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.githubapi.GitHubApiApplication;
import com.example.githubapi.data.GithubApiRepository;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.trending.TrendingReposViewModel;

import java.util.List;

public class FavoritesReposViewModel extends ViewModel {
    private final GithubApiRepository repository;


   private final LiveData<List<Item>> savedFavoriteRepos;

    public LiveData<List<Item>> getSavedFavoriteRepos(){

        return  savedFavoriteRepos;
    }


    public FavoritesReposViewModel(GithubApiRepository githubApiRepository) {
        repository = githubApiRepository;
        savedFavoriteRepos = getSavedFavorites();


    }

    private LiveData<List<Item>> getSavedFavorites() {
      return   repository.observeSavedFavRepos();


    }
    public void setFavoriteRepo( Item item,boolean favorite) {

        repository.addRemoveToFavorite(item,favorite);


    }



    static final ViewModelInitializer<FavoritesReposViewModel> initializer = new ViewModelInitializer<>(
            FavoritesReposViewModel.class,
            creationExtras -> {
                GitHubApiApplication app = (GitHubApiApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;

                return new FavoritesReposViewModel(app.getRepository());
            }
    );
}