package com.group5.preppal.ui.course;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private final List<Course> courseList;
    private final OnEnrollClickListener listener;
    private final Context context;

    public interface OnEnrollClickListener {
        void onEnrollClick(Course course);
    }

    public CourseAdapter(Context context, List<Course> courseList, OnEnrollClickListener listener) {
        this.context = context;
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.txtCourseName.setText(course.getName());
        holder.txtCourseEntryLevel.setText("Entry Level: " + course.getEntryLevel() + " - " + (course.getEntryLevel() + 0.5));
        holder.txtCourseTargetLevel.setText("Target Level: " + (course.getTargetLevel() == 6.5 ? course.getTargetLevel() + " - " + "7.0+" : course.getTargetLevel() + "+"));


    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView txtCourseName, txtCourseEntryLevel, txtCourseTargetLevel;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCourseName = itemView.findViewById(R.id.courseName);
            txtCourseEntryLevel = itemView.findViewById(R.id.entryLevel);
            txtCourseTargetLevel = itemView.findViewById(R.id.targetLevel);
        }
    }
}
