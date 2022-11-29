package com.example.project_demo1.ui.home;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_demo1.R;
import com.example.project_demo1.model.Restaurant;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

public class InfoPageFragment extends Fragment {
    
    private static final String TAG ="InfoPageFragment";
    private SearchView searchView;
    private ImageView appbar_image_header;

    public InfoPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // hide search view
        searchView = getActivity().findViewById(R.id.search_view);
        searchView.setVisibility(View.INVISIBLE);

        // fetch data from bundle args
        String restaurantInfoJson = getArguments().getString("info");
        String restaurantPicJson = getArguments().getString("pic");

        // parse json
        Restaurant restaurantInfo = new Gson().fromJson(restaurantInfoJson, Restaurant.class);
        Bitmap restaurantPic = new Gson().fromJson(restaurantPicJson, Bitmap.class);

        // set detail
        ((TextView)view.findViewById(R.id.info_name)).setText(restaurantInfo.getName());
        ((TextView)view.findViewById(R.id.info_address)).setText(restaurantInfo.getAddress());
        ((TextView)view.findViewById(R.id.info_phone)).setText(restaurantInfo.getPhone());
        ((TextView)view.findViewById(R.id.info_desc)).setText(restaurantInfo.getDescription());

        // set banner image (internet work)
        appbar_image_header = getActivity().findViewById(R.id.appbar_image_header);
        appbar_image_header.setImageBitmap(restaurantPic);

        /*------ google maps------*/
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.map_container, MapFragment.newInstance(restaurantInfo.getAddress()))
                .commit();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        AppBarLayout appbar_layout = getActivity().findViewById(R.id.appbar_layout);
        appbar_layout.setExpanded(false);
        appbar_image_header.setImageDrawable(null);
    }
}