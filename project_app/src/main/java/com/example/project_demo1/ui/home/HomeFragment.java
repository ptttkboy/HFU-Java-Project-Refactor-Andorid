package com.example.project_demo1.ui.home;

import static android.view.View.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.project_demo1.MyApplication;
import com.example.project_demo1.R;
import com.example.project_demo1.adapter.SliderAdapter;
import com.example.project_demo1.network.Result;
import com.example.project_demo1.model.Restaurant;
import com.example.project_demo1.service.Impl.RestaurantServiceImpl;
import com.example.project_demo1.service.RestaurantService;
import com.example.project_demo1.utils.PictureUtils;
import com.example.project_demo1.utils.SharedPreferencesUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 首頁
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private Executor executor;
    private RestaurantService restaurantService;

    // views
    private NavController navController;
    private SearchView searchView;
    private ProgressBar home_loading_pb;
    private ViewPager2 home_viewpager_latest;
    private CardView home_chinese, home_japanese, home_french, home_american, home_innovation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(getActivity().getPreferences(Context.MODE_PRIVATE));
        executor = MyApplication.getExecutorService();
        restaurantService = new RestaurantServiceImpl(sharedPreferencesUtils);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // 導覽控制器
        navController = Navigation.findNavController(view);
        // SearchView
        // 在第一次啟動app時為null（因為Activity在調用onCreate時會先建立此fragment）
        // 啟動成功後，search view將不為null
        // 只要進到此頁的search view就必須為可見
        searchView = getActivity().findViewById(R.id.search_view);
        if (searchView != null && searchView.getVisibility() == INVISIBLE) {
            searchView.setVisibility(VISIBLE);
        }

        // 新商店：進度條，幻燈片
        home_loading_pb = view.findViewById(R.id.home_loading_pb);
        home_viewpager_latest = view.findViewById(R.id.home_viewpager_latest);
        // 取得最新餐廳
        showLatestRestaurants();

        // 依類型快速搜尋按鈕card views及點擊事件
        home_chinese = view.findViewById(R.id.home_chinese);
        home_japanese = view.findViewById(R.id.home_japanese);
        home_french = view.findViewById(R.id.home_french);
        home_american = view.findViewById(R.id.home_american);
        home_innovation = view.findViewById(R.id.home_innovation);

        home_chinese.setOnClickListener(this::onClickCategory);
        home_japanese.setOnClickListener(this::onClickCategory);
        home_french.setOnClickListener(this::onClickCategory);
        home_american.setOnClickListener(this::onClickCategory);
        home_innovation.setOnClickListener(this::onClickCategory);
    }

    /**
     * 依類型快速搜尋按鈕點擊事件
     *
     * 點擊後，將對應之搜尋關鍵字以bundle的形式傳入搜尋結果頁面（searchResultFragment）。
     * @param v：點擊的按鈕
     */
    private void onClickCategory(View v) {

        Bundle bundle = new Bundle();

        if(v.equals(home_chinese)) {
            bundle.putString("categoryQuery", "中式料理");
        }else if(v.equals(home_japanese)) {
            bundle.putString("categoryQuery", "日式料理");
        }else if(v.equals(home_french)) {
            bundle.putString("categoryQuery", "法式料理");
        }else if(v.equals(home_american)) {
            bundle.putString("categoryQuery", "美式料理");
        }else if(v.equals(home_innovation)) {
            bundle.putString("categoryQuery", "創新料理");
        }
        navController.navigate(R.id.action_nav_homeFragment_to_searchResultFragment, bundle);
    }

    /**
     * 取得最新餐廳資訊。
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showLatestRestaurants() {
        // 啟動連網執行緒
        executor.execute(() -> {

            // 獲取結果物件Result
            Result getLatest = restaurantService.getLatest();

            // 成功：取得餐廳，下載圖片，渲染ui
            if(getLatest.isSuccess){
                List<Restaurant> latestRestaurants = ((Result.Success<List<Restaurant>>) getLatest).getData();
                List<Bitmap> images = latestRestaurants.stream()
                        .map(restaurant -> PictureUtils.downloadImageByUrl(restaurant.getImageUrl()))
                        .collect(Collectors.toList());

                getActivity().runOnUiThread(() -> {
                    // hide progress bar
                    home_loading_pb.setVisibility(View.GONE);

                    // inflate view pager
                    home_viewpager_latest.setAdapter(new SliderAdapter(latestRestaurants, navController, images));
                });

            } else {
                // 失敗：控台顯示錯誤。
                Result.Error error = (Result.Error) getLatest;
                Log.e(TAG, "onViewCreated: " + error.getMessage() + ", status: " + error.getStatus());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(getActivity().getPreferences(Context.MODE_PRIVATE));
        Log.e(TAG, "onCreate: isLOGIN?" + !sharedPreferencesUtils.getAll().isEmpty());
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(getActivity().getPreferences(Context.MODE_PRIVATE));
        Log.e(TAG, "onCreate: isLOGIN? " + !sharedPreferencesUtils.getAll().isEmpty());
    }
}