package com.example.project_demo1.ui.home;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_demo1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.base.Strings;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {

    private String address;
    private double longitude;
    private double latitude;


    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String address) {

        Bundle args = new Bundle();
        args.putString("address", address);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().size() != 0) {
            address = getArguments().getString("address");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Strings.isNullOrEmpty(address)) {
            Log.d("GoogleMap API", "onViewCreated: address is null, skip the map creating phase.");
            return;
        }

        // get geocoder: turn address to longitude and latitude
        Geocoder geocoder = new Geocoder(getContext(), Locale.CHINESE);
        try {
            List addressList = geocoder.getFromLocationName(address, 1);
            if(addressList.size() > 0) {
                Address addTmp = (Address) addressList.get(0);
                longitude = addTmp.getLongitude();
                latitude = addTmp.getLatitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create map
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                // Set the map coordinates to current restaurant.
                LatLng restLocation = new LatLng(latitude, longitude);
                // Set the map type to Normal.
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // Add a marker on the map coordinates.
                googleMap.addMarker(new MarkerOptions()
                        .position(restLocation)
                        .title("餐廳位置"));
                // Move the camera to the map coordinates and zoom in closer.
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restLocation, 15));
            }
        });
    }
}