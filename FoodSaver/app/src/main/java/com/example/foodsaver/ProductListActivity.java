package com.example.foodsaver;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {

    private RecyclerView urunGorunumu;
    private List<Product> urunler = new ArrayList<>();
    private ProductAdapter urunAdapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setupBottomNav();

        urunGorunumu = findViewById(R.id.kategoriGorunumu);
        urunAdapter = new ProductAdapter(this, urunler);
        urunGorunumu.setLayoutManager(new GridLayoutManager(this, 2));
        urunGorunumu.setAdapter(urunAdapter);

        firestore = FirebaseFirestore.getInstance();
        String kategoriAdi = getIntent().getStringExtra("kategori");


        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean jsonYuklendiMi = prefs.getBoolean("json_yuklendi", false);

        if (!jsonYuklendiMi) {
            JsonProductLoading.urunleriFirestoreaYukle(getApplicationContext());
            prefs.edit().putBoolean("json_yuklendi", true).apply();
        }


        firestore.collection("Kategoriler")
                .document(kategoriAdi)
                .collection("Urunler")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    urunler.clear();
                    for (QueryDocumentSnapshot belge : querySnapshot) {
                        Product u = belge.toObject(Product.class);
                        urunler.add(u);
                    }
                    urunAdapter.notifyDataSetChanged();
                });
    }
}