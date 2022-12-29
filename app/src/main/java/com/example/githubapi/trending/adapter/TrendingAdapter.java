package com.example.githubapi.trending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.databinding.ItemTrendingRepoBinding;

import java.util.List;

public class TrendingAdapter extends
        RecyclerView.Adapter<TrendingAdapter.ViewHolder> {

    private List<Item> trendingRepos;


    public TrendingAdapter(List<Item> contacts) {
        trendingRepos = contacts;
    }

    public TrendingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewHolder viewHolder = new ViewHolder(ItemTrendingRepoBinding.inflate(inflater));
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TrendingAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Item item = trendingRepos.get(position);

        holder.binding.txtAuthor.setText(item.getOwner().getLogin());
        Glide.with(holder.binding.imgRepo.getContext())
                .load(item.getOwner().getAvatarUrl())
                .into( holder.binding.imgRepo);



    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return trendingRepos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemTrendingRepoBinding binding;

        public ViewHolder(ItemTrendingRepoBinding b){
            super(b.getRoot());
            binding = b;
        }

    }
}