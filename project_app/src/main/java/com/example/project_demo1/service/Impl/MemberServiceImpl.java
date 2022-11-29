package com.example.project_demo1.service.Impl;

import static com.example.project_demo1.network.ApiConfig.*;
import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

import android.util.Log;

import com.example.project_demo1.model.LoginResponse;
import com.example.project_demo1.utils.SharedPreferencesUtils;
import com.example.project_demo1.model.Member;
import com.example.project_demo1.network.Result;
import com.example.project_demo1.service.MemberService;
import com.example.project_demo1.utils.ResponseUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MemberServiceImpl implements MemberService {

    private static final String TAG = "MemberServiceImpl";

    private final SharedPreferencesUtils sharedPreferencesUtils;

    public MemberServiceImpl() {
        this(null);
    }

    public MemberServiceImpl(SharedPreferencesUtils sharedPreferencesUtils) {
        this.sharedPreferencesUtils = sharedPreferencesUtils;
    }

    /**
     * 登入(無token)
     *
     * @param email：登入信箱
     * @param password：登入密碼
     * @return Result，包含Success或Error
     */
    @Override
    public Result login(String email, String password) {

        String jsonRequestBody = new Gson().toJson(Map.of(
                "email", email,
                "password", password
        ));

        HttpURLConnection httpURLConnection = null;
        try {

            URL url = new URL(LOGIN.getUrl());
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty(CONTENT_TYPE, "application/json; charset=utf-8");
            httpURLConnection.setRequestProperty(ACCEPT, "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.getOutputStream().write(jsonRequestBody.getBytes(StandardCharsets.UTF_8));

            httpURLConnection.connect();

            String jsonResponseBody = ResponseUtils.streamToString(httpURLConnection.getInputStream());
            LoginResponse loginResponse = new Gson().fromJson(jsonResponseBody, LoginResponse.class);
            return new Result.Success<>(loginResponse);

        } catch (ConnectException e) {

            Log.e(TAG, "login: ", e);
            return new Result.Error(HttpURLConnection.HTTP_INTERNAL_ERROR, e.getMessage(), System.currentTimeMillis());

        } catch (IOException e) {

            String errorResponseBody = ResponseUtils.streamToString(httpURLConnection.getErrorStream());
            Result.Error error = new Gson().fromJson(errorResponseBody, Result.Error.class);
            return error;

        } finally {

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

        }
    }

    /**
     * 會員註冊
     *
     * @param newMember：新註冊會員
     * @return Result，包含Success或Error
     */
    @Override
    public Result registration(Member newMember) {

        String jsonRequestBody = new Gson().toJson(newMember);

        HttpURLConnection httpURLConnection = null;

        // -----
        try {

            URL apiUrl = new URL(REGISTRATION.getUrl());
            httpURLConnection = (HttpURLConnection) apiUrl.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty(CONTENT_TYPE, "application/json; charset=utf-8");
            httpURLConnection.setRequestProperty(ACCEPT, "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);

            // send the request body
            httpURLConnection.getOutputStream().write(jsonRequestBody.getBytes(StandardCharsets.UTF_8));

            // connect
            httpURLConnection.connect();

            String successResponse = ResponseUtils.streamToString(httpURLConnection.getInputStream());
            return new Result.Success<>(successResponse);

        } catch (IOException e) {

            String errorResponse = ResponseUtils.streamToString(httpURLConnection.getErrorStream());
            Result.Error error = new Gson().fromJson(errorResponse, Result.Error.class);
            return error;

        } finally {

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

        }
    }

    /**
     * 是否已登入
     *
     * 檢查shared preferences儲存的使用者資料，若有資料則視為已登入
     * @return true:已登入；false：未登入
     */
    @Override
    public boolean isLogin() {

        if(sharedPreferencesUtils == null) {
            throw new NullPointerException("Shared preferences is null. Please initialize the service with shared preferences injected.");
        }
        return !sharedPreferencesUtils.getAll().isEmpty();
    }

    /**
     * 會員登出
     *
     * jwt驗證機制的登出不需要去伺服器強制註銷（無狀態）
     * 刪除掉preferences就好
     */
    @Override
    public void logout() {
        sharedPreferencesUtils.clearSharedPreferences();
    }
}
