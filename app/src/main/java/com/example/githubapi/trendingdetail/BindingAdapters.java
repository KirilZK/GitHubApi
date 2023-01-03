package com.example.githubapi.trendingdetail;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.githubapi.R;
import com.example.githubapi.Utils;

public class BindingAdapters {

    @BindingAdapter({"rawTime"})
    public static void setText(TextView view, String rawTime) {
        String formattedTime = Utils.formatRepoTime(rawTime);
        String timeResource = String.format(view.getContext().getString(R.string.created), formattedTime);
        view.setText(timeResource);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .into(view);
    }
}
