package com.example.githubapi.data;

import android.util.Log;

import com.example.githubapi.GitHubApiApplication;
import com.example.githubapi.data.FilterTimeSpanDescriptor;
import com.example.githubapi.data.Result;
import com.example.githubapi.data.model.RepoResponse;
import com.example.githubapi.data.remote.RestClient;

import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Response;

public class GithubApiRepository {
    RestClient restClient =
            GitHubApiApplication.getRestClient();



   public interface RepositoryCallback<T> {
        void onComplete(Result<T> result);
    }

    private final Executor executor;

    public GithubApiRepository(Executor executor) {

        this.executor = executor;
    }



    public void getTrendingRepos(Map<String,String> queryMap, final RepositoryCallback<RepoResponse> callback ){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                  Response<RepoResponse> repoResponse =   restClient.getUrlService().getTrendingRepositories(queryMap).execute();
                    if (repoResponse.isSuccessful() && repoResponse.body() != null) {
                        RepoResponse response = repoResponse.body();
                        Log.d("TAG",response.getTotalCount() + " count");

                        Result<RepoResponse> result = new Result.Success<>(response);
                        callback.onComplete(result);
                    }else{
                        Result<RepoResponse> errorResult = new Result.Error<>(new Exception("Invalid response"));
                        callback.onComplete(errorResult);
                        Log.d("TAG", "invalid response");
                    }


                } catch (Exception e) {
                    Result<RepoResponse> errorResult = new Result.Error<>(e);
                    callback.onComplete(errorResult);
                    Log.d("TAG", "error");
                }
            }
        });


    }

}
