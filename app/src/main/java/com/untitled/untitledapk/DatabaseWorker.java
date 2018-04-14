package com.untitled.untitledapk;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.untitled.untitledapk.persistence.Restaurant;
import com.untitled.untitledapk.persistence.RestaurantImage;
import com.untitled.untitledapk.persistence.RestaurantImageDao;
import com.untitled.untitledapk.persistence.RestaurantImagesDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class DatabaseWorker {

    private static void populateRestaurants(Context context) {
        RestaurantManager.insertRestaurant(context, new Restaurant("Restaurant Alpha", 13.1533, 105.2246, 3, 7, "Awesome Restaurant"));
        RestaurantManager.insertRestaurant(context, new Restaurant("Restaurant Beta", 13.1532, 105.2246, 1, 6, "Lovely Restaurant"));
        RestaurantManager.insertRestaurant(context, new Restaurant("Restaurant Gamma", 13.15315, 105.22405, 4, 4, "Wannabe Restaurant"));
    }

    private static void populateRestaurantImages(Context context) {
        RestaurantImagesDatabase restaurantImagesDatabase = RestaurantImagesDatabase.getInstance(context);
        RestaurantImageDao restaurantImageDao = restaurantImagesDatabase.restaurantImageDao();
        restaurantImageDao.insertRestaurantImage(new RestaurantImage(1, "1"));
        restaurantImageDao.insertRestaurantImage(new RestaurantImage(2, "2"));
        restaurantImageDao.insertRestaurantImage(new RestaurantImage(3, "3"));
    }

    private static boolean isDefaultImagesCopied(Context context) {
        File file = new File(context.getFilesDir(), "copied");
        return file.exists();
    }

    private static boolean isDatabaseSet(Context context) {
        File file = new File(context.getFilesDir(), "dbset");
        return file.exists();
    }

    private static void writeFile(File file, String data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[5120];
        int length = input.read(buffer);
        while (length > 0) {
            output.write(buffer, 0, length);
            length = input.read(buffer);
        }
    }

    private static void copyFromAssetsToStorage(Context context, String sourceFile, String destinationFile) throws IOException {
        InputStream IS = context.getAssets().open(sourceFile);
        OutputStream OS = new FileOutputStream(destinationFile);
        copyStream(IS, OS);
        OS.flush();
        OS.close();
        IS.close();
    }

    private static void copyDefaultImages(Context context) {
        AssetManager assetManager = context.getAssets();
        String assetPath = RestaurantImageManager.imageFolder;
        File internalStorage = context.getFilesDir();
        String internalStoragePath = internalStorage.getPath() + File.separator + RestaurantImageManager.imageFolder;
        File internalStorageFolder = new File(internalStoragePath);
        if (!internalStorageFolder.exists())
            internalStorageFolder.mkdir();
        try {
            String images[] = assetManager.list(assetPath);
            for (String image : images) {
                copyFromAssetsToStorage(context, assetPath + File.separator + image, internalStoragePath + File.separator + image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeFile(new File(internalStorage, "copied"), "");
    }

    public static void work(Context context) {
        if (!isDefaultImagesCopied(context))
            copyDefaultImages(context);
        if (!isDatabaseSet(context)) {
            new PopulateDatabasesTask().execute(context);
            writeFile(new File(context.getFilesDir(), "dbset"), "");
        }
    }

    private static class PopulateDatabasesTask extends AsyncTask<Context, Void, Void> {
        @Override
        protected Void doInBackground(Context... contexts) {
            Context context = contexts[0];
            populateRestaurants(context);
            populateRestaurantImages(context);
            return null;
        }
    }
}
