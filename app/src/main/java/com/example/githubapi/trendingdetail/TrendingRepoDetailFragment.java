package com.example.githubapi.trendingdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.githubapi.R;

import com.example.githubapi.Utils;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.databinding.FragmentDetailRepoBinding;

import com.example.githubapi.favorites.FavoritesReposFragment;
import com.example.githubapi.favorites.FavoritesReposViewModel;
import com.example.githubapi.trending.TrendingAdapter;
import com.example.githubapi.trending.TrendingReposFragmentDirections;
import com.example.githubapi.trending.TrendingReposViewModel;

import java.util.List;


public class TrendingRepoDetailFragment extends Fragment {


    private FragmentDetailRepoBinding binding;
    private RepoDetailViewModel mViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_repo, container, false);


        View view = binding.getRoot();
        return view;


    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mViewModel =  new ViewModelProvider(this, ViewModelProvider.Factory.from(RepoDetailViewModel.initializer)).get(RepoDetailViewModel.class);
    ;


        binding.setMViewModel(mViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());



        int repoId = getArguments() != null ? getArguments().getInt("repoId") : 0;
        Log.d("TAG", repoId +" ");
        if(repoId!= 0){
            mViewModel.start(repoId);
        }else{
            //show error view
        }




    }
}