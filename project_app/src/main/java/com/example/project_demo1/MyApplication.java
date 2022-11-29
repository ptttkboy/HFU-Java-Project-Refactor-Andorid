package com.example.project_demo1;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    private static SharedPreferences prefs;
    private static ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        // prefs = getSharedPreferences("my_key", Context.MODE_PRIVATE);
        executorService = Executors.newSingleThreadExecutor();
    }

    public static SharedPreferences getPrefs() {
        return prefs;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }
}
