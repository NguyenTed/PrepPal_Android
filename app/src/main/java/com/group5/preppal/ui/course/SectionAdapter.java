package com.group5.preppal.ui.course;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.R;
import com.group5.preppal.data.model.WritingQuizSubmission;
import com.group5.preppal.ui.lesson.LessonPDFDetailActivity;
import com.group5.preppal.ui.lesson.LessonVideoActivity;
import com.group5.preppal.ui.quiz.multiple_choice_quiz.MultipleChoiceActivity;
import com.group5.preppal.ui.quiz.multiple_choice_quiz.MultipleChoiceAnswerActivity;
import com.group5.preppal.ui.quiz.writing_quiz.WritingQuizActivity;
import com.group5.preppal.viewmodel.WritingTestViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    private final List<Map<String, Object>> sectionList;
    private final Context context;
    private String courseId;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private final FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private boolean isDone;


    public SectionAdapter(List<Map<String, Object>> sectionList, Context context, String courseId, FirebaseAuth firebaseAuth, ViewModelStoreOwner viewModelStoreOwner) {
        this.sectionList = sectionList;
        this.context = context;
        this.courseId = courseId;
        this.firebaseAuth = firebaseAuth;
        this.user = firebaseAuth.getCurrentUser();
        this.viewModelStoreOwner = viewModelStoreOwner;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        user = firebaseAuth.getCurrentUser();
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Map<String, Object> section = sectionList.get(position);

        if (section.containsKey("lesson")) {
            Map<String, Object> lesson = (Map<String, Object>) section.get("lesson");

            String sectionName = lesson.containsKey("name") ? lesson.get("name").toString() : "Unknown Lesson";
            String type = lesson.containsKey("type") ? lesson.get("type").toString() : "Unknown Type";

            holder.sectionName.setText(sectionName);
            holder.sectionType.setText(type);
            holder.txtTypeFinish.setText(type);

            holder.sectionType.setVisibility(View.VISIBLE);

            if (type.equals("Reading")) {
                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, LessonPDFDetailActivity.class);
                    intent.putExtra("lessonId", lesson.get("id").toString());
                    intent.putExtra("courseId", courseId);
                    context.startActivity(intent);
                });
            } else if (type.equals("Video")) {
                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, LessonVideoActivity.class);
                    intent.putExtra("lessonId", lesson.get("id").toString());
                    intent.putExtra("courseId", courseId);
                    context.startActivity(intent);
                });
            }
        } else if (section.containsKey("quiz")) {
            Map<String, Object> quiz = (Map<String, Object>) section.get("quiz");

            String sectionName = quiz.containsKey("name") ? quiz.get("name").toString() : "Unknown Lesson";
            String type = quiz.containsKey("type") ? quiz.get("type").toString() : "Unknown Type";
            String quizId = quiz.containsKey("id") ? quiz.get("id").toString() : null;
            String maxPoints;
            if (quiz.containsKey("maxPoints")) {
                maxPoints = Float.toString(Float.parseFloat(quiz.get("maxPoints").toString()));
            } else {
                maxPoints = "0.0";
            }

            holder.sectionName.setText(sectionName);
            holder.sectionType.setText(type);
            holder.txtTypeFinish.setText(type);

            if (type.equals("Multiple Choice Quiz")) {
                checkQuizResult(holder, quizId, maxPoints);
            } else if (type.contains("Writing")) {
                holder.sectionName.setText("Writing: " + sectionName);
                holder.sectionType.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, WritingQuizActivity.class);
                    intent.putExtra("id", quizId);
                    intent.putExtra("courseId", courseId);
                    context.startActivity(intent);
                });
                checkWritingQuizResult(holder, quizId, maxPoints);
            }
        }
}


    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName, sectionType, txtTypeFinish, txtPoint, txtState;
        LinearLayout sectionTypeFinish;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.sectionName);
            sectionType = itemView.findViewById(R.id.sectionType);
            txtTypeFinish = itemView.findViewById(R.id.txtSectionTypeFinish);
            txtPoint = itemView.findViewById(R.id.txtPoint);
            txtState = itemView.findViewById(R.id.txtState);
            sectionTypeFinish = itemView.findViewById(R.id.sectionTypeFinish);
        }
    }

    private void checkQuizResult(SectionViewHolder holder, String quizId, String maxPoints) {
        if (user == null) return;

        String userId = user.getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("multiple_choice_quiz_result")
                .document(userId + "_" + quizId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean quizCompleted = false;
                    if (documentSnapshot.exists()) {
                        quizCompleted = true;
                        if (documentSnapshot.contains("score")) {
                            String score = documentSnapshot.get("score").toString();
                            holder.txtPoint.setText("Score: " + score + "/" + maxPoints);
                            holder.txtPoint.setVisibility(View.VISIBLE);
                        }

                        if (documentSnapshot.contains("pass")) {
                            Boolean isPass = documentSnapshot.getBoolean("pass");
                            if (isPass != null && isPass) {
                                holder.sectionTypeFinish.setVisibility(View.VISIBLE);
                            }
                            else holder.sectionType.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        holder.sectionType.setVisibility(View.VISIBLE);
                    }
                    boolean finalQuizCompleted = quizCompleted;
                    holder.itemView.setOnClickListener(view -> {
                        Intent intent;
                        if (finalQuizCompleted) {
                            intent = new Intent(context, MultipleChoiceAnswerActivity.class);
                        } else {
                            intent = new Intent(context, MultipleChoiceActivity.class);
                        }
                        intent.putExtra("quizId", quizId);
                        intent.putExtra("courseId", courseId);
                        context.startActivity(intent);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Lỗi khi lấy kết quả quiz", e);
                });
    }

    private void checkWritingQuizResult(SectionViewHolder holder, String taskId, String maxPoints) {
        String userId = user.getUid();

        WritingTestViewModel writingTestViewModel = new ViewModelProvider(viewModelStoreOwner).get(WritingTestViewModel.class);

        writingTestViewModel.getWritingQuizSubmissionById(taskId, userId).observeForever(writingQuizSubmission -> {
            if (writingQuizSubmission != null) {
                if (writingQuizSubmission.getState() != null && !writingQuizSubmission.getState().equals("pass")) {
                    holder.txtState.setText(writingQuizSubmission.getState());
                    holder.txtState.setVisibility(View.VISIBLE);
                }
                holder.txtPoint.setText("Score: " + writingQuizSubmission.getPoints() + "/" + maxPoints);
                holder.txtPoint.setVisibility(View.VISIBLE);
            }
        });
    }
}
