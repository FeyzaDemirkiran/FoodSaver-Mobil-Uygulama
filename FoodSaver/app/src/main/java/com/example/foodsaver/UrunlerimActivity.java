package com.example.foodsaver;



import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.view.View;
import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UrunlerimActivity extends BaseActivity {

    LinearLayout urunListesi;
    Veritabani veritabani;
    SearchView searchView;
    ImageButton btnFilter;
    TextView btnTumUrunler, btn1Hafta, btn2Hafta, btn3Hafta, btn1Ay, btn2Ay, btn3Ay;

    String secilenKategori = "Tümü";
    String secilenTarihFiltre = "Tümü";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urunlerim);
        setupBottomNav();
        setupNavigationDrawer(R.layout.activity_urunlerim);

        urunListesi = findViewById(R.id.urunListesi);
        searchView = findViewById(R.id.searchView);
        veritabani = new Veritabani(this);
        btnFilter = findViewById(R.id.btnFilter);


        btnTumUrunler = findViewById(R.id.btnTumUrunler);
        btn1Hafta = findViewById(R.id.btn1Hafta);
        btn2Hafta = findViewById(R.id.btn2Hafta);
        btn3Hafta = findViewById(R.id.btn3Hafta);
        btn1Ay = findViewById(R.id.btn1Ay);
        btn2Ay = findViewById(R.id.btn2Ay);
        btn3Ay = findViewById(R.id.btn3Ay);


        btnTumUrunler.setOnClickListener(v -> {
            secilenTarihFiltre = "Tümü";
            listele(searchView.getQuery().toString(), secilenKategori, secilenTarihFiltre);
        });

        btn1Hafta.setOnClickListener(v -> {
            secilenTarihFiltre = "1 Hafta";
            listele(searchView.getQuery().toString(), secilenKategori, secilenTarihFiltre);
        });

        btn2Hafta.setOnClickListener(v -> {
            secilenTarihFiltre = "2 Hafta";
            listele(searchView.getQuery().toString(), secilenKategori, secilenTarihFiltre);
        });

        btn3Hafta.setOnClickListener(v -> {
            secilenTarihFiltre = "3 Hafta";
            listele(searchView.getQuery().toString(), secilenKategori, secilenTarihFiltre);
        });

        btn1Ay.setOnClickListener(v -> {
            secilenTarihFiltre = "1 Ay";
            listele(searchView.getQuery().toString(), secilenKategori, secilenTarihFiltre);
        });

        btn2Ay.setOnClickListener(v -> {
            secilenTarihFiltre = "2 Ay";
            listele(searchView.getQuery().toString(), secilenKategori, secilenTarihFiltre);
        });

        btn3Ay.setOnClickListener(v -> {
            secilenTarihFiltre = "3 Ay";
            listele(searchView.getQuery().toString(), secilenKategori, secilenTarihFiltre);
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listele(query, secilenKategori, secilenTarihFiltre);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listele(newText, secilenKategori, secilenTarihFiltre);
                return false;
            }
        });

        btnFilter.setOnClickListener(v -> {
            final String[] kategoriler = {"Tümü", "Süt Ürünleri", "Şarküteri Ürünleri", "Et ve Balık Ürünleri", "Baklagiller ve Tahıllar", "Unlu Mamuller", "Kahvaltılık Ürünler", "Dondurulmuş Gıdalar", "Konserve Ürünler", "Soslar ve Baharatlar", "Atıştırmalıklar", "Tatlılar", "İçecekler"};
            AlertDialog.Builder builder = new AlertDialog.Builder(UrunlerimActivity.this);
            builder.setTitle("Kategori Seç");

            builder.setItems(kategoriler, (dialog, which) -> {
                secilenKategori = kategoriler[which];
                listele(searchView.getQuery().toString(), secilenKategori, secilenTarihFiltre);
            });

            builder.show();
        });


        listele("", secilenKategori, secilenTarihFiltre);
    }

    private void listele(String aramaKelimesi, String kategoriFiltre, String tarihFiltre) {
        urunListesi.removeAllViews();

        Cursor cursor = veritabani.tumUrunleriGetir();
        while (cursor.moveToNext()) {
            String urunAdi = cursor.getString(cursor.getColumnIndexOrThrow("urunAdi"));
            String sonTarih = cursor.getString(cursor.getColumnIndexOrThrow("sonTarih"));
            byte[] resim = cursor.getBlob(cursor.getColumnIndexOrThrow("resim"));
            String barkod = cursor.getString(cursor.getColumnIndexOrThrow("barkod"));
            String kategori = cursor.getString(cursor.getColumnIndexOrThrow("kategori"));
            int adet = cursor.getInt(cursor.getColumnIndexOrThrow("adet"));

            boolean kategoriUyumlu = kategoriFiltre.equals("Tümü") || kategori.equalsIgnoreCase(kategoriFiltre);
            boolean aramaUyumlu = urunAdi.toLowerCase().contains(aramaKelimesi.toLowerCase());


            boolean tarihUyumlu = isTarihUyumlu(sonTarih, tarihFiltre);

            if (kategoriUyumlu && aramaUyumlu && tarihUyumlu) {
                View urunView = getLayoutInflater().inflate(R.layout.activity_item_urun, null);
                ImageView imageView = urunView.findViewById(R.id.imageViewFoto);
                TextView textViewAdi = urunView.findViewById(R.id.textViewAdi);
                TextView textViewTarih = urunView.findViewById(R.id.textViewTarih);
                TextView textViewKalanGun = urunView.findViewById(R.id.textViewKalanGun);
                ImageView imageViewSil = urunView.findViewById(R.id.copkutusu);

                TextView textViewKategori = urunView.findViewById(R.id.textViewKategori);
                TextView textViewAdet = urunView.findViewById(R.id.textViewAdet);

                imageView.setImageBitmap(BitmapFactory.decodeByteArray(resim, 0, resim.length));
                textViewAdi.setText(urunAdi);
                textViewTarih.setText("SKT: " + sonTarih);
                textViewKategori.setText(kategori);
                textViewAdet.setText("  " + adet + " adet");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Date bugun = new Date();
                    Date tarih = dateFormat.parse(sonTarih);
                    long fark = tarih.getTime() - bugun.getTime();
                    long kalanGun = fark / (1000 * 60 * 60 * 24);

                    if (kalanGun >= 0) {
                        textViewKalanGun.setText("Kalan gün: " + kalanGun);
                    } else {
                        textViewKalanGun.setText("Süre dolmuş!");
                    }

                } catch (ParseException e) {
                    textViewKalanGun.setText("Tarih biçimi hatalı");
                    e.printStackTrace();
                }

                imageViewSil.setOnClickListener(v -> {
                    veritabani.urunSil(barkod);
                    urunListesi.removeView(urunView);
                });

                urunListesi.addView(urunView);
            }
        }
        cursor.close();
    }

    private boolean isTarihUyumlu(String sonTarih, String tarihFiltre) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date bugun = new Date();
            Date tarih = dateFormat.parse(sonTarih);

            if (tarihFiltre.equals("Tümü")) {
                return true;
            }

            long fark = tarih.getTime() - bugun.getTime();
            long kalanGun = fark / (1000 * 60 * 60 * 24);

            switch (tarihFiltre) {
                case "1 Hafta":
                    return kalanGun <= 7;
                case "2 Hafta":
                    return kalanGun <= 14;
                case "3 Hafta":
                    return kalanGun <= 21;
                case "1 Ay":
                    return kalanGun <= 30;
                case "2 Ay":
                    return kalanGun <= 60;
                case "3 Ay":
                    return kalanGun <= 90;
                default:
                    return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
