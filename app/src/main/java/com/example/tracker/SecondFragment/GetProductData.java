package com.example.tracker.SecondFragment;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static androidx.core.content.ContentProviderCompat.requireContext;

/*public class GetProductData {
    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v0/product/";

    public static ArrayList<String> getProductsData(String barcode) {
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + barcode + ".json";

        Request request = new Request.Builder()
                .url(url)
                .build();

        ArrayList<String> nutritions = new ArrayList<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (Response responseTest = client.newCall(request).execute()) {

                    if (response.isSuccessful()) {
                        String responseData = response.body().string();

                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONObject product = jsonObject.getJSONObject("product");
                        JSONObject nutrition = product.getJSONObject("nutriments");

                        String calorie = "Kalorien: " + nutrition.optDouble("energy-kcal_100g", 0) ;
                        String protein = "Protein: " + nutrition.optDouble("proteins_100g", 0);
                        String carbohydrates = "Carbohydrates: " + nutrition.optDouble("carbohydrates_100g", 0);
                        String fat = "Fat: " + nutrition.optDouble("fat_100g", 0);


                        Log.e("CameraFragment:", calorie);
                        Log.e("CameraFragment:", protein);
                        Log.e("CameraFragment:", carbohydrates);
                        Log.e("CameraFragment:", fat);

                        nutritions.add(calorie);
                        nutritions.add(protein);
                        nutritions.add(carbohydrates);
                        nutritions.add(fat);



                    } else {
                        System.out.println("Request failed: " + response.code());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        while (nutritions.size() < 4) {
            nutritions.add("N/A"); // Füge Platzhalter hinzu, wenn weniger als 4 Werte vorhanden sind.
        }

        return nutritions;
    }
}*/

public class GetProductData {
    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v0/product/";

    public interface ProductDataCallback {
        void onSuccess(ArrayList<String> nutritionData);
        void onFailure(String errorMessage);
    }

    public static void getProductsData(String barcode, ProductDataCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + barcode + ".json";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onFailure("Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<String> nutritions = new ArrayList<>();

                try (Response responseTest = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONObject product = jsonObject.getJSONObject("product");
                        JSONObject nutrition = product.getJSONObject("nutriments");

                        String name = product.optString("product_name", "0");
                        String calorie = nutrition.optString("energy-kcal_100g", "0") ;
                        String carbohydrates = nutrition.optString("carbohydrates_100g", "0");
                        String protein = nutrition.optString("proteins_100g", "0");
                        String fat = nutrition.optString("fat_100g", "0");
                        String serving = product.optString("serving_quantity", "0");


                        Date currentDate = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(currentDate);

                        nutritions.add(formattedDate);
                        nutritions.add(name);
                        nutritions.add(calorie);
                        nutritions.add(carbohydrates);
                        nutritions.add(protein);
                        nutritions.add(fat);
                        nutritions.add(serving);


                        callback.onSuccess(nutritions);
                    } else {
                        callback.onFailure("Request failed with code: " + response.code());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure("Error parsing response: " + e.getMessage());
                }
            }
        });
    }
}


