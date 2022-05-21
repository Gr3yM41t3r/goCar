package com.example.myapplication.model;

public class UserModel {
    private String first_name;
    private String last_name;
    private String birth_day;
    private String phone_number;
    private String account_type;
    private String email;
    private String password;


    public UserModel(String first_name, String last_name, String birth_day, String phone_number, String account_type, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_day = birth_day;
        this.phone_number = phone_number;
        this.account_type = account_type;
        this.email = email;
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
