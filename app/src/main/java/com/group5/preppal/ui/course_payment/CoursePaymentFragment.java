package com.group5.preppal.ui.course_payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.preppal.R;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.data.repository.UserRepository;
import com.group5.preppal.ui.MainActivity;
import com.group5.preppal.viewmodel.CourseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CoursePaymentFragment extends Fragment {
    private RecyclerView recyclerView;
    private CoursePaymentAdapter coursePaymentAdapter;
    private CourseViewModel courseViewModel;
    private ImageButton backBtn;

    @Inject
    AuthRepository authRepository;
    @Inject
    UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_payment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        backBtn = view.findViewById(R.id.backButton);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        coursePaymentAdapter = new CoursePaymentAdapter(requireContext(), authRepository, userRepository);
        recyclerView.setAdapter(coursePaymentAdapter);

        // ✅ Lấy ViewModel từ ViewModelProvider
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        courseViewModel.getFilteredCourses().observe(getViewLifecycleOwner(), courses -> {
            if (courses != null && !courses.isEmpty()) {
                coursePaymentAdapter.setCourses(courses);
            } else {
            }
        });

        return view;
    }
}
