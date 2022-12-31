package com.example.githubapi.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.githubapi.data.model.Item;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract class GitRepoRoomDatabase extends RoomDatabase {


    public abstract GitRepoDao gitRepoDao();

    private static volatile GitRepoRoomDatabase INSTANCE;
    static final int NUMBER_OF_THREAD = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);


   public static GitRepoRoomDatabase getDatabase(final Context context){


        if(INSTANCE == null){

            synchronized (GitRepoRoomDatabase.class){

                if(INSTANCE == null){

                    INSTANCE = Room.databaseBuilder(context,
                    GitRepoRoomDatabase.class,"git_repo_database")
                            .build();
                }
            }

        }

        return INSTANCE;
    }

}
