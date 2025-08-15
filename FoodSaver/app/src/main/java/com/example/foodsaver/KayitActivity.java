package com.example.foodsaver;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class KayitActivity extends AppCompatActivity {

    private EditText editAd, editSoyad, editEmail, editSifre, editSifreTekrar;
    private CheckBox checkboxSifreGoster;
    private Button btnKayitOl;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        editAd = findViewById(R.id.editAd);
        editSoyad = findViewById(R.id.editSoyad);
        editEmail = findViewById(R.id.editEmail);
        editSifre = findViewById(R.id.editSifre);
        editSifreTekrar = findViewById(R.id.editSifreTekrar);
        checkboxSifreGoster = findViewById(R.id.checkboxSifreGoster);
        btnKayitOl = findViewById(R.id.btnKayitOl);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        checkboxSifreGoster.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editSifre.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                editSifreTekrar.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                editSifre.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editSifreTekrar.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        btnKayitOl.setOnClickListener(v -> {
            String ad = editAd.getText().toString().trim();
            String soyad = editSoyad.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String sifre = editSifre.getText().toString().trim();
            String sifreTekrar = editSifreTekrar.getText().toString().trim();

            if (ad.isEmpty() || soyad.isEmpty() || email.isEmpty() || sifre.isEmpty() || sifreTekrar.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!sifre.equals(sifreTekrar)) {
                Toast.makeText(this, "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show();
                return;
            }


            mAuth.createUserWithEmailAndPassword(email, sifre)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {

                                HashMap<String, Object> userData = new HashMap<>();
                                userData.put("ad", ad);
                                userData.put("soyad", soyad);
                                userData.put("email", email);
                                userData.put("uid", user.getUid());


                                firestore.collection("Kullanicilar")
                                        .document(user.getUid())
                                        .set(userData)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(KayitActivity.this, GirisActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Firestore kaydı hatası: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            }
                        } else {
                            Toast.makeText(this, "Kayıt başarısız: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}