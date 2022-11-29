package com.example.project_demo1.ui.myprofile;

import static android.content.Context.*;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.project_demo1.MyApplication;
import com.example.project_demo1.R;
import com.example.project_demo1.model.Member;
import com.example.project_demo1.service.Impl.MemberServiceImpl;
import com.example.project_demo1.service.MemberService;
import com.example.project_demo1.utils.PictureUtils;
import com.example.project_demo1.utils.SharedPreferencesUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.Executor;

public class MyProfileFragment extends Fragment {

    private static final String TAG = "MyProfileFragment";
    private NavController navController;
    private Executor executor;
    private MemberService memberService;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executor = MyApplication.getExecutorService();
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(getActivity().getPreferences(MODE_PRIVATE));
        memberService = new MemberServiceImpl(sharedPreferencesUtils);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        /* ----- views ----- */
        // navController
        navController = Navigation.findNavController(view);
        // hide SearchView if visible
        SearchView searchView = getActivity().findViewById(R.id.search_view);
        if(searchView.getVisibility() == View.VISIBLE) {
            searchView.setVisibility(View.INVISIBLE);
        }
        TextView tv_profile_name = view.findViewById(R.id.tv_profile_name);
        TextView tv_profile_account = view.findViewById(R.id.tv_profile_account);
        ImageView iv_profile_picture = view.findViewById(R.id.iv_profile_picture);
        Button logout_btn = view.findViewById(R.id.logout_btn);

        /* ----- 顯示已登入會員資料 ----- */
        // 取得已登入會員
        Member loggedInMember = sharedPreferencesUtils.getMember();

        // 取得名字和信箱
        tv_profile_name.setText(loggedInMember.getName());
        tv_profile_account.setText(loggedInMember.getEmail());

        // 下載圖片
        executor.execute(()-> {
            Bitmap image = PictureUtils.downloadImageByUrl(loggedInMember.getImageUrl());

            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    iv_profile_picture.setImageBitmap(image);
                });
            }
        });
        // 登出按鈕
        logout_btn.setOnClickListener(this::confirmLogout);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 取得logout dialog拋回來的確認訊息
        Bundle fromLogoutDialog = this.getArguments();

        if(fromLogoutDialog == null) {
            return;
        }

        boolean confirmLogoutResult = fromLogoutDialog.getBoolean("logout");
        if(confirmLogoutResult) {
            startLogout();
        }
    }

    /**
     * 登出按鈕
     * 導向確定登出的dialog？
     * @param logoutButton
     */
    private void confirmLogout(View logoutButton) {
        navController.navigate(R.id.action_nav_myProfileFragment_to_logoutDialogFragment);
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

        // 顯示已登出
        Toast.makeText(getActivity(), "已登出", Toast.LENGTH_SHORT).show();
        // 導回首頁
        navController.popBackStack();
        navController.navigateUp();
    }

}