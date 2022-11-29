package com.example.project_demo1.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.example.project_demo1.exception.ImageDownloadException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PictureUtils {

    /**
     * Covert base64 String into Bitmap.
     *
     * @param strBase64
     * @return decoded bitmap.
     */
    public Bitmap base64ToBitmap(String strBase64) {

        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return decodedByte;
    }

    /**
     * Compress the image from gallery
     *
     * @param contentUri
     * @param context
     * @return
     * @throws IOException
     */
    public String compressContentUri(Uri contentUri, Context context) throws IOException {


        // load content uri and convert to binary type
        // Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentUri);

        BitmapFactory.Options options = new BitmapFactory.Options();

        // ex: 1024dp * 1024 dp * 2 bytes (ARGB4444) = 2M
        // required = 150 dp * 150 dp * 2 bytes = 43 kb < 65kb (BLOB)
        // BLOB = 65 kb, 65 * 0.9 for safety = 58KB
        // if image w * h * 2(ARGB4444) * sample size < = 65 * 0.9
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inJustDecodeBounds = true;


        InputStream decodeBoundIs =  context.getContentResolver().openInputStream(contentUri);
        BitmapFactory.decodeStream(decodeBoundIs, null, options);
        decodeBoundIs.close();
        /* -- compress test
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        int sampleSize = 1;
        int BlobSizeWithBuffer = 60 * 1024;
        while (srcWidth * srcHeight * 2 * (1/sampleSize^2) > BlobSizeWithBuffer) {
            sampleSize *= 2;
        }

         */
        int sampleSize = 8;
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;

        InputStream compressIs = context.getContentResolver().openInputStream(contentUri);
        Bitmap bitmap = BitmapFactory.decodeStream(compressIs, null, options);
        compressIs.close();

        System.out.println("Picture byte count: " + bitmap.getByteCount());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        String encodedStr = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);

        return encodedStr;
        //String encoded = Base64.encodeToString(src, Base64.NO_WRAP);
    }

    /**
     * 下載圖片
     * @param src
     * @return
     */
    public static Bitmap downloadImageByUrl(String src) {

        if (src == null || src.isEmpty()) {
            return null;
        }

        HttpURLConnection imgConn = null;
        try {
            URL srcUrl = new URL(src);
            imgConn = (HttpURLConnection)srcUrl.openConnection();
            imgConn.setDoInput(true);
            imgConn.setUseCaches(true);
            imgConn.connect();

            Bitmap bitmap = BitmapFactory.decodeStream(imgConn.getInputStream());
            return bitmap;

        } catch (IOException e) {
            Log.e(null, "downloadImageByUrl: ", e);
            return null;
        } finally {
            if(imgConn != null) {
                imgConn.disconnect();
            }
        }
    }
}
