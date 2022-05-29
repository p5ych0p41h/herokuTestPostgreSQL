package com.example.herokutestpostgresql.entity;

public class User {
    private Integer id;
    private String login;
    private String password;
    private String email;

    public User() {
    }

    public User(int id, String password, String email) {
        this.id = id;
        this.login = email;
        this.password = password;
        this.email = email;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
