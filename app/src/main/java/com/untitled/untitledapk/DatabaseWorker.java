package com.untitled.untitledapk;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.untitled.untitledapk.persistence.Restaurant;
import com.untitled.untitledapk.persistence.RestaurantDao;
import com.untitled.untitledapk.persistence.RestaurantImage;
import com.untitled.untitledapk.persistence.RestaurantImageDao;
import com.untitled.untitledapk.persistence.RestaurantImagesDatabase;
import com.untitled.untitledapk.persistence.RestaurantsDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static com.untitled.untitledapk.RestaurantImageManager.readRestaurantImages;
import static com.untitled.untitledapk.RestaurantManager.readRestaurants;

public class DatabaseWorker {

    private static void populateRestaurants(Context context) {
        RestaurantsDatabase restaurantsDatabase = RestaurantsDatabase.getInstance(context);
        RestaurantDao restaurantDao = restaurantsDatabase.restaurantDao();
        // Food Types: 1, 2 | Category Types: 1, 2, 3
        restaurantDao.insertRestaurant(new Restaurant("Restaurant Alpha", 13.1533, 105.2246, 3, 7, "Awesome Restaurant"));
        // Food Types: 1 | Category Types: 2, 3
        restaurantDao.insertRestaurant(new Restaurant("Restaurant Beta", 13.1532, 105.2246, 1, 6, "Lovely Restaurant"));
        // Food Types: 3 | Category Types: 2
        restaurantDao.insertRestaurant(new Restaurant("Restaurant Gamma", 13.15315, 105.22405, 4, 4, "Wannabe Restaurant"));
    }

    private static void populateRestaurantImages(Context context) {
        RestaurantImagesDatabase restaurantImagesDatabase = RestaurantImagesDatabase.getInstance(context);
        RestaurantImageDao restaurantImageDao = restaurantImagesDatabase.restaurantImageDao();
        int i = 1;
        RestaurantManager.readRestaurants(context);
        for (Restaurant restaurant : RestaurantManager.getRestaurants()) {
            restaurantImageDao.insertRestaurantImage(new RestaurantImage(restaurant.getId(), i++ + ""));
        }
    }

    private static boolean isDefaultImagesCopied(Context context) {
        File file = new File(context.getFilesDir(), "copied");
        return file.exists();
    }

    private static boolean isDatabaseSet(Context context) {
        File file = new File(context.getFilesDir(), "dbset");
        return file.exists();
    }

    private static void writeEmptyFile(File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write("");
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
        writeEmptyFile(new File(internalStorage, "copied"));
    }

    public static void work(Context context) {
        if (!isDefaultImagesCopied(context))
            copyDefaultImages(context);
        new PopulateDatabasesTask().execute(context);
    }

    private static class ReadDatabaseTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            Context context = (Context) params[0];
            readRestaurants(context);
            readRestaurantImages(context);
            return null;
        }
    }

    private static class PopulateDatabasesTask extends AsyncTask<Object, Void, Context> {
        @Override
        protected Context doInBackground(Object... params) {
            Context context = (Context) params[0];
            if (!isDatabaseSet(context)) {
                populateRestaurants(context);
                populateRestaurantImages(context);
                writeEmptyFile(new File(context.getFilesDir(), "dbset"));
            }
            return context;
        }

        @Override
        protected void onPostExecute(Context context) {
            new ReadDatabaseTask().execute(context);
        }
    }
}
