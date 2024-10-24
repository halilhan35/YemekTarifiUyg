package org.example.classes;

public class Tarif {

    private int tarifID;
    private String tarifAdi;
    private String kategori;
    private int hazirlamaSuresi;
    private String talimatlar;
    private String tarifFotoYolu;
    private double eslesmeYuzdesi = 0.00;

    public Tarif(int tarifID, String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar, String tarifFotoYolu){
        this.tarifID = tarifID;
        this.tarifAdi = tarifAdi;
        this.kategori = kategori;
        this.hazirlamaSuresi = hazirlamaSuresi;
        this.talimatlar=talimatlar;
        this.tarifFotoYolu = tarifFotoYolu;
    }

    public int getTarifID() {
        return tarifID;
    }

    public String getTarifAdi() {
        return tarifAdi;
    }

    public String getKategori() {
        return kategori;
    }

    public int getHazirlamaSuresi() {
        return hazirlamaSuresi;
    }

    public String getTalimatlar() {
        return talimatlar;
    }

    public double getEslesmeYuzdesi() {
        return eslesmeYuzdesi;
    }

    public String getTarifFotoYolu() {
        return tarifFotoYolu;
    }

    public void setTarifID(int tarifID) {
        this.tarifID = tarifID;
    }

    public void setTarifAdi(String tarifAdi) {
        this.tarifAdi = tarifAdi;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setHazirlamaSuresi(int hazirlamaSuresi) {
        this.hazirlamaSuresi = hazirlamaSuresi;
    }

    public void setTalimatlar(String talimatlar) {
        this.talimatlar = talimatlar;
    }

    public void setTarifFotoYolu(String tarifFotoYolu) {
        this.tarifFotoYolu = tarifFotoYolu;
    }

    public void setEslesmeYuzdesi(double eslesmeYuzdesi) {
        this.eslesmeYuzdesi = eslesmeYuzdesi;
    }
}
