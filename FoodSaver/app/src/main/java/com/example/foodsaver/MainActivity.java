package com.example.foodsaver;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvAppTitle;
    private Button btnGiris, btnKayit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tvAppTitle = findViewById(R.id.tv_app_title);
        btnGiris = findViewById(R.id.btn_giris);
        btnKayit = findViewById(R.id.btn_kayit);

        Typeface customFont = ResourcesCompat.getFont(this, R.font.caprilo);
        if (customFont != null) {
            tvAppTitle.setTypeface(customFont);
        }

        btnGiris.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GirisActivity.class);
            startActivity(intent);
        });

        btnKayit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KayitActivity.class);
            startActivity(intent);
        });
    }
}