package com.example.project_demo1;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_demo1.model.Member;
import com.example.project_demo1.service.Impl.MemberServiceImpl;
import com.example.project_demo1.service.MemberService;
import com.example.project_demo1.utils.PictureUtils;
import com.example.project_demo1.utils.SharedPreferencesUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    private Executor executor;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private MemberService memberService;

    // views
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executor = MyApplication.getExecutorService();
        // 使用該Activity的專屬唯一sharedPreferences取得工具類。
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(this.getPreferences(MODE_PRIVATE));
        memberService = new MemberServiceImpl(sharedPreferencesUtils);


        /* ----- views 初始化及設定----- */
        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 拖曳式導覽列設定
        // 綁定導覽列（navigation view）以及Jetpack提供的導覽控制器（navController）
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navigationView, navController);

        // 具拖曳功能的版面
        DrawerLayout drawer_layout = findViewById(R.id.drawer_layout);

        // 將拖曳版面跟導覽列結合
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_homeFragment, R.id.nav_loginFragment, R.id.nav_myProfileFragment, R.id.nav_settingsFragment, R.id.nav_logoutDialogFragment)
                .setOpenableLayout(drawer_layout)
                .build();

        // setup actionbar with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // 設定搜尋欄
        // get SearchView
        SearchView searchView = findViewById(R.id.search_view);

        // set submit Listener: navigate and pass the value
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String nameQuery) {

                NavDestination navDestination = navController.getCurrentDestination();
                Bundle bundle = new Bundle();
                bundle.putString("nameQuery", nameQuery.trim());

                // navigation:
                //      if in home page, home -> result
                //      if in result, result -> result (pop the backstack!)
                if(navDestination.getId() == R.id.nav_homeFragment) {

                    navController.navigate(R.id.action_nav_homeFragment_to_searchResultFragment, bundle);
                    return false;
                }

                navController.navigate(R.id.action_searchResultFragment_self, bundle);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // 如果有登入就更新導覽列ui
        if(memberService.isLogin()) {
            updateLoggedInUi();
        }
    }


    // Jetpack Navigation Component: hamburger and back arrow button
    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * 已登入使用者的UI
     */
    public void updateLoggedInUi(){

        Member loggedInMember = sharedPreferencesUtils.getMember();

        executor.execute(() -> {
            // 下載圖片並更新UI
            Bitmap loggedMemberImage = PictureUtils.downloadImageByUrl(loggedInMember.getImageUrl());

            // 更新ui
            runOnUiThread(() -> {
                // 更新頭像和歡迎字體
                View navHeader = navigationView.getHeaderView(0);
                ImageView nav_header_picture =  navHeader.findViewById(R.id.nav_header_picture);
                TextView nav_header_text = navHeader.findViewById(R.id.nav_header_text);

                nav_header_text.setText("您好，" + loggedInMember.getName());
                nav_header_picture.setImageBitmap(loggedMemberImage);
                // 隱藏登入選項
                navigationView.getMenu().findItem(R.id.nav_loginFragment).setVisible(false);
            });
        });
    }
}