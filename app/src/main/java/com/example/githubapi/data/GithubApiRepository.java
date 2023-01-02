package com.example.githubapi.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.githubapi.GitHubApiApplication;
import com.example.githubapi.data.source.local.GitRepoLocalDataSource;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.data.model.RepoResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Response;

public class GithubApiRepository {
    private RestClient restClient =
            GitHubApiApplication.getRestClient();
    private GitRepoLocalDataSource localDataSource;
    private List<Item> cashedRepoList;
    private final Executor executor;


    public interface RepositoryCallback<T> {
        void onComplete(Result<T> result);
    }

    public GithubApiRepository(Executor executor, GitRepoLocalDataSource localDataSource) {

        this.executor = executor;
        this.localDataSource = localDataSource;
    }


    public void addRemoveToFavorite(int pos, Item item, boolean add) {
        if (add) {

            saveRepo(item);
        } else {
            deleteRepo(item);
        }


        cashedRepoList.get(pos).setFavorite(add);

    }

    public void saveRepo(Item item) {
        localDataSource.insertGitRepo(item);
    }

    public void deleteRepo(Item item) {

        localDataSource.deleteGitRepo(item);
    }


    public Item getTrendingRepo(int repoId) {
        if (cashedRepoList != null && !cashedRepoList.isEmpty()) {
            for (Item repo : cashedRepoList) {
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

                    if (!refresh && (cashedRepoList != null && !cashedRepoList.isEmpty())) {
                        Result<List<Item>> result = new Result.Success<>(cashedRepoList);
                        callback.onComplete(result);
                        return;
                    }

                    Response<RepoResponse> repoResponse = restClient.getUrlService().getTrendingRepositories(queryMap).execute();
                    if (repoResponse.isSuccessful() && repoResponse.body() != null) {
                        RepoResponse response = repoResponse.body();


                        cashedRepoList = updateStateWithSavedRepos(response.getItems());
                        Result<List<Item>> result = new Result.Success<>(cashedRepoList);
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
