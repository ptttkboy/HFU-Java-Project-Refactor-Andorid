package com.example.project_demo1.ui.home;

import static android.view.View.*;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.project_demo1.MyApplication;
import com.example.project_demo1.R;
import com.example.project_demo1.adapter.SearchResultAdapter;
import com.example.project_demo1.model.Restaurant;
import com.example.project_demo1.network.Result;
import com.example.project_demo1.service.Impl.MemberServiceImpl;
import com.example.project_demo1.service.Impl.RestaurantServiceImpl;
import com.example.project_demo1.service.MemberService;
import com.example.project_demo1.service.RestaurantService;
import com.example.project_demo1.utils.PictureUtils;
import com.example.project_demo1.utils.SharedPreferencesUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 搜尋結果頁面
 */
public class SearchResultFragment extends Fragment {

    private static final String TAG = "SearchResultFragment";
    private Executor executor;
    private RestaurantService restaurantService;
    private MemberService memberService;

    // views
    private NavController navController;
    private SearchView searchView;
    private TextView search_result_notFound;
    private RecyclerView search_result_recyclerview;
    private ProgressBar search_result_progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesUtils sharedPreferencesUtils = SharedPreferencesUtils.getInstance(getActivity().getPreferences(Context.MODE_PRIVATE));
        restaurantService = new RestaurantServiceImpl(sharedPreferencesUtils);
        memberService = new MemberServiceImpl(sharedPreferencesUtils);
        executor = MyApplication.getExecutorService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* ----- 設定 views ----- */
        navController = Navigation.findNavController(view);
        searchView = getActivity().findViewById(R.id.search_view);
        // 顯示搜尋列
        searchView.setVisibility(View.VISIBLE);

        search_result_notFound = view.findViewById(R.id.search_result_notFound);
        search_result_recyclerview = view.findViewById(R.id.search_result_recyclerview);
        search_result_progressBar = view.findViewById(R.id.search_result_progressBar);

        /* ----- 依名稱搜尋餐廳 ----- */
        // 取得搜尋欄的輸入值query並轉成json
        String nameQuery = getArguments().getString("nameQuery");
        String categoryQuery = getArguments().getString("categoryQuery");

        // 開始搜尋
        startSearch(nameQuery, categoryQuery);
    }

    // 連線伺服器搜尋
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startSearch(String nameQuery, String categoryQuery) {
        executor.execute(() -> {

            Result result = restaurantService.getRestaurantsByNameOrCategory(nameQuery, categoryQuery);

            // 成功
            if(result.isSuccess) {
                List<Restaurant> restaurants = ((Result.Success<List<Restaurant>>) result).getData();

                // 成功，有搜到東西
                if(!restaurants.isEmpty()) {
                    List<Bitmap> images = restaurants.stream()
                            .map(restaurant -> PictureUtils.downloadImageByUrl(restaurant.getImageUrl()))
                            .collect(Collectors.toList());

                    // render UI: 隱藏轉圈圈、顯示出recycler view結果列表
                    getActivity().runOnUiThread(() -> {
                        search_result_progressBar.setVisibility(GONE);
                        search_result_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                        search_result_recyclerview.setAdapter(new SearchResultAdapter(restaurants, images, navController));
                    });

                } else {
                    // 成功，但結果為空：顯示找不到資料
                    getActivity().runOnUiThread(() -> {
                        search_result_progressBar.setVisibility(GONE);
                        search_result_notFound.setVisibility(VISIBLE);
                        search_result_recyclerview.setVisibility(INVISIBLE);
                    });
                }
            } else {
                // 失敗：顯示錯誤訊息
                Result.Error error = (Result.Error) result;

                getActivity().runOnUiThread(() -> {

                    switch (error.getStatus()) {
                        // 403: 要求先登入
                        case HTTP_FORBIDDEN :
                            Toast.makeText(getActivity(), "請先登入", Toast.LENGTH_SHORT).show();
                            break;
                        // 401: 要求先登入
                        case HTTP_UNAUTHORIZED :
                            Toast.makeText(getActivity(), "請先登入", Toast.LENGTH_SHORT).show();
                            break;
                        // token過期，要求重新登入
                        case 498 :

                            // clear out login info
                            startLogout();
                            Toast.makeText(getActivity(), "已過期，請重新登入", Toast.LENGTH_SHORT).show();
                            navController.popBackStack();
                            navController.navigateUp();
                            break;
                        default:
                            // 其他錯誤
                            Toast.makeText(getActivity(), "錯誤：" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onViewCreated: " + error);
                    }
                });
            }
        });
    }

    /**
     * 執行登出
     */
    private void startLogout() {

        memberService.logout();

        // 清除nav ui
        // clear Navigation header
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);

        ImageView nav_header_picture = navHeader.findViewById(R.id.nav_header_picture);
        TextView nav_header_text = navHeader.findViewById(R.id.nav_header_text);
        ((TextView) getActivity().findViewById(R.id.nav_header_text)).setText("尚未登入");
        ((ImageView) getActivity().findViewById(R.id.nav_header_picture)).setImageResource(R.drawable.ic_launcher_foreground);

        //
        // navigationView.getMenu().findItem(R.id.nav_logoutDialogFragment).setVisible(false);

        // 重新在導覽列顯示登入選項，隱藏個人頁面
        navigationView.getMenu().findItem(R.id.nav_loginFragment).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_myProfileFragment).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_logoutDialogFragment).setVisible(false);

        // 顯示已登出
        Toast.makeText(getActivity(), "已登出", Toast.LENGTH_SHORT).show();
        // 導回首頁
        navController.popBackStack();
        navController.navigateUp();
    }
}