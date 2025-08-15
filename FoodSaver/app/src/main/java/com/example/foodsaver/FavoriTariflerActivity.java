package com.example.foodsaver;



import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class FavoriTariflerActivity extends BaseActivity {

    private GridView listViewFavoriler;
    private ArrayList<Tarif> favoriListesi;
    private TarifAdapter adapter;
    private FirestoreTarifManager firestoreManager;
    private TextView txtview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori_tarifler);


        EdgeToEdge.enable(this);

        setupBottomNav();
        setupNavigationDrawer(R.layout.activity_favori_tarifler);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        txtview = findViewById(R.id.textView);
        listViewFavoriler = findViewById(R.id.listeFavoriler);
        favoriListesi = new ArrayList<>();
        adapter = new TarifAdapter(this, favoriListesi);
        listViewFavoriler.setAdapter(adapter);

        firestoreManager = new FirestoreTarifManager();


        favoriTarifleriYukle();


        listViewFavoriler.setOnItemClickListener((parent, view, position, id) -> {
            Tarif secilenTarif = favoriListesi.get(position);
            Intent intent = new Intent(FavoriTariflerActivity.this, TarifDetayActivity.class);
            intent.putExtra("yemekAdi", secilenTarif.getYemekAdi());
            startActivity(intent);
        });
    }

    private void favoriTarifleriYukle() {
        favoriListesi.clear();


        firestoreManager.favoriTarifleriGetir(tarifList -> {
            favoriListesi.addAll(tarifList);
            adapter.notifyDataSetChanged();
        });
    }
}