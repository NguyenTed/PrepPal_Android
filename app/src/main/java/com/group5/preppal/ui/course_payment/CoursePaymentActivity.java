package com.group5.preppal.ui.course_payment;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.group5.preppal.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CoursePaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_payment);
    }
}
