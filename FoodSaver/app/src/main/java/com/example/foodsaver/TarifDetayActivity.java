package com.example.foodsaver;



import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;

public class TarifDetayActivity extends AppCompatActivity {

    private TextView textViewBaslik, textViewTarif;
    private ImageView imageViewYemek, imageViewFavori;
    private Button btnBesinDegeri, btnIpucu;

    private String yemekAdi;
    private boolean favoriMi = false;

    private Tarif secilenTarif;
    private final FirestoreTarifManager firestoreManager = new FirestoreTarifManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarif_detay);


        textViewBaslik = findViewById(R.id.textViewBaslik);
        textViewTarif = findViewById(R.id.textViewTarif);
        imageViewYemek = findViewById(R.id.imageViewYemek);
        imageViewFavori = findViewById(R.id.imageViewFavori);
        btnBesinDegeri = findViewById(R.id.btnBesinDegeri);
        btnIpucu = findViewById(R.id.btnIpucu);

        findViewById(R.id.anaLayout).setOnClickListener(v -> finish());


        Intent intent = getIntent();
        yemekAdi = intent.getStringExtra("yemekAdi");
        textViewBaslik.setText(yemekAdi);


        firestoreManager.yemekDetayiGetir(yemekAdi, doc -> {
            if (doc.exists()) {
                secilenTarif = mapToTarif(doc);
                tarifVerisiniGoster(secilenTarif);
            } else {
                Toast.makeText(this, "Tarif bulunamadı", Toast.LENGTH_SHORT).show();
            }
        });


        imageViewFavori.setOnClickListener(v -> {
            if (secilenTarif == null) return;

            favoriMi = !favoriMi;
            secilenTarif.setFavori(favoriMi);
            firestoreManager.favoriGuncelle(secilenTarif.getYemekAdi(), favoriMi);
            imageViewFavori.setImageResource(favoriMi ? R.drawable.dolukalp : R.drawable.boskalp);
        });


        btnBesinDegeri.setOnClickListener(v -> {
            if (secilenTarif == null) return;
            new AlertDialog.Builder(this)
                    .setTitle("Besin Değeri")
                    .setMessage(secilenTarif.getBesinDegeri())
                    .setPositiveButton("Kapat", null)
                    .show();
        });


        btnIpucu.setOnClickListener(v -> {
            if (secilenTarif == null) return;
            new AlertDialog.Builder(this)
                    .setTitle("İpucu")
                    .setMessage(secilenTarif.getIpuclari())
                    .setPositiveButton("Kapat", null)
                    .show();
        });
    }

    private Tarif mapToTarif(DocumentSnapshot doc) {
        return new Tarif(
                doc.getString("yemekAdi"),
                doc.getString("malzemeler"),
                doc.getString("yapilis"),
                doc.getString("gorselUrl"),
                doc.getString("besinDegeri"),
                doc.getString("ipuclari"),
                Boolean.TRUE.equals(doc.getBoolean("favori"))
        );
    }

    private void tarifVerisiniGoster(Tarif tarif) {
        favoriMi = tarif.isFavori();
        String yapilis = tarif.getYapilis();

        SpannableString styledText = new SpannableString(yapilis);
        int malzIndex = yapilis.indexOf("Malzemeler:");
        int yapilIndex = yapilis.indexOf("Yapılışı:");
        if (malzIndex != -1) {
            styledText.setSpan(new android.text.style.StyleSpan(Typeface.BOLD),
                    malzIndex, malzIndex + "Malzemeler:".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (yapilIndex != -1) {
            styledText.setSpan(new android.text.style.StyleSpan(Typeface.BOLD),
                    yapilIndex, yapilIndex + "Yapılışı:".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textViewTarif.setText(styledText);

        if (tarif.getGorselUrl() != null && !tarif.getGorselUrl().isEmpty()) {
            Glide.with(this)
                    .load(tarif.getGorselUrl())
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .into(imageViewYemek);
        } else {
            imageViewYemek.setImageResource(R.drawable.noimage);
        }

        imageViewFavori.setImageResource(favoriMi ? R.drawable.dolukalp : R.drawable.boskalp);
    }
}