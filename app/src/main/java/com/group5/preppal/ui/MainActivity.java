package com.group5.preppal.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private FirestoreTestViewModel testViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

