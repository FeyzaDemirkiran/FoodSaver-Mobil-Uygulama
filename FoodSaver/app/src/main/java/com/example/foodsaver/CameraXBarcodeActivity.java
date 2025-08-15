package com.example.foodsaver;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.animation.ValueAnimator;


import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CameraXBarcodeActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private PreviewView previewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_xbarcode);



        View barcodeScannerLine = findViewById(R.id.barcodeScannerLine);
        previewView = findViewById(R.id.viewFinder);



        previewView.post(() -> {
            int centerX = previewView.getWidth() / 2;
            int centerY = previewView.getHeight() / 2;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) barcodeScannerLine.getLayoutParams();
            params.leftMargin = centerX - barcodeScannerLine.getWidth() / 2;
            params.topMargin = centerY;
            barcodeScannerLine.setLayoutParams(params);
        });



        startScannerAnimation(barcodeScannerLine);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void startCamera() {

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();


                Preview preview = new Preview.Builder().build();


                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
                    try {
                        if (imageProxy.getImage() != null) {
                            InputImage inputImage = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());
                            BarcodeScanner barcodeScanner = BarcodeScanning.getClient();
                            barcodeScanner.process(inputImage)
                                    .addOnSuccessListener(barcodes -> {
                                        if (!barcodes.isEmpty()) {
                                            Barcode barcode = barcodes.get(0);
                                            String barcodeResult = barcode.getDisplayValue();


                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("BARKOD_SONUCU", barcodeResult);
                                            setResult(RESULT_OK, resultIntent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(CameraXBarcodeActivity.this, "Barkod okuma hatası", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnCompleteListener(task -> imageProxy.close());
                        } else {
                            Toast.makeText(CameraXBarcodeActivity.this, "Görüntü alınamadı", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();


                Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis);


                preview.setSurfaceProvider(previewView.getSurfaceProvider());


                Log.d("Kamera", "Kamera başlatıldı.");

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }


    private void startScannerAnimation(View barcodeScannerLine) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            barcodeScannerLine.setTranslationY(value);
        });
        animator.start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Kamera izni gerekli", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
