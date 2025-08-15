package com.example.foodsaver;



import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TopluTarifYukleyici {

    public static void tarifleriFirestoreaYukle(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        try {
            InputStream is = context.getAssets().open("tarifler.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JSONObject rootObject = new JSONObject(jsonBuilder.toString());
            JSONArray jsonArray = rootObject.getJSONArray("tarifler");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Tarif tarif = new Tarif(
                        obj.getString("yemekAdi"),
                        obj.getString("malzemeler"),
                        obj.getString("yapilis"),
                        obj.getString("gorselUrl"),
                        obj.getString("besinDegeri"),
                        obj.getString("ipuclari"),
                        obj.getBoolean("favori") // JSON'dan geliyor
                );

                db.collection("tarifler")
                        .document(tarif.getYemekAdi())
                        .set(tarif)
                        .addOnSuccessListener(aVoid ->
                                Log.d("Yükleme", "Yüklendi: " + tarif.getYemekAdi())
                        )
                        .addOnFailureListener(e ->
                                Log.e("Yükleme", "HATA: " + e.getMessage())
                        );
            }

        } catch (Exception e) {
            Log.e("Yükleme", "Genel hata: " + e.getMessage());
        }
    }
}
