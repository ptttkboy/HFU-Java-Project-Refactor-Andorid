package com.example.project_demo1.service;

import com.example.project_demo1.network.Result;

public interface RestaurantService {

    Result getAllRestaurants();
    Result getLatest();
    Result getRestaurantsByNameOrCategory(String name, String category);
}
