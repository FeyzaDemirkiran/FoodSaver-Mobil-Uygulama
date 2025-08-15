package com.example.foodsaver;

public class Urun {
    private int id;
    private String urunAdi;
    private String sonTarih;
    private byte[] resim;
    private String barkod;
    private String kategori;
    private int adet;

    public Urun(int id, String urunAdi, String sonTarih, byte[] resim, String barkod, String kategori, int adet) {
        this.id = id;
        this.urunAdi = urunAdi;
        this.sonTarih = sonTarih;
        this.resim = resim;
        this.barkod = barkod;
        this.kategori = kategori;
        this.adet = adet;
    }
    public int getId() {
        return id;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public String getSonTarih() {
        return sonTarih;
    }

    public byte[] getResim() {
        return resim;
    }

    public String getBarkod() {
        return barkod;
    }

    public String getKategori() {
        return kategori;
    }

    public int getAdet() {
        return adet;
    }


}

