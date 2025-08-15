package com.example.foodsaver;



import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends BaseActivity {

    private RecyclerView kategoriGorunumu;
    private List<Kategori> kategoriler;
    private KategoriAdapter kategoriAdapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setupBottomNav();
        setupNavigationDrawer(R.layout.activity_category);

        SharedPreferences prefs = getSharedPreferences("ilkAcilisKontrol", MODE_PRIVATE);
        boolean yuklendiMi = prefs.getBoolean("urunYuklendi", false);

        if (!yuklendiMi) {
            JsonProductLoading.urunleriFirestoreaYukle(this);
            prefs.edit().putBoolean("urunYuklendi", true).apply();
        }

        kategoriGorunumu = findViewById(R.id.kategoriGorunumu);
        SearchView arama = findViewById(R.id.aramaCubugu);

        firestore = FirebaseFirestore.getInstance();
        kategoriler = new ArrayList<>();
        kategoriAdapter = new KategoriAdapter(this, kategoriler);

        kategoriGorunumu.setLayoutManager(new GridLayoutManager(this, 3));
        kategoriGorunumu.setAdapter(kategoriAdapter);

        arama.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                kategoriAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                kategoriAdapter.getFilter().filter(newText);
                return false;
            }
        });
        Log.d("FIREBASE", "onCreate çalıştı");

        kategorileriFirebasedenYukleYoksaEkle();
        kategorileriYukle();
    }

    private void kategorileriYukle() {
        firestore.collection("Kategoriler")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    kategoriler.clear();
                    for (QueryDocumentSnapshot belge : querySnapshot) {
                        Kategori kategori = belge.toObject(Kategori.class);
                        kategoriler.add(kategori);
                    }

                    kategoriAdapter = new KategoriAdapter(this, kategoriler);
                    kategoriGorunumu.setAdapter(kategoriAdapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Veriler alınamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void kategorileriFirebasedenYukleYoksaEkle() {
        firestore.collection("Kategoriler").get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        List<Kategori> kategoriler = new ArrayList<>();

                        kategoriler.add(new Kategori("Et Ürünleri", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fet.jpeg?alt=media", "#FFF0F0"));
                        kategoriler.add(new Kategori("Şarküteri", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fsarkuteri.jpeg?alt=media", "#FFE4E1"));
                        kategoriler.add(new Kategori("Atıştırmalık", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fatistirmalik.jpeg?alt=media", "#FFFDE7"));

                        kategoriler.add(new Kategori("Fırından", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Ffirin.jpeg?alt=media", "#FBE9E7"));
                        kategoriler.add(new Kategori("Süt & Kahvaltılık", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fkahvalti.jpeg?alt=media", "#E8F5E9"));

                        kategoriler.add(new Kategori("Temel Gıda", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Ftemelgida.jpeg?alt=media", "#FFF8E1"));
                        kategoriler.add(new Kategori("Hazır & Donuk Gıda", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fdonukgida.jpeg?alt=media", "#FBE9E7"));
                        kategoriler.add(new Kategori("Sağlıklı Yaşam", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fsaglikliyasam.jpeg?alt=media", "#E8F5E9"));
                        kategoriler.add(new Kategori("Sebze & Meyve", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fsebzemeyve.jpeg?alt=media", "#FFFDE7"));

                        for (Kategori k : kategoriler) {
                            firestore.collection("Kategoriler")
                                    .document(k.getAd())
                                    .set(k);
                        }
                    }
                });
    }
}