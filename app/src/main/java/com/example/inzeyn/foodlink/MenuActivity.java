package com.example.inzeyn.foodlink;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.inzeyn.foodlink.Adapters.MenuAdapter;
import com.example.inzeyn.foodlink.Helpers.FirebaseHelper;
import com.example.inzeyn.foodlink.Interfaces.ILoadMenuItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "myTag";
    final List<com.example.inzeyn.foodlink.Models.MenuItem> menuItems = new ArrayList<>();
    MenuAdapter menuAdapter;
    private FirebaseFirestore mFirestore;
    private DatabaseReference mDatabase;
    private FirebaseHelper mDBHelper;
    private Button FinalBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
               }

        });

        //init db
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDBHelper = new FirebaseHelper(mDatabase);
        mFirestore = FirebaseFirestore.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // mockData();
        realData();
        RecyclerView recycler= findViewById(R.id.recyclerMenu);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(recycler,this, menuItems);
        recycler.setAdapter(menuAdapter);

        if(menuItems.isEmpty()) {
            menuItems.add(null);
            menuAdapter.notifyItemInserted(menuItems.size()-1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    menuItems.remove(menuItems.get(0));
                    menuAdapter.notifyItemRemoved(menuItems.size());
                    menuAdapter.notifyDataSetChanged();
                    menuAdapter.setLoaded();
                }
            },5000);
        }else {
            Toast.makeText(MenuActivity.this,"Real Data complete", Toast.LENGTH_SHORT).show();
        }

       /* menuAdapter.setLoadMenuItem(new ILoadMenuItem() {
            @Override
            public void onLoadMenuITem() {
                if(menuItems.size() <= 50) {
                    menuItems.add(null);
                    menuAdapter.notifyItemInserted(menuItems.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menuItems.remove(menuItems.size()-1);
                            menuAdapter.notifyItemRemoved(menuItems.size());
                            //random more data
                            int index = menuItems.size();
                            int end = index+5;
                            for(int i=index;i<end;i++) {
                                String title = "Here goes another name" + i;
                                com.example.inzeyn.foodlink.Models.MenuItem menuItem = new com.example.inzeyn.foodlink.Models.MenuItem(title, title.length());
                                menuItems.add(menuItem);
                            }
                            menuAdapter.notifyDataSetChanged();
                            menuAdapter.setLoaded();
                        }
                    },2000);
                }else {
                    Toast.makeText(MenuActivity.this,"Load data complete", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    public void realData() {
        mFirestore.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                StringBuilder title = new StringBuilder();
                                StringBuilder price = new StringBuilder();
                                title.append(document.get("title"));
                                price.append(document.get("price"));
                                com.example.inzeyn.foodlink.Models.MenuItem menuItem = new com.example.inzeyn.foodlink.Models.MenuItem(title.toString(), price.toString());
                                //com.example.inzeyn.foodlink.Models.MenuItem menuItem = new com.example.inzeyn.foodlink.Models.MenuItem("test", 10);

                                menuItems.add(menuItem);
                                //break;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void mockData() {
        for(int i=0;i<10;i++) {
            String title = "Pizza " + i;
            com.example.inzeyn.foodlink.Models.MenuItem menuItem = new com.example.inzeyn.foodlink.Models.MenuItem(title, title);
            menuItems.add(menuItem);
       }
    }

    public void makeToast() {
        FinalBtn = findViewById(R.id.menuFinalBtn);
        FinalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FinalBtn.getContext(), "Added" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_signout) {
            mAuth.getInstance().signOut();
            Intent myIntent = new Intent(MenuActivity.this, SigninActivity.class);
            MenuActivity.this.startActivity(myIntent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
