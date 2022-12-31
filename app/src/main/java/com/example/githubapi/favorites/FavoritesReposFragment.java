package com.example.githubapi.favorites;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.githubapi.R;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.databinding.FragmentFavoritesReposBinding;
import com.example.githubapi.databinding.FragmentTrendingReposBinding;
import com.example.githubapi.trending.TrendingAdapter;
import com.example.githubapi.trending.TrendingReposFragment;
import com.example.githubapi.trending.TrendingReposViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesReposFragment extends Fragment implements TrendingAdapter.OnItemClickListener {

    private FavoritesReposViewModel mViewModel;
    private FragmentFavoritesReposBinding binding;
    private TrendingAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesReposBinding.inflate(inflater, container, false);
        View  view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(FavoritesReposViewModel.initializer)).get(FavoritesReposViewModel.class);;

        mViewModel.getSavedFavoriteRepos().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                adapter = new TrendingAdapter(items, FavoritesReposFragment.this);
                binding.rvFavoritesRepos.setAdapter(adapter);

            }
        });


       setupAdapter();



    }

    private void setupAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvFavoritesRepos.setLayoutManager(layoutManager);



    }

    @Override
    public void onItemClick(int pos, Item item) {

    }

    @Override
    public void onItemFavClick(int pos, Item item) {

    }
}