package com.example.githubapi.trendingdetail;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.githubapi.GitHubApiApplication;
import com.example.githubapi.data.GithubApiRepository;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.favorites.FavoritesReposViewModel;

public class RepoDetailViewModel extends ViewModel {
    private final GithubApiRepository repository;


    private MutableLiveData<Integer> repoId = new MutableLiveData<Integer>();


    private final LiveData<Item> repoDetail = Transformations.map(repoId, repoId -> getRepoDetail(repoId));

    public LiveData<Item> getRepoDetail() {

        return repoDetail;

    }

    public Item getRepoDetail(int repoId) {

        return repository.getTrendingRepo(repoId);
    }


    public RepoDetailViewModel(GithubApiRepository githubApiRepository) {
        repository = githubApiRepository;

    }

    public void start(int id) {
        if (repoId.getValue() != null && repoId.getValue() == id) {
            return;
        }

        repoId.setValue(id);

    }

    static final ViewModelInitializer<RepoDetailViewModel> initializer = new ViewModelInitializer<>(
            RepoDetailViewModel.class,
            creationExtras -> {
                GitHubApiApplication app = (GitHubApiApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;

                return new RepoDetailViewModel(app.getRepository());
            }
    );

    public void openRepoPage(String url) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        GitHubApiApplication.getAppContext().startActivity(browserIntent);

    }

}