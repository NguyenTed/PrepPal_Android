package com.group5.preppal.ui.course;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.ui.lesson.LessonPDFDetailActivity;
import com.group5.preppal.ui.lesson.LessonVideoActivity;
import com.group5.preppal.ui.quiz.multiple_choice_quiz.MultipleChoiceActivity;

import java.util.List;
import java.util.Map;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    private final List<Map<String, Object>> sectionList;
    private final Context context;
    private String courseId;

    public SectionAdapter(List<Map<String, Object>> sectionList, Context context, String courseId) {
        this.sectionList = sectionList;
        this.context = context;
        this.courseId = courseId;
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
            if (type.equals("Reading")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, LessonPDFDetailActivity.class);
                        intent.putExtra("lessonId", lesson.get("id").toString());
                        intent.putExtra("courseId", courseId);
                        context.startActivity(intent);
                    }
                });
            }
            else if (type.equals("Video")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, LessonVideoActivity.class);
                        intent.putExtra("lessonId", lesson.get("id").toString());
                        intent.putExtra("courseId", courseId);
                        context.startActivity(intent);
                    }
                });
            }

        } else if ((section.containsKey("quiz") )) {
            Map<String, Object> quiz = (Map<String, Object>) section.get("quiz");

            String sectionName = quiz.containsKey("name") && quiz.get("name") != null
                    ? quiz.get("name").toString()
                    : "Unknown Lesson";

            String type = quiz.containsKey("type") && quiz.get("type") != null
                    ? quiz.get("type").toString()
                    : "Unknown Type";

            holder.sectionName.setText(sectionName);
            holder.sectionType.setText(type);
            if (type.equals("Multiple Choice Quiz")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, MultipleChoiceActivity.class);
                        intent.putExtra("quizId", quiz.get("id").toString());
                        intent.putExtra("courseId", courseId);
                        context.startActivity(intent);
                    }
                });
            }
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
