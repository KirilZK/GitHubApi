package com.example.githubapi.trendingdetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.githubapi.R;
import com.example.githubapi.databinding.DetailRepoFragmentBinding;
import com.example.githubapi.databinding.TrendingReposFragmentBinding;
import com.example.githubapi.trending.TrendingReposFragmentDirections;
import com.example.githubapi.trending.TrendingReposViewModel;


public class TrendingRepoDetailFragment extends Fragment {


    private DetailRepoFragmentBinding binding;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DetailRepoFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;


    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int repoId = getArguments() != null ? getArguments().getInt("repoId") : 0;
        binding.id.setText(String.valueOf(repoId));

        Log.d("TAG", repoId +" ");
    }
}