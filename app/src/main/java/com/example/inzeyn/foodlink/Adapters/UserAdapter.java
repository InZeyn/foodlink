package com.example.inzeyn.foodlink.Adapters;

public class UserAdapter {

    private String userName, name, email;
    public UserAdapter() {
    }

    public UserAdapter(String userName, String name, String email) {
        this.userName = userName;
        this.name = name;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
