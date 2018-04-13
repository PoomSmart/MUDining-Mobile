package com.untitled.untitledapk;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    private void populateRestaurants() {
        Context context = getApplicationContext();
        RestaurantManager.insertRestaurant(context, new Restaurant("Restaurant Alpha", 13.1533, 105.2246, 3, 7, "Awesome Restaurant"));
        RestaurantManager.insertRestaurant(context, new Restaurant("Restaurant Beta", 13.1532, 105.2246, 1, 6, "Lovely Restaurant"));
        RestaurantManager.insertRestaurant(context, new Restaurant("Restaurant Gamma", 13.15315, 105.22405, 4, 4, "Wannabe Restaurant"));
    }

    private void populateRestaurantImages() {
        RestaurantImagesDatabase restaurantImagesDatabase = RestaurantImagesDatabase.getInstance(getApplicationContext());
        RestaurantImageDao restaurantImageDao = restaurantImagesDatabase.restaurantImageDao();
        restaurantImageDao.insertRestaurantImage(new RestaurantImage(1, "1"));
        restaurantImageDao.insertRestaurantImage(new RestaurantImage(2, "2"));
        restaurantImageDao.insertRestaurantImage(new RestaurantImage(3, "3"));
    }

    private boolean isDefaultImagesCopied(Context context) {
        File file = new File(context.getFilesDir(), "copied");
        return file.exists();
    }

    private void writeFile(File file, String data) {
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

    private void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[5120];
        int length = input.read(buffer);
        while (length > 0) {
            output.write(buffer, 0, length);
            length = input.read(buffer);
        }
    }

    private void copyFromAssetsToStorage(Context context, String sourceFile, String destinationFile) throws IOException {
        InputStream IS = context.getAssets().open(sourceFile);
        OutputStream OS = new FileOutputStream(destinationFile);
        copyStream(IS, OS);
        OS.flush();
        OS.close();
        IS.close();
    }

    private boolean copyDefaultImages(Context context) {
        if (isDefaultImagesCopied(context))
            return false;
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
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (copyDefaultImages(getApplicationContext()))
            new PopulateDatabasesTask().execute();
    }

    private class PopulateDatabasesTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... voids) {
            populateRestaurants();
            populateRestaurantImages();
            return null;
        }
    }
}
