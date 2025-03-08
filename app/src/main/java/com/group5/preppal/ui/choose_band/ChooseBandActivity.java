package com.group5.preppal.ui.choose_band;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.group5.preppal.R;
import com.group5.preppal.viewmodel.UserViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChooseBandActivity extends AppCompatActivity {

    private Button btnBand4_0, btnBand5_0, btnBand6_0;
    private Button btnGoal5_0, btnGoal6_0, btnGoal6_5;
    private LinearLayout containerBandGoal;
    private Button saveBtn;

    private double selectedBandLevel = 4.0;
    private double selectedGoalBand = 6.5;

    protected UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_band_score);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        saveBtn = findViewById(R.id.btn_save);

        // Ánh xạ ID từ XML
        btnBand4_0 = findViewById(R.id.btn_band_4_0);
        btnBand5_0 = findViewById(R.id.btn_band_5_0);
        btnBand6_0 = findViewById(R.id.btn_band_6_0);

        btnGoal5_0 = findViewById(R.id.btn_goal_5_0);
        btnGoal6_0 = findViewById(R.id.btn_goal_6_0);
        btnGoal6_5 = findViewById(R.id.btn_goal_6_5);

        containerBandGoal = findViewById(R.id.container_band_goal);

        // Xử lý sự kiện khi nhấn vào các nút Band Level
        btnBand4_0.setOnClickListener(view -> selectBandLevel(4.0));
        btnBand5_0.setOnClickListener(view -> selectBandLevel(5.0));
        btnBand6_0.setOnClickListener(view -> selectBandLevel(6.0));

        btnGoal5_0.setOnClickListener(view -> selectBandGoal(5.0));
        btnGoal6_0.setOnClickListener(view -> selectBandGoal(6.0));
        btnGoal6_5.setOnClickListener(view -> selectBandGoal(6.5));

        saveBtn.setOnClickListener(v -> saveBandToFirebase());

        // Mặc định chọn band level 6.0-6.5 và band goal 6.5-7.0+
        selectBandLevel(4.0);
    }

    private void selectBandLevel(double level) {
        // Reset tất cả về màu mặc định
        resetBandButtons();
        selectedBandLevel = level;

        if (level == 4.0) {
            btnBand4_0.setBackgroundResource(R.drawable.rounded_1000dp_white);
            btnBand4_0.setTextColor(ContextCompat.getColor(this, R.color.black));

            btnGoal5_0.setVisibility(View.VISIBLE);
            btnGoal6_0.setVisibility(View.VISIBLE);
            btnGoal6_5.setVisibility(View.VISIBLE);
        }
        else if (level == 5.0) {
            btnBand5_0.setBackgroundResource(R.drawable.rounded_1000dp_white);
            btnBand5_0.setTextColor(ContextCompat.getColor(this, R.color.black));

            btnGoal5_0.setVisibility(View.GONE);
            btnGoal6_0.setVisibility(View.VISIBLE);
            btnGoal6_5.setVisibility(View.VISIBLE);
        }
        else if (level == 6.0) {
            btnBand6_0.setBackgroundResource(R.drawable.rounded_1000dp_white);
            btnBand6_0.setTextColor(ContextCompat.getColor(this, R.color.black));

            btnGoal5_0.setVisibility(View.GONE);
            btnGoal6_0.setVisibility(View.GONE);
            btnGoal6_5.setVisibility(View.VISIBLE);
        }

        // Mặc định chọn band goal là 6.5-7.0+
        resetBandGoals();
        selectBandGoal(6.5);
    }

    private void selectBandGoal(double level) {
        resetBandGoals();
        selectedGoalBand = level;

        if (level == 5.0) {
            btnGoal5_0.setBackgroundResource(R.drawable.rounded_1000dp_white);
            btnGoal5_0.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        else if (level == 6.0) {
            btnGoal6_0.setBackgroundResource(R.drawable.rounded_1000dp_white);
            btnGoal6_0.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        else if (level == 6.5) {
            btnGoal6_5.setBackgroundResource(R.drawable.rounded_1000dp_white);
            btnGoal6_5.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

    }

    private void resetBandButtons() {
        btnBand4_0.setBackgroundResource(R.drawable.rounded_button_1000dp_gray);
        btnBand5_0.setBackgroundResource(R.drawable.rounded_button_1000dp_gray);
        btnBand6_0.setBackgroundResource(R.drawable.rounded_button_1000dp_gray);
        btnBand4_0.setTextColor(Color.parseColor("#6B7280"));
        btnBand5_0.setTextColor(Color.parseColor("#6B7280"));
        btnBand6_0.setTextColor(Color.parseColor("#6B7280"));
    }

    private void resetBandGoals() {
        btnGoal5_0.setBackgroundResource(R.drawable.rounded_button_1000dp_gray);
        btnGoal6_0.setBackgroundResource(R.drawable.rounded_button_1000dp_gray);
        btnGoal6_5.setBackgroundResource(R.drawable.rounded_button_1000dp_gray);
        btnGoal5_0.setTextColor(Color.parseColor("#6B7280"));
        btnGoal6_0.setTextColor(Color.parseColor("#6B7280"));
        btnGoal6_5.setTextColor(Color.parseColor("#6B7280"));
    }

    private void saveBandToFirebase() {
        userViewModel.updateUserBand(selectedBandLevel, selectedGoalBand).observe(this, success -> {
//            if (success) {
//                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
//            }
        });
    }
}
