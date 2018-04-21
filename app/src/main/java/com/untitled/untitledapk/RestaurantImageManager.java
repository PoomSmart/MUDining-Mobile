package com.untitled.untitledapk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.untitled.untitledapk.persistence.RestaurantImage;
import com.untitled.untitledapk.persistence.RestaurantImageDao;
import com.untitled.untitledapk.persistence.RestaurantImagesDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Flowable;

public class RestaurantImageManager {

    public static final String imageFolder = "restaurant_images";
    private static RestaurantImageDao restaurantImageDao = null;
    private static List<RestaurantImage> cachedRestaurantImages = null;

    private static RestaurantImageDao getRestaurantImageDao(Context context) {
        if (restaurantImageDao == null) {
            RestaurantImagesDatabase restaurantImageDatabase = RestaurantImagesDatabase.getInstance(context);
            return restaurantImageDao = restaurantImageDatabase.restaurantImageDao();
        }
        return restaurantImageDao;
    }

    public static void readRestaurantImages(Context context) {
        if (cachedRestaurantImages == null)
            cachedRestaurantImages = getRestaurantImageDao(context).getRestaurantImages();
    }

    public static void saveImage(Context context, String restaurantId, Bitmap image) {
        FileOutputStream outputStream;
        String fileName = restaurantId + ".jpg";
        try {
            File file = new File(context.getFilesDir() + File.separator + imageFolder, fileName);
            outputStream = new FileOutputStream(file);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            outputStream.write(stream.toByteArray());
            outputStream.close();
            new AsyncTask<Object, Void, Void>() {
                @Override
                protected Void doInBackground(Object... objects) {
                    getRestaurantImageDao(context).insertRestaurantImage(new RestaurantImage(restaurantId, restaurantId));
                    return null;
                }
            }.execute();
            if (cachedRestaurantImages != null) {
                cachedRestaurantImages.removeIf(restaurantImage -> restaurantImage.getRestaurantId().equals(restaurantId));
                cachedRestaurantImages.add(new RestaurantImage(restaurantId, restaurantId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeImage(Context context, String restaurantId) {
        String fileName = restaurantId + ".jpg";
        File file = new File(context.getFilesDir() + File.separator + imageFolder, fileName);
        file.delete();
        new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... objects) {
                getRestaurantImageDao(context).deleteRestaurantImage(restaurantId);
                return null;
            }
        }.execute();
        if (cachedRestaurantImages != null)
            cachedRestaurantImages.removeIf(restaurantImage -> restaurantImage.getRestaurantId().equals(restaurantId));
    }

    @Deprecated
    public static Bitmap getImage(Context context, String restaurantId) {
        Flowable<RestaurantImage> restaurantImage = getRestaurantImageDao(context).getRestaurantImage(restaurantId);
        String imageName = restaurantImage.blockingFirst().getImageId();
        try {
            InputStream input = new FileInputStream(new File(context.getFilesDir() + File.separator + imageFolder, imageName + ".jpg"));
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
    }

    public static void loadImage(Context context, String restaurantId, ImageView imageView) {
        RestaurantImage restaurantImage = null;
        for (RestaurantImage restaurantImage1 : cachedRestaurantImages) {
            if (restaurantImage1.getRestaurantId().equals(restaurantId)) {
                restaurantImage = restaurantImage1;
                break;
            }
        }
        if (restaurantImage != null) {
            String imageName = restaurantImage.getImageId();
            File file = new File(context.getFilesDir() + File.separator + imageFolder, imageName + ".jpg");
            Glide.with(context).load(Uri.fromFile(file)).into(imageView);
        }
    }
}
