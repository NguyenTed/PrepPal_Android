package com.group5.preppal.ui.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource; // ✅ THÊM IMPORT NÀY
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.group5.preppal.R;

public class WritingTestActivity extends AppCompatActivity {
    private TextView tvTitle, tvQuestion;
    private EditText etAnswer;
    private Button btnSubmit;
    private ImageView btnBack, imgQuestion;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_test);

        tvTitle = findViewById(R.id.tvTitle);
        tvQuestion = findViewById(R.id.tvQuestion);
        etAnswer = findViewById(R.id.etAnswer);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        imgQuestion = findViewById(R.id.imgQuestion);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String imgUrl = intent.getStringExtra("imgUrl");

            Log.d("WritingTestActivity", "Image URL: " + imgUrl);

            tvTitle.setText(title);
            tvQuestion.setText(description);

            if (imgUrl != null && !imgUrl.isEmpty()) {
                imgQuestion.setVisibility(View.VISIBLE);

                Glide.with(this)
                        .load(imgUrl)
                        .placeholder(R.drawable.loading)
                        .error(new ColorDrawable(Color.TRANSPARENT))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                imgQuestion.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                imgQuestion.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(imgQuestion);
            } else {
                imgQuestion.setVisibility(View.GONE);
            }
        }

        btnBack.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> {
            String answer = etAnswer.getText().toString().trim();
            if (answer.isEmpty()) {
                Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Answer submitted successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
