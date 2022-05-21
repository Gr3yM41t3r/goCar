package com.example.myapplication.model;

public class Organisation extends UserModel {
    private String organisation;
    private String adress;
    private String zipCode;
    private String city;

    public Organisation(String first_name, String last_name, String birth_day, String phone_number, String account_type, String email, String password, String organisation, String adress, String zipCode, String city) {
        super(first_name, last_name, birth_day, phone_number, account_type, email, password);
        this.organisation = organisation;
        this.adress = adress;
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
