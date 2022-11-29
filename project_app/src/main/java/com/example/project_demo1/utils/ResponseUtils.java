package com.example.project_demo1.utils;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResponseUtils {

    public static String streamToString(InputStream inputStream) {
        try {
            return CharStreams.toString(new InputStreamReader(new BufferedInputStream(inputStream), Charsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
