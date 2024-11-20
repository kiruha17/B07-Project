package com.example.b07group57.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    private String email;
    private String name;
    private String hashedPassword;
    private final String userId;

    public User(String email, String name, String password, String userId) {
        this.email = email;
        this.name = name;
        this.hashedPassword = password;
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return hashedPassword;
    }

    public void setPassword(String password) {
        this.hashedPassword = password;
    }
    public void storeUserData() {
        FirebaseDatabase database = FirebaseDatabase
                .getInstance("https://b07-group-57-default-rtdb.firebaseio.com/");

        // Store user data under the "Users/userId" node
        database.getReference("Users").child(userId).setValue(this);
    }

}