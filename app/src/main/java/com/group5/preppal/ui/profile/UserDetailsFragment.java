package com.group5.preppal.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group5.preppal.R;

public class UserDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Liên kết giao diện với fragment
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        TextView phoneTextView = view.findViewById(R.id.phoneTextView);

        // Gán thông tin người dùng
        phoneTextView.setText("Phone: 0123456789");

        return view;
    }
}
