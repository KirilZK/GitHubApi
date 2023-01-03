package com.example.githubapi.data.source.local;

import androidx.lifecycle.LiveData;

import com.example.githubapi.data.RepoDataSource;
import com.example.githubapi.data.model.Item;

import java.util.List;

public class GitRepoLocalDataSource implements RepoDataSource {
    private GitRepoDao gitRepoDao;

    public GitRepoLocalDataSource(GitRepoDao gitRepoDao) {
        this.gitRepoDao = gitRepoDao;


    }


    @Override
    public void saveGitRepo(Item item) {
        gitRepoDao.insert(item);
    }

    @Override
    public void deleteGitRepo(Item item) {
        gitRepoDao.deleteGitRepo(item);

    }

    @Override
    public LiveData<List<Item>> observeGitRepos() {
        return gitRepoDao.getLiveGitRepos();
    }

    @Override
    public List<Item> getGitRepos() {

        return gitRepoDao.getAllSavedGitRepos();
    }

    public LiveData<Item> observeGitRepo(int id) {
        return gitRepoDao.getRepo(id);
    }


}
