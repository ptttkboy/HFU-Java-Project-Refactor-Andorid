package com.example.project_demo1.utils;

import android.content.SharedPreferences;

import com.example.project_demo1.MainActivity;
import com.example.project_demo1.model.Member;

import java.util.Map;

/**
 * SharedPreferences的工具類別
 *
 * 該工具類別仰賴MainActivity提供的shared preferences不得為null
 * 且關乎到ＩＯ操作 -> 採用singleton
 * static方法不可用（一定要有Main preferences才可以用，具有state）
 * singleton可行？
 *
 * 需要時才建立--> lazy mode
 *
 */
public class SharedPreferencesUtils {

    private static final String MEMBER_NAME = "MEMBER_NAME";
    private static final String MEMBER_EMAIL = "MEMBER_EMAIL";
    private static final String MEMBER_PHONE = "MEMBER_PHONE";
    private static final String MEMBER_IMAGE_URL = "MEMBER_IMAGE_URL";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    // 使用volatile來避免多執行緒建立時，singleton因可見性問題而失效。
    private volatile static SharedPreferencesUtils instance;

    private final SharedPreferences sharedPreferences;

    private SharedPreferencesUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setMemberInfo(Member member,
                              String accessToken,
                              String refreshToken){

        sharedPreferences.edit()
                .putString(MEMBER_NAME, member.getName())
                .putString(MEMBER_EMAIL, member.getEmail())
                .putString(MEMBER_PHONE, member.getPhone())
                .putString(MEMBER_IMAGE_URL, member.getImageUrl())
                .putString(ACCESS_TOKEN, accessToken)
                .putString(REFRESH_TOKEN, refreshToken)
                .commit(); // 使用commit（立即寫入）來確保該操作是執行緒安全
    }

    public Member getMember() {
        return new Member(
                sharedPreferences.getString(MEMBER_NAME, null),
                sharedPreferences.getString(MEMBER_EMAIL, null),
                null,
                sharedPreferences.getString(MEMBER_PHONE, null),
                sharedPreferences.getString(MEMBER_IMAGE_URL, null));
    }

    public String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(REFRESH_TOKEN, null);
    }

    public void clearSharedPreferences() {
        sharedPreferences.edit().clear().commit();
    }

    public Map getAll(){
        return sharedPreferences.getAll();
    }

    // 使用singleton的DCL建立模式確保該utils類只會產生一次
    public static SharedPreferencesUtils getInstance(SharedPreferences sharedPreferences) {

        if(sharedPreferences == null) {
            throw new RuntimeException("SharedPreference cannot be null.");
        }

        if(instance == null) {
            synchronized (SharedPreferencesUtils.class) {
                if(instance == null) {
                    instance = new SharedPreferencesUtils(sharedPreferences);
                }
            }
        }
        return instance;
    }
}
