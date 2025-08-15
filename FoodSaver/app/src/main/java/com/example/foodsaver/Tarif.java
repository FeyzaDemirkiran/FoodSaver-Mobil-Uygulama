package com.example.foodsaver;
public class Tarif {
    private String yemekAdi;
    private String malzemeler;
    private String yapilis;
    private String gorselUrl;
    private String besinDegeri;
    private String ipuclari;
    private boolean favori;

    public Tarif() {}

    public Tarif(String yemekAdi, String malzemeler, String yapilis,
                 String gorselUrl, String besinDegeri, String ipuclari, boolean favori) {
        this.yemekAdi = yemekAdi;
        this.malzemeler = malzemeler;
        this.yapilis = yapilis;
        this.gorselUrl = gorselUrl;
        this.besinDegeri = besinDegeri;
        this.ipuclari = ipuclari;
        this.favori = favori;
    }

    public String getYemekAdi() {
        return yemekAdi;
    }

    public String getMalzemeler() {
        return malzemeler;
    }

    public String getYapilis() {
        return yapilis;
    }

    public String getGorselUrl() {
        return gorselUrl;
    }

    public String getBesinDegeri() {
        return besinDegeri;
    }

    public String getIpuclari() {
        return ipuclari;
    }

    public boolean isFavori() {
        return favori;
    }

    public void setYemekAdi(String yemekAdi) {
        this.yemekAdi = yemekAdi;
    }

    public void setMalzemeler(String malzemeler) {
        this.malzemeler = malzemeler;
    }

    public void setYapilis(String yapilis) {
        this.yapilis = yapilis;
    }

    public void setGorselUrl(String gorselUrl) {
        this.gorselUrl = gorselUrl;
    }

    public void setBesinDegeri(String besinDegeri) {
        this.besinDegeri = besinDegeri;
    }

    public void setIpuclari(String ipuclari) {
        this.ipuclari = ipuclari;
    }

    public void setFavori(boolean favori) {
        this.favori = favori;
    }
}