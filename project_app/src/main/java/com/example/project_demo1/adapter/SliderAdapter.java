package com.example.project_demo1.adapter;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_demo1.R;
import com.example.project_demo1.model.Restaurant;
import com.google.gson.Gson;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{

    private List<Restaurant> latestRestaurantList;
    private NavController navController;
    private List<Bitmap> downloadedPic;

    // pass data into adapter
    public SliderAdapter(List<Restaurant> latestRestaurantList, NavController navController, List<Bitmap> downloadedPic) {
        // input data
        this.latestRestaurantList = latestRestaurantList;
        this.navController = navController;
        this.downloadedPic = downloadedPic;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_latest_item, parent, false);
        return new SliderViewHolder(view, latestRestaurantList, downloadedPic, navController);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {

        holder.getHome_latest_tv().setText(latestRestaurantList.get(position).getName());
        holder.getHome_latest_desc().setText(latestRestaurantList.get(position).getDescription());
        holder.getHome_latest_iv().setImageBitmap(downloadedPic.get(position));
    }

    @Override
    public int getItemCount() {
        return latestRestaurantList.size();
    }

    // View holder
    public static class SliderViewHolder extends RecyclerView.ViewHolder {

        private ImageView home_latest_iv;
        private TextView home_latest_tv;
        private TextView home_latest_desc;

        public SliderViewHolder(@NonNull View itemView, List<Restaurant> latestRestaurantList, List<Bitmap> downloadedPicList, NavController navController) {
            super(itemView);
            home_latest_iv = itemView.findViewById(R.id.home_latest_iv);
            home_latest_tv = itemView.findViewById(R.id.home_latest_tv);
            home_latest_desc = itemView.findViewById(R.id.home_latest_desc);

            /* ------- Navigate to info page when user click -------- */

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // get current position of item
                    int position = getAdapterPosition();

                    // pass the current data to next fragment via bundle
                    Restaurant currentRestaurant = latestRestaurantList.get(position);
                    Bitmap currentPic = downloadedPicList.get(position);
                    // use Gson to pass current Restaurant and picture to info page
                    String currentRestaurantInfo = new Gson().toJson(currentRestaurant, Restaurant.class);
                    String currentRestaurantPic = new Gson().toJson(currentPic, Bitmap.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("info", currentRestaurantInfo);
                    bundle.putString("pic", currentRestaurantPic);

                    // navigate to info page
                    navController.navigate(R.id.action_nav_homeFragment_to_infoPageFragment, bundle);
                }
            });
        }

        public ImageView getHome_latest_iv() {
            return home_latest_iv;
        }

        public TextView getHome_latest_tv() {
            return home_latest_tv;
        }

        public TextView getHome_latest_desc() {
            return home_latest_desc;
        }
    }
}
