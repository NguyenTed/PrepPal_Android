package com.group5.preppal.ui.profile.teacher;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.ui.TeacherMainActivity;
import com.group5.preppal.ui.auth.LoginActivity;
import com.group5.preppal.ui.profile.ProfileActivity;
import com.group5.preppal.viewmodel.AuthViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TeacherProfileActivity extends AppCompatActivity {
    private ImageView iconProfile;
    private TextView tvProfile;
    private LinearLayout navHome, navProfile, navLogOut;
    private AuthViewModel authViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teacher_drawer_base);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        iconProfile = findViewById(R.id.iconProfile);
        navHome = findViewById(R.id.navHome);
        navProfile =  findViewById( R.id.navProfile);
        navLogOut = findViewById(R.id.navLogOut);
        tvProfile = findViewById(R.id.tvProfile);

        navProfile.setBackgroundColor(Color.parseColor("#FFFFFF"));
        iconProfile.setImageResource(R.drawable.ic_profile_black);
        tvProfile.setTextColor(Color.parseColor("#000000"));

        ViewGroup contentFrame = findViewById(R.id.contentFrame);
        getLayoutInflater().inflate(R.layout.activity_teacher_profile, contentFrame, true);

        findViewById(R.id.btnToggleDrawer).setOnClickListener(v -> {
            DrawerLayout drawer = findViewById(R.id.drawerLayout);
            drawer.openDrawer(GravityCompat.START);
        });

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, TeacherMainActivity.class));
        });

        navLogOut.setOnClickListener(v -> {
            handleLogOut();
        });
    }

    private void handleLogOut() {
        authViewModel.signOut();
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(TeacherProfileActivity.this, LoginActivity.class));
        finish();
    }
}
