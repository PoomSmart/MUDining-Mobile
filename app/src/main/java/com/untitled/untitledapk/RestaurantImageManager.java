package com.untitled.untitledapk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.untitled.untitledapk.persistence.RestaurantImage;
import com.untitled.untitledapk.persistence.RestaurantImageDao;
import com.untitled.untitledapk.persistence.RestaurantImagesDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Flowable;

public class RestaurantImageManager {

    public static final String imageFolder = "restaurant_images";
    private static RestaurantImageDao restaurantImageDao = null;

    private static RestaurantImageDao getRestaurantImageDao(Context context) {
        if (restaurantImageDao == null) {
            RestaurantImagesDatabase restaurantImageDatabase = RestaurantImagesDatabase.getInstance(context);
            return restaurantImageDao = restaurantImageDatabase.restaurantImageDao();
        }
        return restaurantImageDao;
    }

    public static void saveImage(Context context, Integer restaurantId, Bitmap image) {
        FileOutputStream outputStream;
        String fileName = restaurantId + ".jpg";
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            outputStream.write(stream.toByteArray());
            outputStream.close();
            getRestaurantImageDao(context).insertRestaurantImage(new RestaurantImage(restaurantId, restaurantId + ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeImage(Context context, Integer restaurantId) {
        String fileName = restaurantId + ".jpg";
        File file = new File(context.getFilesDir() + File.separator + imageFolder, fileName);
        file.delete();
        getRestaurantImageDao(context).deleteRestaurantImage(restaurantId);
    }

    public static Bitmap getImage(Context context, Integer restaurantId) {
        Flowable<RestaurantImage> restaurantImage = getRestaurantImageDao(context).getRestaurantImage(restaurantId);
        String imageName = restaurantImage.blockingFirst().getImageId();
        try {
            InputStream input = new FileInputStream(new File(context.getFilesDir() + File.separator + imageFolder, imageName));
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
    }
}
