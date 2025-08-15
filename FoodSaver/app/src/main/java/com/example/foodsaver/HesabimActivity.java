package com.example.foodsaver;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class HesabimActivity extends BaseActivity {

    private EditText editAd, editSoyad, editEmail, editTelefon, editDogumTarihi, editCinsiyet;
    private Button btnGuncelle;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesabim);
        setupBottomNav();
        setupNavigationDrawer(R.layout.activity_hesabim);


        editAd = findViewById(R.id.editAd);
        editSoyad = findViewById(R.id.editSoyad);
        editEmail = findViewById(R.id.editEmail);
        editTelefon = findViewById(R.id.editTelefon);
        editDogumTarihi = findViewById(R.id.editDogumTarihi);
        editCinsiyet = findViewById(R.id.editCinsiyet);
        btnGuncelle = findViewById(R.id.btnGuncelle);

        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        editDogumTarihi.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    HesabimActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                selectedDay, selectedMonth + 1, selectedYear);
                        editDogumTarihi.setText(formattedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });


        if (currentUser != null) {
            DocumentReference docRef = firestore.collection("Kullanicilar")
                    .document(currentUser.getUid());

            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String ad = documentSnapshot.getString("ad");
                    String soyad = documentSnapshot.getString("soyad");
                    String email = documentSnapshot.getString("email");


                    editAd.setText(basHarfleriBuyut(ad));
                    editSoyad.setText(basHarfleriBuyut(soyad));
                    editEmail.setText(email);
                    editTelefon.setText(documentSnapshot.getString("telefon"));
                    editDogumTarihi.setText(documentSnapshot.getString("dogumTarihi"));
                    editCinsiyet.setText(documentSnapshot.getString("cinsiyet"));
                }
            });
        }


        btnGuncelle.setOnClickListener(v -> {
            String ad = editAd.getText().toString().trim();
            String soyad = editSoyad.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String telefon = editTelefon.getText().toString().trim();
            String dogumTarihi = editDogumTarihi.getText().toString().trim();
            String cinsiyet = editCinsiyet.getText().toString().trim();

            HashMap<String, Object> guncelVeri = new HashMap<>();
            guncelVeri.put("ad", ad);
            guncelVeri.put("soyad", soyad);
            guncelVeri.put("email", email);
            guncelVeri.put("telefon", telefon);
            guncelVeri.put("dogumTarihi", dogumTarihi);
            guncelVeri.put("cinsiyet", cinsiyet);

            firestore.collection("Kullanicilar")
                    .document(currentUser.getUid())
                    .set(guncelVeri, SetOptions.merge())
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Bilgiler başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hata oluştu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });


        ImageButton btnCikisIcon = findViewById(R.id.btnCikisIcon);
        btnCikisIcon.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(HesabimActivity.this, GirisActivity.class); // Giriş ekranına dön
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Backstack temizle
            startActivity(intent);
            finish();
        });
    }


    private String basHarfleriBuyut(String input) {
        if (input == null) return "";
        String[] kelimeler = input.trim().split("\\s+");
        StringBuilder sonuc = new StringBuilder();

        for (String kelime : kelimeler) {
            if (!kelime.isEmpty()) {
                sonuc.append(kelime.substring(0, 1).toUpperCase(new Locale("tr", "TR")))
                        .append(kelime.substring(1).toLowerCase(new Locale("tr", "TR")))
                        .append(" ");
            }
        }
        return sonuc.toString().trim();
    }
}