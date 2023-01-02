package com.example.githubapi.trending;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.githubapi.R;
import com.example.githubapi.data.model.Item;
import com.example.githubapi.databinding.ItemTrendingRepoBinding;

import java.util.List;

public class TrendingAdapter extends
        RecyclerView.Adapter<TrendingAdapter.ViewHolder> {


    public interface OnItemClickListener{
        void onItemClick(int pos,Item item);
        void onItemFavClick(int pos,Item item);
    }

    private List<Item> trendingRepos;

    private OnItemClickListener onItemClickListener;


    public TrendingAdapter(List<Item> contacts,OnItemClickListener onItemClickListener) {
        trendingRepos = contacts;
        this.onItemClickListener = onItemClickListener;
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



        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(holder.getAdapterPosition(), item);
            }
        });

         holder.binding.btnFav.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 onItemClickListener.onItemFavClick(holder.getAdapterPosition(), item);
             }
         });

         if(item.isFavorite()){
             holder.binding.btnFav.setColorFilter((ContextCompat.getColor(holder.binding.btnFav.getContext(), R.color.fav_selected)), android.graphics.PorterDuff.Mode.SRC_IN);
         }else{
             holder.binding.btnFav.setColorFilter((ContextCompat.getColor(holder.binding.btnFav.getContext(), R.color.fav_disabled)), android.graphics.PorterDuff.Mode.SRC_IN);

         }
        holder.binding.tvRepoName.setText(item.getName());
        String formattedName = String.format(holder.binding.btnFav.getContext().getString(R.string.owner_name), item.getOwner().getLogin());
        holder.binding.tvAuthor.setText(formattedName);
        if(TextUtils.isEmpty(item.getDescription())){
            holder.binding.tvDesc.setText(holder.binding.tvDesc.getContext().getString(R.string.empty_repo_desc));
        }else{
            holder.binding.tvDesc.setText(item.getDescription());
        }

        holder.binding.tvStars.setText(String.valueOf(item.getStargazersCount()));
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