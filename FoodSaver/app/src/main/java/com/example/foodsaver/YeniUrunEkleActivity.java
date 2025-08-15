package com.example.foodsaver;



import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class YeniUrunEkleActivity extends BaseActivity {

    private static final int CAMERA_REQUEST = 101;
    private static final int BARCODE_SCAN_REQUEST = 102;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 1001;

    private ImageButton imageButton, imageButtonScan;
    private EditText editTextBarkod, editTextUrunAdi, editTextSonTarih, editTextUyariGunu, editTextAdet;
    private ImageButton buttonUyariArttir, buttonUyariAzalt, buttonAdetArttir, buttonAdetAzalt;
    private Button buttonEkle, buttonSorgula;
    private Spinner spinnerKategori;
    private String secilenKategori = "";
    private Veritabani veritabani;
    private Bitmap selectedImage;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> barcodeScanLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_urun_ekle);

        setupBottomNav();
        setupNavigationDrawer(R.layout.activity_yeni_urun_ekle);

        imageButton = findViewById(R.id.imageButton);
        imageButtonScan = findViewById(R.id.imageButtonScan);
        editTextBarkod = findViewById(R.id.textViewBarkod);
        editTextUrunAdi = findViewById(R.id.editTexturun);
        editTextSonTarih = findViewById(R.id.editTexttarih);
        buttonEkle = findViewById(R.id.buttonekle);
        buttonSorgula = findViewById(R.id.buttonsorgula);
        editTextUyariGunu = findViewById(R.id.editTextUyariGunu);
        editTextAdet = findViewById(R.id.editTextAdet);
        buttonUyariArttir = findViewById(R.id.buttonUyariArttir);
        buttonUyariAzalt = findViewById(R.id.buttonUyariAzalt);
        buttonAdetArttir = findViewById(R.id.buttonAdetArttir);
        buttonAdetAzalt = findViewById(R.id.buttonAdetAzalt);
        spinnerKategori = findViewById(R.id.spinnerKategori);

        veritabani = new Veritabani(this);
        List<String> kategoriler = new ArrayList<>();
        kategoriler.add("Kategori Seç");
        kategoriler.add("Süt Ürünleri");
        kategoriler.add("Şarküteri Ürünleri");
        kategoriler.add("Et ve Balık Ürünleri");
        kategoriler.add("Baklagiller ve Tahıllar");
        kategoriler.add("Unlu Mamuller");
        kategoriler.add("Kahvaltılık Ürünler");
        kategoriler.add("Dondurulmuş Gıdalar");
        kategoriler.add("Konserve Ürünler");
        kategoriler.add("Soslar ve Baharatlar");
        kategoriler.add("Atıştırmalıklar");
        kategoriler.add("Tatlılar");
        kategoriler.add("İçecekler");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kategoriler) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    tv.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(adapter);

        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    secilenKategori = kategoriler.get(position);
                } else {
                    secilenKategori = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                secilenKategori = "";
            }
        });

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(this, "Kamera izni reddedildi", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        imageButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "Kamera izni gerekli, lütfen izin verin", Toast.LENGTH_LONG).show();
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                } else {
                    Toast.makeText(this, "Kamera izni kalıcı olarak reddedildi. Ayarlardan açmanız gerekiyor.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            } else {
                openCamera();
            }
        });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            selectedImage = imageBitmap;
                            imageButton.setImageBitmap(selectedImage);
                        }
                    }
                }
        );

        barcodeScanLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String barkodSonucu = result.getData().getStringExtra("BARKOD_SONUCU");
                if (barkodSonucu != null) {
                    editTextBarkod.setText(barkodSonucu);
                    fetchProductNameFromBarcode(barkodSonucu);
                }
            }
        });

        imageButtonScan.setOnClickListener(v -> openCameraForScan());

        buttonEkle.setOnClickListener(v -> {
            String barkod = editTextBarkod.getText().toString();
            String urunAdi = editTextUrunAdi.getText().toString();
            String sonTarih = editTextSonTarih.getText().toString();
            String uyariGunuStr = editTextUyariGunu.getText().toString();
            String adetStr = editTextAdet.getText().toString();
            int adet = adetStr.isEmpty() ? 0 : Integer.parseInt(adetStr);
            int uyariGunu;

            if (barkod.isEmpty() || urunAdi.isEmpty() || sonTarih.isEmpty() || uyariGunuStr.isEmpty() || secilenKategori.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                uyariGunu = Integer.parseInt(uyariGunuStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Uyarı günü geçersiz", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImage == null) {
                Toast.makeText(this, "Lütfen bir fotoğraf seçin", Toast.LENGTH_SHORT).show();
                return;
            }


            AlarmBildirimPlanlayici.planla(this, urunAdi, sonTarih, uyariGunu);
            Log.d("DEBUG", "urunAdi: " + urunAdi + ", sonTarih: " + sonTarih + ", uyariGunu: " + uyariGunu);


            byte[] imageByteArray = bitmapToByteArray(selectedImage);
            veritabani.urunEkle(barkod, urunAdi, sonTarih, uyariGunu, adet, secilenKategori, imageByteArray);
            Toast.makeText(this, "Ürün eklendi", Toast.LENGTH_SHORT).show();
        });

        buttonSorgula.setOnClickListener(v -> startActivity(new Intent(this, UrunlerimActivity.class)));

        buttonUyariAzalt.setOnClickListener(v -> {
            int value = getEditTextValue(editTextUyariGunu);
            editTextUyariGunu.setText(String.valueOf(value + 1));
        });

        buttonUyariArttir.setOnClickListener(v -> {
            int value = getEditTextValue(editTextUyariGunu);
            if (value > 0) {
                editTextUyariGunu.setText(String.valueOf(value - 1));
            }
        });

        buttonAdetAzalt.setOnClickListener(v -> {
            int value = getEditTextValue(editTextAdet);
            editTextAdet.setText(String.valueOf(value + 1));
        });

        buttonAdetArttir.setOnClickListener(v -> {
            int value = getEditTextValue(editTextAdet);
            if (value > 0) {
                editTextAdet.setText(String.valueOf(value - 1));
            }
        });

        editTextSonTarih.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                editTextSonTarih.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bildirim izni verildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bildirim izni reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCameraForScan() {
        Intent intent = new Intent(this, CameraXBarcodeActivity.class);
        barcodeScanLauncher.launch(intent);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

    private int getEditTextValue(EditText editText) {
        String value = editText.getText().toString();
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void fetchProductNameFromBarcode(String barcode) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject product = response.getJSONObject("product");
                        String productName = product.getString("product_name");
                        editTextUrunAdi.setText(productName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Ürün adı alınamadı", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Barkoddan ürün alınamadı", Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}