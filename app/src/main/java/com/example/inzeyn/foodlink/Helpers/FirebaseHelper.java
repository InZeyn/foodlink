package com.example.inzeyn.foodlink.Helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.inzeyn.foodlink.Models.MenuItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FirebaseHelper {
    DatabaseReference db;
    Boolean saved;
    ArrayList<MenuItem> menuItems = new ArrayList<>();

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    public Boolean getSaved(MenuItem menuItem) {
        if(menuItem==null){
            saved = false;
        }else {
            try {
                db.child("categories").push().setValue(menuItem);
                saved = true;
            }catch (DatabaseException e){
                e.printStackTrace();
                saved = false;
            }
        }
        return saved;
    }

    private void fetchData(DataSnapshot dataSnapshot) {
        menuItems.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            MenuItem menuItem = ds.getValue(MenuItem.class);
            menuItems.add(menuItem);
        }
    }

    public ArrayList<MenuItem> getMenuItems() {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return menuItems;
    }
}
