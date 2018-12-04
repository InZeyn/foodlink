package com.example.inzeyn.foodlink.Adapters;

public class UserAdapter {

    private String userName, name, email, id;

    public UserAdapter() {
    }

    public UserAdapter(String userName, String name, String email, String id) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.id = id;
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

    public String getId() {return id;}
}
