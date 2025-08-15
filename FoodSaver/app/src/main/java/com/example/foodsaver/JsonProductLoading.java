package com.example.foodsaver;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonProductLoading {


    public static Map<String, List<Product>> urunVerileri = new HashMap<>();

    public static void urunleriFirestoreaYukle(Context context) {
        try {

            InputStreamReader reader = new InputStreamReader(context.getAssets().open("urunler.json"), "UTF-8");
            Type mapType = new TypeToken<Map<String, List<Product>>>() {}.getType();
            urunVerileri = new Gson().fromJson(reader, mapType);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            for (Map.Entry<String, List<Product>> kategori : urunVerileri.entrySet()) {
                String kategoriAdi = kategori.getKey();
                List<Product> urunler = kategori.getValue();

                for (Product urun : urunler) {
                    String urunAdi = urun.getAd();

                    firestore.collection("Kategoriler")
                            .document(kategoriAdi)
                            .collection("Urunler")
                            .document(urunAdi)
                            .get()
                            .addOnSuccessListener(snapshot -> {
                                if (!snapshot.exists()) {
                                    firestore.collection("Kategoriler")
                                            .document(kategoriAdi)
                                            .collection("Urunler")
                                            .document(urunAdi)
                                            .set(urun)
                                            .addOnSuccessListener(aVoid ->
                                                    Log.d("FirestoreYukle", kategoriAdi + " > " + urunAdi + " eklendi."))
                                            .addOnFailureListener(e ->
                                                    Log.e("FirestoreYukle", "Ekleme hatası: " + urunAdi, e));
                                } else {
                                    Log.d("FirestoreYukle", kategoriAdi + " > " + urunAdi + " zaten kayıtlı, atlandı.");
                                }
                            })
                            .addOnFailureListener(e ->
                                    Log.e("FirestoreYukle", "Belge kontrolü hatası: " + urunAdi, e));
                }
            }

        } catch (Exception e) {
            Log.e("JsonProductLoading", "JSON okuma hatası:", e);
        }
    }
}