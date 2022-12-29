package com.example.githubapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.githubapi.data.Result;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.data.model.RepoResponse;
import com.example.githubapi.data.GithubApiRepository;
import com.example.githubapi.databinding.ActivityMainBinding;
import com.example.githubapi.trending.adapter.TrendingAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
         initAdapter();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Map<String, String> data = new HashMap<>();
        data.put("q", " created:>2022-08-01");
        data.put("sort","stars");
        data.put("order","desc");
        data.put("per_page","50");

        new GithubApiRepository(executorService).getTrendingRepos(data, new GithubApiRepository.RepositoryCallback<RepoResponse>() {
            @Override
            public void onComplete(Result<RepoResponse> result) {
                if (result instanceof Result.Success) {
                    List<Item>  items = ((Result.Success<RepoResponse>) result).data.getItems();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.rvTrendingRepos.setAdapter(new TrendingAdapter(items));
                        }
                    });

                } else {
                    // Show error in UI
                }

            }
        });






    }


    private void initAdapter(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvTrendingRepos.setLayoutManager(layoutManager);

    }
}