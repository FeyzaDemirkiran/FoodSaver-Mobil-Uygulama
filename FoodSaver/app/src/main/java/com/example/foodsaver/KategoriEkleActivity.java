package com.example.foodsaver;



import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class KategoriEkleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kategori_ekle);
        setupBottomNav();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        kategorileriFirebasedenYukleYoksaEkle();
    }

    private void kategorileriFirebasedenYukleYoksaEkle() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Kategoriler").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        List<Kategori> kategoriler = new ArrayList<>();

                        kategoriler.add(new Kategori("Et Ürünleri", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fet.jpeg?alt=media", "#FFF0F0"));
                        kategoriler.add(new Kategori("Şarküteri", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fsarkuteri.jpeg?alt=media", "#FFE4E1"));
                        kategoriler.add(new Kategori("Atıştırmalık", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fatistirmalik.jpeg?alt=media", "#FFFDE7"));


                        kategoriler.add(new Kategori("Fırından", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Ffirin.jpeg?alt=media", "#FBE9E7"));
                        kategoriler.add(new Kategori("Süt & Kahvaltılık", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fkahvalti.jpeg?alt=media", "#E8F5E9"));

                        kategoriler.add(new Kategori("Temel Gıda", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Ftemelgida.jpeg?alt=media", "#FFF8E1"));
                        kategoriler.add(new Kategori("Hazır & Donuk Gıda", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fdonukgida.jpeg?alt=media", "#FBE9E7"));
                        kategoriler.add(new Kategori("Sağlıklı Yaşam", "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fsaglikliyasam.jpeg?alt=media", "#E8F5E9"));
                        kategoriler.add(new Kategori("Sebze & Meyve",
                                "https://firebasestorage.googleapis.com/v0/b/fir-kullanimi-226f0.appspot.com/o/KategoriGorsel%2Fsebzemeyve.jpeg?alt=media",
                                "#FFFDE7"));

                        for (Kategori k : kategoriler) {
                            firestore.collection("Kategoriler").add(k);
                        }
                    }
                });
    }
}