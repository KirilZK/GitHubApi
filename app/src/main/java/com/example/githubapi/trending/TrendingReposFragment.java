package com.example.githubapi.trending;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.githubapi.MainActivity;
import com.example.githubapi.R;
import com.example.githubapi.data.GithubApiRepository;
import com.example.githubapi.data.Result;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.data.model.RepoResponse;
import com.example.githubapi.databinding.TrendingReposFragmentBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrendingReposFragment extends Fragment implements TrendingAdapter.OnItemClickListener {

    private TrendingReposViewModel mViewModel;

    private TrendingReposFragmentBinding binding;
    private View view;

    public List<Item> repos;


    public static TrendingReposFragment newInstance() {
        return new TrendingReposFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = TrendingReposFragmentBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TrendingReposViewModel.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvTrendingRepos.setLayoutManager(layoutManager);
        if (repos == null || repos.isEmpty()) {
            getData();

        }else{
            binding.rvTrendingRepos.setAdapter(new TrendingAdapter(repos, TrendingReposFragment.this));
        }


    }

    @Override
    public void onItemClick(Item item) {
        Log.d("TAG", "on item clicked");

        TrendingReposFragmentDirections.ActionFullListFragmentToDetailFragment action = TrendingReposFragmentDirections.actionFullListFragmentToDetailFragment(item.getId());
        Navigation.findNavController(view).navigate(action);

    }


    private void getData() {


        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Map<String, String> data = new HashMap<>();
        data.put("q", " created:>2022-08-01");
        data.put("sort", "stars");
        data.put("order", "desc");
        data.put("per_page", "50");

        new GithubApiRepository(executorService).getTrendingRepos(data, new GithubApiRepository.RepositoryCallback<RepoResponse>() {
            @Override
            public void onComplete(Result<RepoResponse> result) {
                if (result instanceof Result.Success) {
                    List<Item> items = ((Result.Success<RepoResponse>) result).data.getItems();
                    repos = items;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.rvTrendingRepos.setAdapter(new TrendingAdapter(items, TrendingReposFragment.this));
                        }
                    });

                } else {
                    // Show error in UI
                }

            }
        });

    }

}