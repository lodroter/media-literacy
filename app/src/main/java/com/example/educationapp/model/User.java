package com.example.educationapp.model;

public class User {

    private Integer id;
    private String username;
    private String password;
    private String mail;
    private Integer statistics_id;
    private String notifications;

    public User(String user, String pass, String email, int stats_id, String notif){
        this.username = user;
        this.password = pass;
        this.mail = email;
        this.statistics_id = stats_id;
        this.notifications = notif;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getStatistics_id() {
        return statistics_id;
    }

    public void setStatistics_id(Integer statistics_id) {
        this.statistics_id = statistics_id;
    }

    public String isNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }
}
