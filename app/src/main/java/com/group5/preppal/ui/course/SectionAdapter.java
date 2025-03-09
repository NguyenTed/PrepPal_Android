package com.group5.preppal.ui.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;

import java.util.List;
import java.util.Map;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    private final List<Map<String, Object>> sectionList;

    public SectionAdapter(List<Map<String, Object>> sectionList) {
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Map<String, Object> section = sectionList.get(position);

        // Kiểm tra xem có "lesson" không, và nó có phải là một Map không
        if (section.containsKey("lesson") && section.get("lesson") instanceof Map) {
            Map<String, Object> lesson = (Map<String, Object>) section.get("lesson");

            String sectionName = lesson.containsKey("name") && lesson.get("name") != null
                    ? lesson.get("name").toString()
                    : "Unknown Lesson";

            String type = lesson.containsKey("type") && lesson.get("type") != null
                    ? lesson.get("type").toString()
                    : "Unknown Type";

            holder.sectionName.setText(sectionName);
            holder.sectionType.setText(type);
        } else {
            // Trường hợp không có lesson hoặc không phải kiểu Map
            holder.sectionName.setText("No Lesson Info");
            holder.sectionType.setText("Unknown");
        }
    }


    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName, sectionType;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.sectionName);
            sectionType = itemView.findViewById(R.id.sectionType);
        }
    }
}
