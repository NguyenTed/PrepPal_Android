package com.group5.preppal.ui.profile.teacher;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import com.group5.preppal.R;
import com.group5.preppal.ui.TeacherMainActivity;

public class TeacherProfileActivity extends AppCompatActivity {
    private ImageView iconProfile;
    private TextView tvProfile;
    private LinearLayout navHome, navProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teacher_drawer_base);

        iconProfile = findViewById(R.id.iconProfile);
        navHome = findViewById(R.id.navHome);
        navProfile =  findViewById( R.id.navProfile);
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
    }
}
