package com.hnu.entity;
public class User {
    private long id;
    private String password;
    private int gender;
    private String realName;
    private int userType;
    private String username;
    public User() {
    }
    public User(long id,String username,String password,
                int gender, String realname, int userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.realName = realname;
        this.userType = userType;
    }
    public long getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }
    public int getGender() {
        return gender;
    }
    public String getRealName() {
        return realName;
    }
    public int getUserType() {
        return userType;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public void setRealName(String realname) {
        this.realName = realname;
    }
    public void setUserType(int userType) {
        this.userType = userType;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
