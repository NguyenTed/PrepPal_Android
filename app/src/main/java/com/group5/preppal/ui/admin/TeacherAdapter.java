package com.group5.preppal.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {

    private final List<Teacher> teacherList = new ArrayList<>();

    public void submitList(List<Teacher> list) {
        teacherList.clear();
        teacherList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_teacher_card, parent, false);
        return new TeacherViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher t = teacherList.get(position);
        holder.teacherId.setText("ID: " + t.getUid());
        holder.teacherName.setText(t.getName());
        holder.teacherEmail.setText(t.getEmail());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    static class TeacherViewHolder extends RecyclerView.ViewHolder {
        TextView teacherId, teacherName, teacherEmail;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherId = itemView.findViewById(R.id.teacher_id);
            teacherName = itemView.findViewById(R.id.teacher_name);
            teacherEmail = itemView.findViewById(R.id.teacher_email);
        }
    }
}

