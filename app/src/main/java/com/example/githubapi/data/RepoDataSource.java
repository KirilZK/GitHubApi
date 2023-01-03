package com.example.githubapi.data;

import androidx.lifecycle.LiveData;

import com.example.githubapi.data.model.Item;
import com.example.githubapi.data.source.local.GitRepoRoomDatabase;

import java.util.List;

public interface RepoDataSource {

    void saveGitRepo(Item item);

    void deleteGitRepo(Item item);

    LiveData<List<Item>> observeGitRepos();

    List<Item> getGitRepos();

    LiveData<Item> observeGitRepo(int id);


}
