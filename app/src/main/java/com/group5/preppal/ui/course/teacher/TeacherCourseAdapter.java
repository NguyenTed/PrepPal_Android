package com.group5.preppal.ui.course.teacher;

import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Course;
import com.group5.preppal.ui.grading.GradingTestActivity;
import com.group5.preppal.ui.grading.WritingAnswerListActivity;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

public class TeacherCourseAdapter  extends RecyclerView.Adapter<TeacherCourseAdapter.CourseViewHolder> {
    private final List<Course> courseList;
    private final SparseBooleanArray expandedItems = new SparseBooleanArray(); // Track which items are expanded

    public TeacherCourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        String courseName = courseList.get(position).getName();
        holder.tvCourseName.setText(courseName);

        boolean isExpanded = expandedItems.get(position, false);
        holder.dropdownLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.btnArrow.setRotation(isExpanded ? 90 : 0);

        View.OnClickListener toggleListener = v -> {
            boolean newState = !expandedItems.get(position, false);
            expandedItems.put(position, newState);
            notifyItemChanged(position);
        };

        holder.headerLayout.setOnClickListener(toggleListener);
        holder.btnArrow.setOnClickListener(toggleListener);

        holder.tvWriting.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), GradingTestActivity.class);
            intent.putExtra("courseId", course.getCourseId()); // đảm bảo Course có phương thức getId()
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvWriting, tvSpeaking;
        LinearLayout dropdownLayout, headerLayout;
        ImageButton btnArrow;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvWriting = itemView.findViewById(R.id.tvWriting);
            tvSpeaking = itemView.findViewById(R.id.tvSpeaking);
            dropdownLayout = itemView.findViewById(R.id.dropdownLayout);
            btnArrow = itemView.findViewById(R.id.btnArrow);
            headerLayout = itemView.findViewById(R.id.headerLayout);

        }
    }
}
