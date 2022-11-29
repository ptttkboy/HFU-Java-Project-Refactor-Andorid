package com.example.project_demo1;

import android.graphics.Bitmap;

import com.example.project_demo1.utils.PictureUtils;

import org.junit.Test;

public class ConnectionTest {

//    @Test
//    public void getLatest() {
//        RestaurantService restaurantService = new RestaurantServiceImpl(new HttpConnectionImpl());
//        List<Restaurant> latest = restaurantService.getLatest();
//        System.out.println(latest);
//    }

    @Test
    public void downloadImg() {
        PictureUtils pictureUtils = new PictureUtils();
        Bitmap bitmap = pictureUtils.downloadImageByUrl("https://i.imgur.com/FZxJmDu.jpeg");
    }
}
