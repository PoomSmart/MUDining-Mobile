package com.untitled.untitledapk.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.untitled.untitledapk.persistence.Restaurant;
import com.untitled.untitledapk.persistence.RestaurantDao;
import com.untitled.untitledapk.persistence.RestaurantDatabase;
import com.untitled.untitledapk.persistence.RestaurantImage;
import com.untitled.untitledapk.persistence.RestaurantImageDao;
import com.untitled.untitledapk.persistence.RestaurantImageDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static com.untitled.untitledapk.database.RestaurantImageManager.readRestaurantImages;
import static com.untitled.untitledapk.database.RestaurantManager.readRestaurants;

public class DatabaseWorker {

    private static void populateRestaurants(Context context) {
        RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance(context);
        RestaurantDao restaurantDao = restaurantDatabase.restaurantDao();
/*        // Food Types: 1, 2 | Category Types: 1, 2, 3
        restaurantDao.insertRestaurant(new Restaurant("Restaurant Alpha", 13.1533, 105.2246, 3, 7, "Awesome Restaurant"));
        // Food Types: 1 | Category Types: 2, 3
        restaurantDao.insertRestaurant(new Restaurant("Restaurant Beta", 13.1532, 105.2246, 1, 6, "Lovely Restaurant"));
        // Food Types: 3 | Category Types: 2
        restaurantDao.insertRestaurant(new Restaurant("Restaurant Gamma", 13.15315, 105.22405, 4, 4, "Wannabe Restaurant"));
*/
        restaurantDao.insertRestaurant(new Restaurant("ไม่ตกไม่แตก", 13.7959655, 100.3255092, 9, 3, "This is one of the most well-known restaurants near Mahidol Salaya. Located directly in the front of the university, it serves wide ranges of menus, Thai and European. The most iconic thing of this place is the \"Chef\'s Choice\" menu, which can be any dish from the restaurant for only 60 Baht for those who don\'t want to choose their own menu."));
        restaurantDao.insertRestaurant(new Restaurant("สเต็กลุงหนวด", 13.7947481, 100.3271531, 8, 1, "There are many branches of Steak-lung-nhuad in Thailand, and one of the best is in front of Mahidol University. There are many kind of steaks such as chicken steak, pork chop ,fish steak, beef steak and hotdog. All steaks will be served with fries. You can choose level of steak doneness you prefer with the waiter. Also, there are many dishes of spaghetti, American fried rice, and salad. For beverages, you can order Coke, or water."));
        restaurantDao.insertRestaurant(new Restaurant("J-Class Vegetarian", 13.8016313, 100.3154292, 5, 1, "This vegetarian restaurant is located across Mahidol University near Namnuan restaurant. In the morning it serves noodle, and later in the morning it serves cooked to order food which are all vegetarian food.\n"));
        restaurantDao.insertRestaurant(new Restaurant("พุทธรักษา เรสเทรอง", 13.799496, 100.3258569, 13, 1, "The most delicious steamed duck in three worlds. If you are looking for a special evening with gorgeous surroundings, great food, and plenty of romance, Phuttaraksa-restaurant completely fits the bill."));
        restaurantDao.insertRestaurant(new Restaurant("LODIHAM", 13.7974226, 100.325415, 9, 3, "LODIHAM is a steak restaurant that\'s popular in Salaya and not too expensive. It has been serving delicious food for more than 5 years.The recommended menu are ham-cheeses roll, spicy chicken and teriyaki chicken with rice"));
        restaurantDao.insertRestaurant(new Restaurant("Music Square", 13.7889181, 100.321157, 9, 3, "Classy restaurant in the middle of nature surroundings. It is located in the College of Music of Mahidol University, Salaya campus. This restaurant has both Thai food and European food with great services."));
        restaurantDao.insertRestaurant(new Restaurant("Anya\'s Place", 13.7973155, 100.3283286, 9, 1, "Anya\'s Place is one of the most famous restaurants in Nakhon Pathom. It serves high quality and delicious food with fair price. The service is as nice as if they\'re serving their own family."));
        restaurantDao.insertRestaurant(new Restaurant("Buta Grill", 13.7955481, 100.3257422, 10, 7, "It is a grill restaurant that worth going and it is suitable for seafood lover because of the high quality ingredients. It provides great service and fresh food with tasty sauce in 199 Bath, and 299 for seafood. These prices include the soft drinks."));
        restaurantDao.insertRestaurant(new Restaurant("Hua Seng Hong", 13.7964109, 100.3267302, 5, 3, "Hua Seng Hong is located in the opposite side of Mahidol University. The front of the restaurant has a large, easy to see red label. Parking is available in front of the restaurant and both sides of the restaurant. Hua Seng Hong is a luxury Chinese restaurant that serves high grade dishes. The price ranges from hundreds to thousands."));
        restaurantDao.insertRestaurant(new Restaurant("ธนัญธรก๋วยเตี๋ยวไก่มะระ", 13.8020562, 100.3116657, 1, 1, "This the most interesting restaurant for Kai Mara Noodle lover, it\'s great for night meanl as it opens from 18.30 until 1.00."));
        restaurantDao.insertRestaurant(new Restaurant("Koh Lanta Pizzeria", 13.7925727, 100.3257248, 8, 3, "Lanta Pizzeria\'s pizza is crispy and delicious. You can choose toppings that you want from a wide ranges of choices, preset recommended combinations are also available on the menu."));
        restaurantDao.insertRestaurant(new Restaurant("ปู๊ตี่บะหมี่เกี๊ยวกุ้ง", 13.7589102, 100.4668393, 4, 1, "The old restaurant which located in front of Srifa bakery. It has been serving noodle dishes there for more than 10 years. The highlight is the large shrimp wonton."));
        restaurantDao.insertRestaurant(new Restaurant("ศรีไทยขาหมู", 13.8020154, 100.3090175, 4, 1, "Kao Kha Moo for 75 Baht as dish is worth the money if you compare with the quantity. It serves with a medium-boiled egg."));
        restaurantDao.insertRestaurant(new Restaurant("ครัวการ์ตูน", 13.8016958, 100.3163558, 1, 1, "A small cozy restaurant at the side of Mahidol University. Serves quality Thai dishes with a relatively low price."));
        restaurantDao.insertRestaurant(new Restaurant("สุธารส", 13.7969843, 100.3254455, 1, 1, "It is a noodle restaurant. The highlight is the tasty pork which smells real good. The price is fair with quality and quantity."));

        restaurantDao.insertRestaurant(new Restaurant("D\'Eiffel", 13.7959242, 100.325712, 8, 3, "This is a luxurious classic European style restaurant with a quite high price."));
        restaurantDao.insertRestaurant(new Restaurant("ห้องนั่งเล่น", 13.7962527, 100.3254683, 1, 3, "It is a small restaurant. The highlight is the set menus you can have with cheap price."));
        restaurantDao.insertRestaurant(new Restaurant("โกวเล็ก กุ้งอบวุ้นเส้น", 13.7956523, 100.3255079, 5, 1, "You can enjoy the original taste of Hongkong Suki, Dried fish-maw soup and the other menus here at this food vendor."));
        restaurantDao.insertRestaurant(new Restaurant("Rit วังหลัง", 13.7972856, 100.3274831, 2, 3, "Serving Wanglang style sushi with fair price. Suitable for sushi lovers."));
    }

    private static void populateRestaurantImages(Context context) {
        RestaurantImageDatabase restaurantImageDatabase = RestaurantImageDatabase.getInstance(context);
        RestaurantImageDao restaurantImageDao = restaurantImageDatabase.restaurantImageDao();
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
