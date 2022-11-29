package com.example.project_demo1.ui.login;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_demo1.MyApplication;
import com.example.project_demo1.R;
import com.example.project_demo1.model.LoginResponse;
import com.example.project_demo1.model.Member;
import com.example.project_demo1.network.Result;
import com.example.project_demo1.service.Impl.MemberServiceImpl;
import com.example.project_demo1.service.MemberService;
import com.example.project_demo1.utils.PictureUtils;
import com.example.project_demo1.utils.SharedPreferencesUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.common.base.Strings;

import java.net.HttpURLConnection;
import java.util.concurrent.Executor;

/**
 * 登入頁面
 */
public class LoginFragment extends Fragment{

    private static final String TAG = "LoginFragment";

    // executor service from Activity
    private Executor executor;
    private MemberService memberService;
    private SharedPreferencesUtils sharedPreferencesUtils;

    private Activity activityHolder;

    // views
    private NavController navController;
    private SearchView search_view;
    private Button login_btn, registration_btn;
    private EditText input_email, input_password;
    private TextView login_err_msg;

    public LoginFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activityHolder = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        executor = MyApplication.getExecutorService();
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(getActivity().getPreferences(Context.MODE_PRIVATE));
        memberService = new MemberServiceImpl(sharedPreferencesUtils);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* ----- 設定views -----*/
        navController = Navigation.findNavController(view);
        search_view = getActivity().findViewById(R.id.search_view);
        // 隱藏搜尋列
        if(search_view.getVisibility() == View.VISIBLE) {
            search_view.setVisibility(View.INVISIBLE);
        }

        // 輸入欄位和提示訊息
        input_email = view.findViewById(R.id.et_login_email);
        input_password = view.findViewById(R.id.et_login_password);
        login_err_msg = view.findViewById(R.id.login_err_msg);

        // 登入按鈕
        login_btn = view.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this::startLogin);

        // 註冊按鈕
        registration_btn = view.findViewById(R.id.registration_btn);
        registration_btn.setOnClickListener(this::startRegistration);
    }

    /**
     * 登入按鈕事件
     *
     * @param loginButton
     */
    private void startLogin(View loginButton) {
        // 取得輸入欄位資料
        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();

        // 檢查輸入
        if(Strings.isNullOrEmpty(email) || Strings.isNullOrEmpty(password)) {
            login_err_msg.setVisibility(View.VISIBLE);
            login_err_msg.setText("輸入的帳號或密碼不得為空");
            login_err_msg.setTextColor(getResources().getColor(R.color.text_warning_red));
            return;
        }

        // 網路連線
        executor.execute(() -> {
            Result loginResult = memberService.login(email, password);

            // 成功
            if (loginResult.isSuccess) {
                // 取得登入會員資料
                LoginResponse data = ((Result.Success<LoginResponse>) loginResult).getData();
                Member loginMember = data.getMember();
                String accessToken = data.getAccess_token();
                String refreshToken = data.getRefresh_token();

                // 將資訊存到preferences
                sharedPreferencesUtils.setMemberInfo(loginMember, accessToken, refreshToken);

                // 下載圖片
                Bitmap memberImage = PictureUtils.downloadImageByUrl(loginMember.getImageUrl());
                // 更新ui，跳轉到個人頁面
                if (getActivity() != null){
                    getActivity().runOnUiThread(() -> {
                        // 更新導覽列
                        this.updateNavUi(loginMember.getName(), memberImage);
                        // 顯示登入成功並跳轉
                        Toast.makeText(getActivity(), "登入成功", Toast.LENGTH_SHORT).show();
                        navController.navigateUp();
                    });
                }

            } else {
                // 失敗：
                Result.Error error = (Result.Error) loginResult;
                //更新Ui
                getActivity().runOnUiThread(() -> {

                    if (error.getStatus() == HTTP_FORBIDDEN) {
                        login_err_msg.setVisibility(View.VISIBLE);
                        login_err_msg.setText("帳號或密碼錯誤，錯誤訊息：" + error.getMessage());
                        login_err_msg.setTextColor(getResources().getColor(R.color.text_warning_red));

                    } else {
                        // 其他：伺服器錯誤
                        login_err_msg.setVisibility(View.VISIBLE);
                        login_err_msg.setText("登入失敗，錯誤訊息：" + error.getMessage());
                        login_err_msg.setTextColor(getResources().getColor(R.color.text_warning_red));
                        Log.e(TAG, "makeLoginRequest: " + error);
                    }
                });
            }
        });
    }

    /**
     * 註冊按鈕事件
     *
     * @param registrationButton
     */
    protected void startRegistration(View registrationButton) {
        // 導覽到註冊頁面
        navController.navigate(R.id.action_nav_loginFragment_to_registrationInfoFragment);
    }

    /**
     * 導覽列UI更新
     *
     * @param memberName
     * @param memberImage
     */
    protected void updateNavUi(String memberName, Bitmap memberImage) {

        // header設定：您好，使用者名稱
        TextView nav_header_text = getActivity().findViewById(R.id.nav_header_text);
        nav_header_text.setText("您好，" + memberName);

        // header圖像設定
        ImageView nav_header_picture = getActivity().findViewById(R.id.nav_header_picture);
        if (memberImage != null ) nav_header_picture.setImageBitmap(memberImage);

        // 導覽列顯示項目設定：隱藏登入選項，顯示個人頁面
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_logoutDialogFragment).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_loginFragment).setVisible(false);
    }
}