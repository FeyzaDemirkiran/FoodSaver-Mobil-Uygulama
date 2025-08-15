package com.example.foodsaver;



public class Product {
    private String ad;
    private String resimUrl;
    private String muhafaza;

    public Product() {}

    public Product(String ad, String resimUrl, String muhafaza) {
        this.ad = ad;
        this.resimUrl = resimUrl;
        this.muhafaza = muhafaza;
    }

    public String getAd() { return ad; }
    public String getResimUrl() { return resimUrl; }
    public String getMuhafaza() { return muhafaza; }

    public void setAd(String ad) { this.ad = ad; }
    public void setResimUrl(String resimUrl) { this.resimUrl = resimUrl; }
    public void setMuhafaza(String muhafaza) { this.muhafaza = muhafaza; }
}