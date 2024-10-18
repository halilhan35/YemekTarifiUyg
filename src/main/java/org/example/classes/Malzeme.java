package org.example.classes;

import java.math.BigDecimal;

public class Malzeme {

    private int malzemeID;
    private String malzemeAdi;
    private String toplamMiktar;
    private String malzemeBirim;
    private Double malzemeFiyat;
    private String malzemeFotoYolu;


    public Malzeme(int malzemeID, String malzemeAdi, String toplamMiktar, String malzemeBirim, Double malzemeFiyat, String malzemeFotoYolu){
        this.malzemeID = malzemeID;
        this.malzemeAdi = malzemeAdi;
        this.toplamMiktar = toplamMiktar;
        this.malzemeBirim = malzemeBirim;
        this.malzemeFiyat = malzemeFiyat;
        this.malzemeFotoYolu = malzemeFotoYolu;
    }

    public int getMalzemeID() {
        return malzemeID;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public String getToplamMiktar() {
        return toplamMiktar;
    }

    public String getMalzemeBirim() {
        return malzemeBirim;
    }

    public Double getMalzemeFiyat() {
        return malzemeFiyat;
    }

    public String getMalzemeFotoYolu() {
        return malzemeFotoYolu;
    }

    public void setMalzemeID(int malzemeID) {
        this.malzemeID = malzemeID;
    }

    public void setMalzemeAdi(String malzemeAdi) {
        this.malzemeAdi = malzemeAdi;
    }

    public void setToplamMiktar(String toplamMiktar) {
        this.toplamMiktar = toplamMiktar;
    }

    public void setMalzemeBirim(String malzemeBirim) {
        this.malzemeBirim = malzemeBirim;
    }

    public void setMalzemeFiyat(Double malzemeFiyat) {
        this.malzemeFiyat = malzemeFiyat;
    }

    public void setMalzemeFotoYolu(String malzemeFotoYolu) {
        this.malzemeFotoYolu = malzemeFotoYolu;
    }

}
