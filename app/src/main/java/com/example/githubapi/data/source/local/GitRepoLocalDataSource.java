package com.example.githubapi.data.source.local;

import androidx.lifecycle.LiveData;

import com.example.githubapi.data.model.Item;

import java.util.List;

public class GitRepoLocalDataSource {
    private GitRepoDao gitRepoDao;
    private LiveData<List<Item>> liveGitRepos;

    public GitRepoLocalDataSource(GitRepoDao gitRepoDao) {
        this.gitRepoDao = gitRepoDao;
        liveGitRepos = gitRepoDao.getLiveGitRepos();


    }

    public void insertGitRepo(Item item) {

        GitRepoRoomDatabase.databaseWriteExecutor.execute(() -> {
            gitRepoDao.insert(item);
        });
    }
    public void deleteGitRepo(Item item) {
        GitRepoRoomDatabase.databaseWriteExecutor.execute(() -> {
            gitRepoDao.deleteGitRepo(item);
        });
    }
    public LiveData<List<Item>> observeGitRepos() {
        return liveGitRepos;
    }

    public List<Item> getGitRepos() {

        return gitRepoDao.getAllSavedGitRepos();
    }

    public LiveData<Item> observeGitRepo(int id) {
        return gitRepoDao.getRepo(id);
    }


}
