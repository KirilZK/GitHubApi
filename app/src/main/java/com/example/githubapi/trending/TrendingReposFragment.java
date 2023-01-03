package com.example.githubapi.trending;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.githubapi.R;
import com.example.githubapi.data.Filter;
import com.example.githubapi.data.Result;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.data.model.RepoResponse;
import com.example.githubapi.databinding.FragmentTrendingReposBinding;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrendingReposFragment extends Fragment implements TrendingAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private TrendingReposViewModel mViewModel;


    private FragmentTrendingReposBinding binding;
    private View view;
    private Map<Filter, String> spinnerValuesMap = new HashMap<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentTrendingReposBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG", "on view created");
        mViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.Factory.from(TrendingReposViewModel.initializer)
        ).get(TrendingReposViewModel.class);
        final Observer<Result<List<Item>>> repoObserver = new Observer<Result<List<Item>>>() {
            @Override
            public void onChanged(@Nullable final Result<List<Item>> result) {
                Log.d("TAG", "on changed");
                if (result instanceof Result.Success) {
                    List<Item> items = ((Result.Success<List<Item>>) result).data;
                    showErrorView(false);
                    binding.rvTrendingRepos.setAdapter(new TrendingAdapter(items, TrendingReposFragment.this));

                } else {
                    showErrorView(true);
                    Log.d("TAG", "Error");
                }
            }
        };

        mViewModel.getTrendingRepos().observe(getViewLifecycleOwner(), repoObserver);


        binding.btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Navigation.findNavController(view).navigate(TrendingReposFragmentDirections.actionTrendingReposFragmentToFavorites());
            }
        });

        setupSpinner();

        setupAdapter();


    }

    private void showErrorView(boolean show) {
        binding.ivError.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        binding.rvTrendingRepos.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        binding.tvError.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }


    private void setupAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvTrendingRepos.setLayoutManager(layoutManager);
        binding.rvTrendingRepos.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }

    private void setupSpinner() {
        buildSpinnerFilterMap();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filter_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setOnItemSelectedListener(this);
        binding.spinner.setAdapter(adapter);
    }

    //
    private void buildSpinnerFilterMap() {

        spinnerValuesMap.put(Filter.MONTHLY, "Monthly");
        spinnerValuesMap.put(Filter.WEEKLY, "Weekly");
        spinnerValuesMap.put(Filter.DAILY, "Daily");


    }


    private void onFilterSelected(String selectedFilter) {
        Filter filter;
        Log.d("TAG", selectedFilter + " clicked");
        switch (selectedFilter) {

            case "Daily":
                filter = Filter.DAILY;
                break;
            case "Weekly":
                filter = Filter.WEEKLY;
                break;
            case "Monthly":

            default:
                filter = Filter.MONTHLY;
        }
        mViewModel.filterTrendingRepos(filter);


    }

    @Override
    public void onItemClick(int pos, Item item) {
        Log.d("TAG", "on item clicked" + pos);


        TrendingReposFragmentDirections.ActionFullListFragmentToDetailFragment action = TrendingReposFragmentDirections.actionFullListFragmentToDetailFragment(item.getId());
        Navigation.findNavController(view).navigate(action);

    }

    @Override
    public void onItemFavClick(int pos, Item item) {
        Log.d("TAG", "on item clicked fav" + pos);
        mViewModel.setFavoriteRepo(item, !item.isFavorite());
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // prevent multiple calls to fetch new data when Configuration change happened
        // (OnItemSelected listener is called on every spinner initialization)

        if (String.valueOf(adapterView.getItemAtPosition(i)).equalsIgnoreCase(spinnerValuesMap.get(mViewModel.getFilter().getValue())))
            return;

        Log.d("TAG", i + " clicked");
        onFilterSelected((String) adapterView.getItemAtPosition(i));


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


}