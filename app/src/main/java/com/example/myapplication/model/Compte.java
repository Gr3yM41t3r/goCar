package com.example.myapplication.model;

public class Compte {
    private String email;
    private String password;

    public Compte(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return password;
    }

    public void setMotDePasse(String motDePasse) {
        this.password = motDePasse;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
