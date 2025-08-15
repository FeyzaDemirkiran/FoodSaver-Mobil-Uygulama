package com.example.foodsaver;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class GirisActivity extends AppCompatActivity {

    private EditText editEmailGiris, editSifreGiris;
    private Button btnGiris;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        editEmailGiris = findViewById(R.id.editEmailGiris);
        editSifreGiris = findViewById(R.id.editSifreGiris);
        btnGiris = findViewById(R.id.btnGiris);
        mAuth = FirebaseAuth.getInstance();

        btnGiris.setOnClickListener(v -> {
            String email = editEmailGiris.getText().toString().trim();
            String sifre = editSifreGiris.getText().toString().trim();

            if (email.isEmpty() || sifre.isEmpty()) {
                Toast.makeText(GirisActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, sifre)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(GirisActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(GirisActivity.this, AnasayfaActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(GirisActivity.this, "Giriş başarısız: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}