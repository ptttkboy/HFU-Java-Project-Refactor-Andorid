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

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private List<Restaurant> restaurantList;
    private NavController navController;
    private List<Bitmap> downloadedPicList;

    // constructor
    public SearchResultAdapter() {
    }

    public SearchResultAdapter(List<Restaurant> restaurantList, List<Bitmap> downloadedPicList, NavController navController) {
        // input data
        this.restaurantList = restaurantList;
        this.downloadedPicList = downloadedPicList;
        this.navController = navController;
    }

    // view holder
    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {

        private ImageView pic_iv;
        private TextView title_tv;
        private TextView desc_tv;

        public SearchResultViewHolder(@NonNull View itemView, List<Restaurant> dataList, List<Bitmap> downloadedPicList, NavController navController) {
            super(itemView);
            pic_iv = itemView.findViewById(R.id.search_result_list_item_picture);
            title_tv = itemView.findViewById(R.id.search_result_list_item_title);
            desc_tv = itemView.findViewById(R.id.search_result_list_item_description);
            itemView.setOnClickListener(v -> {
                // get current position
                int position = getAdapterPosition();

                // pass the current data to next fragment via bundle
                Restaurant currentRestaurant = dataList.get(position);
                Bitmap currentPic = downloadedPicList.get(position);

                //TODO: pass Bitmap to next fragment

                // use Gson to pass current Restaurant and picture to info page
                String currentRestaurantInfo = new Gson().toJson(currentRestaurant, Restaurant.class);
                String currentRestaurantPic = new Gson().toJson(currentPic, Bitmap.class);

                Bundle bundle = new Bundle();
                bundle.putString("info", currentRestaurantInfo);
                bundle.putString("pic", currentRestaurantPic);

                // navigate to info page
                navController.navigate(R.id.action_searchResultFragment_to_infoPageFragment, bundle);
                // test show toast
                // Toast.makeText(context, "This is at item: " + position + ", and store name is: " + rName, Toast.LENGTH_SHORT).show();
            });
        }

        public ImageView getPic_iv() {
            return pic_iv;
        }

        public TextView getTitle_tv() {
            return title_tv;
        }

        public TextView getDesc_tv() {
            return desc_tv;
        }
    }

    // overrides
    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_list_item, parent, false);
        return new SearchResultViewHolder(view, restaurantList, downloadedPicList, navController);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {

        holder.getPic_iv().setImageBitmap(downloadedPicList.get(position));
        holder.getTitle_tv().setText(restaurantList.get(position).getName());
        holder.getDesc_tv().setText(restaurantList.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}
