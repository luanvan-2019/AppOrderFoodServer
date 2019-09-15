package com.hcmunre.apporderfoodserver.models.Database;

public class User {
    private String tennd;
    private String sodienthoai;
    private String email;
    private String password;
    public User(){
    }

    public User( String tennd, String sodienthoai, String email, String password) {
        this.tennd = tennd;
        this.sodienthoai = sodienthoai;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getTennd() {
        return tennd;
    }

    public void setTennd(String tennd) {
        this.tennd = tennd;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
