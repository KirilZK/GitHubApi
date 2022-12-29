package com.example.githubapi.trending;

import androidx.lifecycle.Observer;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.githubapi.Constants;
import com.example.githubapi.MainActivity;
import com.example.githubapi.R;
import com.example.githubapi.Utils;
import com.example.githubapi.data.Filter;
import com.example.githubapi.data.GithubApiRepository;
import com.example.githubapi.data.Result;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.data.model.RepoResponse;
import com.example.githubapi.databinding.TrendingReposFragmentBinding;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrendingReposFragment extends Fragment implements TrendingAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private TrendingReposViewModel mViewModel;


    private TrendingReposFragmentBinding binding;
    private View view;
    private Map<Filter,String> map = new HashMap<>();


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
        mViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.Factory.from(TrendingReposViewModel.initializer)
        ).get(TrendingReposViewModel.class);

        buildSpinnerFilterMap();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filter_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setOnItemSelectedListener(this);
        binding.spinner.setAdapter(adapter);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvTrendingRepos.setLayoutManager(layoutManager);


        final Observer<Result<RepoResponse>> repoObserver = new Observer<Result<RepoResponse>>() {
            @Override
            public void onChanged(@Nullable final Result<RepoResponse> result) {
                Log.d("TAG", "on changed");
                if (result instanceof Result.Success) {
                    List<Item> items = ((Result.Success<RepoResponse>) result).data.getItems();

                    binding.rvTrendingRepos.setAdapter(new TrendingAdapter(items, TrendingReposFragment.this));

                } else {
                    Log.d("TAG", "Error");
                }
            }
        };

        mViewModel.getTrendingRepos().observe(getViewLifecycleOwner(), repoObserver);



    }
//
    private void buildSpinnerFilterMap(){

        map.put(Filter.MONTHLY,"Monthly");
        map.put(Filter.WEEKLY,"Weekly");
        map.put(Filter.DAILY,"Daily");



    }


    private void onFilterSelected(String selectedFilter) {
        Filter filter;
        Log.d("TAG", selectedFilter+ " clicked");
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
        mViewModel.setFilter(filter);


    }

    @Override
    public void onItemClick(Item item) {
        Log.d("TAG", "on item clicked");

        TrendingReposFragmentDirections.ActionFullListFragmentToDetailFragment action = TrendingReposFragmentDirections.actionFullListFragmentToDetailFragment(item.getId());
        Navigation.findNavController(view).navigate(action);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // prevent multiple calls to fetch new data when Configuration change happened
        // (OnItemSelected listener is called on every spinner initialization)

        if(String.valueOf( adapterView.getItemAtPosition(i)).equalsIgnoreCase(map.get(mViewModel.getFilter().getValue())))
            return;





        Log.d("TAG", i +" clicked");
           onFilterSelected((String) adapterView.getItemAtPosition(i));



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}