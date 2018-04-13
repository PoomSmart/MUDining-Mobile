package com.untitled.untitledapk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.untitled.untitledapk.persistence.RestaurantImage;
import com.untitled.untitledapk.persistence.RestaurantImageDao;
import com.untitled.untitledapk.persistence.RestaurantImagesDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Flowable;

public class RestaurantImageManager {

    public static void saveImage(Context context, Integer restaurantId, Bitmap image) {
        FileOutputStream outputStream;
        String fileName = restaurantId + ".jpg";
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(image.getNinePatchChunk());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getImage(Context context, Integer restaurantId) {
        RestaurantImagesDatabase restaurantImagesDatabase = RestaurantImagesDatabase.getInstance(context);
        RestaurantImageDao restaurantImageDao = restaurantImagesDatabase.restaurantImageDao();
        Flowable<RestaurantImage> restaurantImage = restaurantImageDao.getRestaurantImage(restaurantId);
        String imageName = restaurantImage.blockingFirst().getImageId();
        try {
            InputStream input = new FileInputStream(new File(context.getFilesDir(), imageName));
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
    }
}
