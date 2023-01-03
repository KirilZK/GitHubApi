package com.example.githubapi.data.source.local;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.githubapi.data.model.Item;

import java.util.List;

@Dao
public interface GitRepoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Delete
    void deleteGitRepo(Item item);


    @Query("SELECT * FROM git_repo")
    List<Item> getAllSavedGitRepos();

    @Query("SELECT * FROM git_repo")
    LiveData<List<Item>> getLiveGitRepos();

    @Query("SELECT * FROM git_repo WHERE id = :id")
    LiveData<Item> getRepo(int id);


}
