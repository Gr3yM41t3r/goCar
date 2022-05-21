package com.example.myapplication.model;

public class Organisation extends UserModel {
    private String nomOrganisation;
    private String adresse;
    private String zipCode;
    private String ville;


    public Organisation(String first_name, String last_name, String birth_day, String phone_number, String account_type, String email, String password, String nomOrganisation, String adresse, String zipCode, String ville) {
        super(first_name, last_name, birth_day, phone_number, account_type, email, password);
        this.nomOrganisation = nomOrganisation;
        this.adresse = adresse;
        this.zipCode = zipCode;
        this.ville = ville;
    }

    public String getNomOrganisation() {
        return nomOrganisation;
    }

    public void setNomOrganisation(String nomOrganisation) {
        this.nomOrganisation = nomOrganisation;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}
