package com.group5.preppal.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.group5.preppal.R;
import com.group5.preppal.ui.auth.LoginActivity;

public class AdminMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();

            int itemId = item.getItemId();

            if (itemId == R.id.nav_manage_tests) {
                Toast.makeText(AdminMainActivity.this, "Manage Tests clicked", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(AdminMainActivity.this, ManageTestsActivity.class));
                return true;

            } else if (itemId == R.id.nav_manage_teacher_accounts) {
                Toast.makeText(AdminMainActivity.this, "Manage Flashcards clicked", Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(AdminMainActivity.this, ManageTeachersActivity.class));
                return true;

            } else if (itemId == R.id.nav_logout) {
                Toast.makeText(AdminMainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                 FirebaseAuth.getInstance().signOut();
                 startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));
                 finish();
                return true;
            }

            // Default case
            Toast.makeText(AdminMainActivity.this, "Unknown option", Toast.LENGTH_SHORT).show();
            return false;
        });
    }
}
