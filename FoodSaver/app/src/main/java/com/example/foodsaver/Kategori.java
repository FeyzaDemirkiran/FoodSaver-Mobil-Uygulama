package com.example.foodsaver;

public class Kategori {
    private String ad;
    private String resimUrl;
    private String renkKodu;

    public Kategori() {}

    public Kategori(String ad, String resimUrl, String renkKodu) {
        this.ad = ad;
        this.resimUrl = resimUrl;
        this.renkKodu = renkKodu;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getResimUrl() {
        return resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public String getRenkKodu() {
        return renkKodu;
    }

    public void setRenkKodu(String renkKodu) {
        this.renkKodu = renkKodu;
    }
}