package com.example.project_demo1.utils;

import com.google.common.base.Strings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class QueryStringBuilder {

    private StringBuilder stringBuilder = new StringBuilder();

    public QueryStringBuilder addParam(String param, String value) throws UnsupportedEncodingException {
        String trimmedValue = Strings.nullToEmpty(value);
        String encoded = URLEncoder.encode(trimmedValue, StandardCharsets.UTF_8.name());

        if(stringBuilder.length() == 0) {
            stringBuilder.append("?" + param + "=" + encoded);
            return this;
        }
        stringBuilder.append("&" + param + "=" + encoded);
        return this;
    }

    public String create() {
        return stringBuilder.toString();
    }
}
