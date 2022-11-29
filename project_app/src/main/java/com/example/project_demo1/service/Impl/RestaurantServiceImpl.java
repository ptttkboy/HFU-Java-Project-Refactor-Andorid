package com.example.project_demo1.service.Impl;

import static com.example.project_demo1.network.ApiConfig.GET_LATEST;
import static com.example.project_demo1.network.ApiConfig.GET_RESTAURANTS;
import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

import com.example.project_demo1.network.Result;
import com.example.project_demo1.model.Restaurant;
import com.example.project_demo1.service.RestaurantService;
import com.example.project_demo1.utils.QueryStringBuilder;
import com.example.project_demo1.utils.ResponseUtils;
import com.example.project_demo1.utils.SharedPreferencesUtils;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestaurantServiceImpl implements RestaurantService {

    private static final String TAG = "RestaurantService";

    private final SharedPreferencesUtils sharedPreferencesUtils;

    public RestaurantServiceImpl(SharedPreferencesUtils sharedPreferencesUtils) {
        this.sharedPreferencesUtils = sharedPreferencesUtils;
    }

    /**
     * 取得所有餐廳
     *
     * @return
     */
    @Override
    public Result getAllRestaurants() {
        return null;
    }
    /*
    @Override
    public Result getAllRestaurants() {

        String api = baseUrl + "/api/v1/restaurants";
        HttpURLConnection httpURLConnection = null;

        // -----------
        // -----------

        try {
            Log.d(TAG, "getRestaurants: Connecting to server...");
            httpURLConnection = httpConnection.doGetRequest(api);
            String response = ResponseUtils.streamToString(httpURLConnection.getInputStream());
            Type listOfRestaurant = new TypeToken<ArrayList<Restaurant>>() {}.getType();
            List<Restaurant> restaurants = new Gson().fromJson(response, listOfRestaurant);
            return new Result.Success<>(restaurants);

        } catch (FileNotFoundException e) {
            // 404，接收伺服器錯誤訊息。
            String errorResponse = ResponseUtils.streamToString(httpURLConnection.getErrorStream());
            Result.Error error = new Gson().fromJson(errorResponse, Result.Error.class);
            return error;

        } catch (IOException e) {
            // 500 ，連不上伺服器（網路問題或是伺服器關閉）。
            return new Result.Error(HTTP_INTERNAL_ERROR, e.getMessage(), System.currentTimeMillis());
        } finally {
            if(httpURLConnection != null) {
              httpURLConnection.disconnect();
            }
        }
    }
     */

    /**
     * 取得最新餐廳資料
     *
     * @return Result:回傳結果，成功回傳Result.Success，失敗回傳Result.Error
     */
    @Override
    public Result getLatest() {

        HttpURLConnection httpURLConnection = null;

        try {

            URL url = new URL(GET_LATEST.getUrl());
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty(CONTENT_TYPE, "application/json");
            httpURLConnection.setRequestProperty(ACCEPT, "application/json");
            httpURLConnection.setDoInput(true); // allow response stream
            httpURLConnection.setInstanceFollowRedirects(false);

            // connect
            httpURLConnection.connect();

            // 取得回傳
            String successResponse = CharStreams.toString(new InputStreamReader(new BufferedInputStream(httpURLConnection.getInputStream()), Charsets.UTF_8));
            Type listOfRestaurant = new TypeToken<ArrayList<Restaurant>>() {}.getType();
            List<Restaurant> restaurants = new Gson().fromJson(successResponse, listOfRestaurant);
            return new Result.Success<>(restaurants);

        } catch (IOException e) {

            String errorResponse = ResponseUtils.streamToString(httpURLConnection.getErrorStream());
            Result.Error error = new Gson().fromJson(errorResponse, Result.Error.class);
            return error;

        } finally {

            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    /**
     * 搜尋餐廳
     *
     * 搜尋餐廳功能需要登入後，持伺服器發送的token才可使用
     *
     * @param name：餐廳名稱
     * @param category：餐廳類別
     * @return Result:回傳結果，成功回傳Result.Success，失敗回傳Result.Error
     */
    @Override
    public Result getRestaurantsByNameOrCategory(String name, String category) {

        // get token from sp
        String accessToken = sharedPreferencesUtils.getAccessToken();

        HttpURLConnection httpURLConnection = null;

        try {
            // 用傳進來的參數建立queryString
            String queryString = new QueryStringBuilder()
                    .addParam("name", name)
                    .addParam("category", category)
                    .create();

            URL url = new URL(GET_RESTAURANTS.getUrl() + queryString);

            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty(AUTHORIZATION, "Bearer " + accessToken);
            httpURLConnection.setRequestProperty(CONTENT_TYPE, "application/json");
            httpURLConnection.setRequestProperty(ACCEPT, "application/json");
            httpURLConnection.setDoInput(true); // allow response stream
            httpURLConnection.setInstanceFollowRedirects(false);

            // connect
            httpURLConnection.connect();

            // 取得回傳值
            String successResponse = ResponseUtils.streamToString(httpURLConnection.getInputStream());
            Type listOfRestaurant = new TypeToken<ArrayList<Restaurant>>() {}.getType();
            List<Restaurant> restaurants = new Gson().fromJson(successResponse, listOfRestaurant);
            return new Result.Success<>(restaurants);

        } catch (FileNotFoundException e) {

            String errorResponse = ResponseUtils.streamToString(httpURLConnection.getErrorStream());
            Result.Error error = new Gson().fromJson(errorResponse, Result.Error.class);
            return error;

        } catch (IOException e) {

            return new Result.Error(HTTP_INTERNAL_ERROR, e.getMessage(), System.currentTimeMillis());

        } finally {

            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

        }
    }
}
