package com.example.githubapi.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.githubapi.data.source.local.GitRepoLocalDataSource;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.data.model.RepoResponse;
import com.example.githubapi.data.source.local.GitRepoRoomDatabase;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Response;

public class GithubApiRepository {
    private ApiInterface restClient;

    private RepoDataSource localDataSource;
    private List<Item> networkCashedRepoList;
    private final Executor executor;


    public interface RepositoryCallback<T> {
        void onComplete(Result<T> result);
    }

    public GithubApiRepository(Executor executor, RepoDataSource localDataSource, ApiInterface restClient) {
        this.restClient = restClient;
        this.executor = executor;
        this.localDataSource = localDataSource;
    }


    public void addRemoveToFavorite(Item item, boolean add) {
        if (add) {
            executor.execute(() -> {
                saveRepo(item);
            });

        } else {
            executor.execute(() -> {
                deleteRepo(item);
            });

        }
        updateCachedList(item.getId(),add);

    }

    private void updateCachedList(int id,boolean add){
        for (Item item: networkCashedRepoList) {
            if(item.getId() == id){
                item.setFavorite(add);
                return;
            }
        }

    }

    public void saveRepo(Item item) {
        localDataSource.saveGitRepo(item);
    }

    public void deleteRepo(Item item) {

        localDataSource.deleteGitRepo(item);
    }


    public LiveData<Item> observeRepo(int id) {
        return localDataSource.observeGitRepo(id);


    }


    /// wrap inside result object to handle null values

    public Item getTrendingRepo(int repoId) {
        if (networkCashedRepoList != null && !networkCashedRepoList.isEmpty()) {
            for (Item repo : networkCashedRepoList) {
                if (repo.getId() == repoId) {
                    return repo;
                }
            }
        }
        return null;
    }

    //Use only on background thread
    public List<Item> getSavedFavRepos() {


        return localDataSource.getGitRepos();
    }


    public LiveData<List<Item>> observeSavedFavRepos() {


        return localDataSource.observeGitRepos();
    }


    public List<Item> updateStateWithSavedRepos(List<Item> networkItems) {


        List<Item> saved = getSavedFavRepos();

        for (int i = 0; i < networkItems.size(); i++) {

            Item networkItem = networkItems.get(i);

            for (int j = 0; j < saved.size(); j++) {

                Item savedItem = saved.get(j);

                if (networkItem.getId() == savedItem.getId()) {
                    networkItem.setFavorite(true);
                    break;
                }
            }


        }
        return networkItems;


    }

    public void getTrendingRepos(Map<String, String> queryMap, final RepositoryCallback<List<Item>> callback, boolean refresh) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    // return cached version updated with fav filed

                    if (!refresh && (networkCashedRepoList != null && !networkCashedRepoList.isEmpty())) {
                        Result<List<Item>> result = new Result.Success<>(networkCashedRepoList);
                        callback.onComplete(result);
                        return;
                    }

                    Response<RepoResponse> repoResponse = restClient.getTrendingRepositories(queryMap).execute();
                    if (repoResponse.isSuccessful() && repoResponse.body() != null) {
                        RepoResponse response = repoResponse.body();


                        networkCashedRepoList = updateStateWithSavedRepos(response.getItems());
                        Result<List<Item>> result = new Result.Success<>(networkCashedRepoList);
                        callback.onComplete(result);

                    } else {
                        Result<List<Item>> errorResult = new Result.Error<>(new Exception("Invalid response"));
                        callback.onComplete(errorResult);
                        Log.d("TAG", "invalid response");
                    }


                } catch (Exception e) {
                    Result<List<Item>> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                    Log.d("TAG", "error");
                }
            }
        });


    }

}
