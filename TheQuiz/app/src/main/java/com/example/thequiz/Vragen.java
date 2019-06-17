package com.example.thequiz;

public class Vragen {
    public static final String MOEILIJKHEID_MAK = "Makkelijk";
    public static final String MOEILIJKHEID_GEM = "Gemiddeld";
    public static final String MOEILIJKHEID_MOE = "Moeilijk";

    private int id;
    private String vraag;
    private String optie1;
    private String optie2;
    private String optie3;
    private int antwoordNr;
    private String moeilijkheid;
    private int categorieID;

    public Vragen() {
    }

    public Vragen(String vraag, String optie1, String optie2, String optie3, int antwoordNr, String moeilijkheid, int categorieID) {
        this.vraag = vraag;
        this.optie1 = optie1;
        this.optie2 = optie2;
        this.optie3 = optie3;
        this.antwoordNr = antwoordNr;
        this.moeilijkheid = moeilijkheid;
        this.categorieID = categorieID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVraag() {
        return vraag;
    }

    public void setVraag(String vraag) {
        this.vraag = vraag;
    }

    public String getOptie1() {
        return optie1;
    }

    public void setOptie1(String optie1) {
        this.optie1 = optie1;
    }

    public String getOptie2() {
        return optie2;
    }

    public void setOptie2(String optie2) {
        this.optie2 = optie2;
    }

    public String getOptie3() {
        return optie3;
    }

    public void setOptie3(String optie3) {
        this.optie3 = optie3;
    }

    public int getAntwoordNr() {
        return antwoordNr;
    }

    public void setAntwoordNr(int antwoordNr) {
        this.antwoordNr = antwoordNr;
    }

    public String getMoeilijkheid() {
        return moeilijkheid;
    }

    public void setMoeilijkheid(String moeilijkheid) {
        this.moeilijkheid = moeilijkheid;
    }

    public int getCategorieID() {
        return categorieID;
    }

    public void setCategorieID(int categorieID) {
        this.categorieID = categorieID;
    }

    public static String[] getAllMoeilijkheden() {
        return new String[]{
                MOEILIJKHEID_MAK,
                MOEILIJKHEID_GEM,
                MOEILIJKHEID_MOE
        };
    }
}
