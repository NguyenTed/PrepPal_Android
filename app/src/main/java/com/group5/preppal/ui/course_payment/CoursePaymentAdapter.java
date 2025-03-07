package com.group5.preppal.ui.course_payment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Course;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.data.repository.UserRepository;
import com.group5.preppal.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class CoursePaymentAdapter extends RecyclerView.Adapter<CoursePaymentAdapter.CourseViewHolder> {
    private List<Course> courseList = new ArrayList<>();
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final Context context;

    public CoursePaymentAdapter(Context context, AuthRepository authRepository, UserRepository userRepository) {
        this.context = context;
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    public void setCourses(List<Course> courses) {
        this.courseList = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_payment, parent, false);
        return new CourseViewHolder(view, context, authRepository, userRepository);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseName.setText("IELTS package practice " + Math.round((course.getTargetLevel() - course.getEntryLevel()) * 2) + " months");
        holder.entryLevel.setText("Entry Level: " + course.getEntryLevel() + " - " + (course.getEntryLevel() + 0.5));
        holder.targetLevel.setText("Target Level: " + (course.getTargetLevel() == 6.5 ? course.getTargetLevel() +  " - " + "7.0+" : course.getTargetLevel() + "+"));
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, entryLevel, targetLevel;
        Button payBtn;
        private final Context context;
        private final AuthRepository authRepository;
        private final UserRepository userRepository;

        public CourseViewHolder(@NonNull View itemView, Context context, AuthRepository authRepository, UserRepository userRepository) {
            super(itemView);
            this.context = context;
            this.authRepository = authRepository;

            courseName = itemView.findViewById(R.id.courseName);
            entryLevel = itemView.findViewById(R.id.entryLevel);
            targetLevel = itemView.findViewById(R.id.targetLevel);

            payBtn = itemView.findViewById(R.id.payBtn);
            this.userRepository = userRepository;
        }

        public void bind(Course course) {
            courseName.setText("IELTS package practice " + Math.round((course.getTargetLevel() - course.getEntryLevel()) * 2) + " months");
            entryLevel.setText("Entry Level: " + course.getEntryLevel() + " - " + (course.getEntryLevel() + 0.5));
            targetLevel.setText("Target Level: " + (course.getTargetLevel() == 6.5 ? course.getTargetLevel() +  " - " + "7.0+" : course.getTargetLevel() + "+"));

            payBtn.setOnClickListener(v -> {
                String courseId = course.getCourseId();
                Toast.makeText(context, "Buy course successfully", Toast.LENGTH_SHORT).show();

                FirebaseUser user = authRepository.getCurrentUser();
                if (user != null) {
                    userRepository.addCourseToStudent(user.getUid(), courseId);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "User not logged in!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
