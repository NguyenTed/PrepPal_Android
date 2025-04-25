package com.group5.preppal.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.group5.preppal.R;

public class ShowToast {

    public enum ToastType {
        SUCCESS, ERROR, WARNING
    }

    public static void show(Context context, String message, ToastType type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView toastText = layout.findViewById(R.id.toastText);
        ImageView toastIcon = layout.findViewById(R.id.toastIcon);
        View toastLayout = layout.findViewById(R.id.toastLayout);

        toastText.setText(message);

        // Tuỳ chỉnh background + icon theo loại
        switch (type) {
            case SUCCESS:
                toastLayout.setBackgroundResource(R.drawable.toast_success_background);
                toastIcon.setImageResource(R.drawable.bee_blue_bg);
                break;
            case ERROR:
                toastLayout.setBackgroundResource(R.drawable.toast_error_background);
                toastIcon.setImageResource(R.drawable.ic_error);
                break;
            case WARNING:
                toastLayout.setBackgroundResource(R.drawable.toast_warning_background);
                toastIcon.setImageResource(R.drawable.ic_warning);
                break;
        }

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 100);
        toast.setView(layout);
        toast.show();
    }
}
