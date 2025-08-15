package com.example.foodsaver;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class TariflerActivity extends BaseActivity {

    private TextView aramaEditText;
    private GridView liste;
    private ArrayList<Tarif> tarifListesi;
    private TarifAdapter adapter;
    private FirestoreTarifManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tarifler);

        setupBottomNav();
        setupNavigationDrawer(R.layout.activity_tarifler);

        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        prefs.edit().putBoolean("ilkYukleme", true).apply(); // JSON’dan yükleme yapılacak


        if (prefs.getBoolean("ilkYukleme", true)) {
            TopluTarifYukleyici.tarifleriFirestoreaYukle(this);
            prefs.edit().putBoolean("ilkYukleme", false).apply();
        }


        aramaEditText = findViewById(R.id.aramaEditText);
        liste = findViewById(R.id.liste);
        tarifListesi = new ArrayList<>();
        adapter = new TarifAdapter(this, tarifListesi);
        liste.setAdapter(adapter);
        firestoreManager = new FirestoreTarifManager();


        tumTarifleriYukle();


        aramaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String aranan = s.toString().trim();
                if (!aranan.isEmpty()) {
                    tarifAra(aranan);
                } else {
                    tumTarifleriYukle();
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });


        liste.setOnItemClickListener((parent, view, position, id) -> {
            Tarif secilenTarif = tarifListesi.get(position);
            Intent intent = new Intent(TariflerActivity.this, TarifDetayActivity.class);
            intent.putExtra("yemekAdi", secilenTarif.getYemekAdi());
            startActivity(intent);
        });


        ImageView btnFavoriler = findViewById(R.id.btnFavoriler);
        if (btnFavoriler != null) {
            btnFavoriler.setOnClickListener(v -> {
                Intent intent = new Intent(TariflerActivity.this, FavoriTariflerActivity.class);
                startActivity(intent);
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        String aranan = aramaEditText.getText().toString().trim();
        if (!aranan.isEmpty()) {
            tarifAra(aranan);
        } else {
            tumTarifleriYukle();
        }
    }

    private void tumTarifleriYukle() {
        firestoreManager.tumTarifleriGetir(tarifler -> {
            tarifListesi.clear();
            if (tarifler == null || tarifler.isEmpty()) {
                Toast.makeText(this, "Henüz tarif eklenmedi.", Toast.LENGTH_SHORT).show();
            } else {
                tarifListesi.addAll(tarifler);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void tarifAra(String kelime) {
        firestoreManager.tarifAra(kelime, tarifler -> {
            tarifListesi.clear();
            tarifListesi.addAll(tarifler);
            adapter.notifyDataSetChanged();
        });
    }
}