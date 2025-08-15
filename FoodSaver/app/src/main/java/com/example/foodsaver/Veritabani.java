package com.example.foodsaver;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class Veritabani extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "urunler.db";
    private static final int DATABASE_VERSION = 2;

    public Veritabani(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE urunler (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "barkod TEXT, " +
                "urunAdi TEXT, " +
                "sonTarih TEXT, " +
                "uyariGunu INTEGER, " +
                "adet INTEGER, " +
                "kategori TEXT, " +
                "resim BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS urunler");
        onCreate(db);
    }


    public void urunEkle(String barkod, String urunAdi, String sonTarih, int uyariGunu, int adet, String kategori, byte[] resim) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("barkod", barkod);
        values.put("urunAdi", urunAdi);
        values.put("sonTarih", sonTarih);
        values.put("uyariGunu", uyariGunu);
        values.put("adet", adet);
        values.put("kategori", kategori);
        values.put("resim", resim);
        db.insert("urunler", null, values);
        db.close();
    }


    public Cursor tumUrunleriGetir() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM urunler", null);
    }


    public void urunSil(String barkod) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("urunler", "barkod=?", new String[]{barkod});
        db.close();
    }


    public List<Urun> kategoriyeGoreUrunleriGetir(String kategori) {
        List<Urun> urunList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM urunler WHERE kategori = ?", new String[]{kategori});

        if (cursor.moveToFirst()) {
            do {

                int urunAdiIndex = cursor.getColumnIndex("urunAdi");
                int sonTarihIndex = cursor.getColumnIndex("sonTarih");
                int resimIndex = cursor.getColumnIndex("resim");
                int barkodIndex = cursor.getColumnIndex("barkod");
                int kategoriIndex = cursor.getColumnIndex("kategori");
                int adetIndex = cursor.getColumnIndex("adet");

                if (urunAdiIndex != -1 && sonTarihIndex != -1 && resimIndex != -1) {
                    @SuppressLint("Range") Urun urun = new Urun(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(urunAdiIndex),
                            cursor.getString(sonTarihIndex),
                            cursor.getBlob(resimIndex),
                            cursor.getString(barkodIndex),
                            cursor.getString(kategoriIndex),
                            cursor.getInt(adetIndex)
                    );
                    urunList.add(urun);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return urunList;
    }
}

