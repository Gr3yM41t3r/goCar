package com.example.myapplication.model;

public class Favorites {

    private Long idfavorites;
    private String idadvert;
    private String iduser;

    public Favorites( String idadvert, String iduser) {
        this.idadvert = idadvert;
        this.iduser = iduser;
    }

    public Long getIdfavorites() {
        return idfavorites;
    }

    public void setIdfavorites(Long idfavorites) {
        this.idfavorites = idfavorites;
    }

    public String getIdadvert() {
        return idadvert;
    }

    public void setIdadvert(String idadvert) {
        this.idadvert = idadvert;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }
}