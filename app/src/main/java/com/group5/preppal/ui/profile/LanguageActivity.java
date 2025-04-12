package com.group5.preppal.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.preppal.R;
import com.group5.preppal.utils.LanguageUtils;

import java.util.Arrays;
import java.util.List;

public class LanguageActivity extends AppCompatActivity {

    private String selectedLanguage = "vi"; // Ngôn ngữ mặc định

    private static class LanguageItem {
        String code;
        String name;
        int icon;

        LanguageItem(String code, String name, int icon) {
            this.code = code;
            this.name = name;
            this.icon = icon;
        }
    }

    private List<LanguageItem> languageItems = Arrays.asList(
            new LanguageItem("vi", "Tiếng Việt", R.drawable.ic_vn_flag),
            new LanguageItem("en", "English", R.drawable.ic_uk_flag)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        selectedLanguage = LanguageUtils.getSavedLanguage(this); // load ngôn ngữ đã chọn

        LinearLayout languageList = findViewById(R.id.languageList);

        for (LanguageItem item : languageItems) {
            View view = LayoutInflater.from(this).inflate(R.layout.language_item, languageList, false); // Sửa: dùng 'false' thay vì 'null'
            TextView name = view.findViewById(R.id.languageName);
            ImageView flag = view.findViewById(R.id.flagIcon);
            ImageView check = view.findViewById(R.id.checkIcon);

            name.setText(item.name);
            flag.setImageResource(item.icon);

            if (item.code.equals(selectedLanguage)) {
                view.setSelected(true);
                check.setVisibility(View.VISIBLE);
            } else {
                view.setSelected(false);
                check.setVisibility(View.GONE);
            }

            view.setOnClickListener(v -> {
                selectedLanguage = item.code;

                // Reset toàn bộ view con
                for (int i = 0; i < languageList.getChildCount(); i++) {
                    View child = languageList.getChildAt(i);
                    child.setSelected(false); // selector background
                    ImageView icon = child.findViewById(R.id.checkIcon);
                    icon.setVisibility(View.GONE);
                }

                // Set view hiện tại
                v.setSelected(true);
                ImageView icon = v.findViewById(R.id.checkIcon);
                icon.setVisibility(View.VISIBLE);
            });

            languageList.addView(view);
        }

        findViewById(R.id.btnUpdateLanguage).setOnClickListener(v -> {
            LanguageUtils.saveLanguage(this, selectedLanguage);
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        String lang = LanguageUtils.getSavedLanguage(newBase);
        Context context = LanguageUtils.setLocale(newBase, lang);
        super.attachBaseContext(context);
    }
}
