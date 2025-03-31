package com.group5.preppal.ui.course;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.R;
import com.group5.preppal.ui.lesson.LessonPDFDetailActivity;
import com.group5.preppal.ui.lesson.LessonVideoActivity;
import com.group5.preppal.ui.quiz.multiple_choice_quiz.MultipleChoiceActivity;
import com.group5.preppal.ui.quiz.multiple_choice_quiz.MultipleChoiceAnswerActivity;
import com.group5.preppal.ui.quiz.speaking.SpeakingBookingActivity;
import com.group5.preppal.ui.quiz.speaking.SpeakingWaitingActivity;
import com.group5.preppal.ui.quiz.writing_quiz.WritingQuizActivity;
import com.group5.preppal.ui.video_call.VideoCallActivity;
import com.group5.preppal.viewmodel.StudentViewModel;
import com.group5.preppal.viewmodel.WritingTestViewModel;

import java.util.List;
import java.util.Map;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    private final List<Map<String, Object>> sectionList;
    private final Context context;
    private String courseId;
    private final ViewModelStoreOwner viewModelStoreOwner;
    private FirebaseUser user;
    private final List<String> finishedLessons;
    private StudentViewModel studentViewModel;


    public SectionAdapter(List<Map<String, Object>> sectionList, Context context, String courseId, FirebaseAuth firebaseAuth, ViewModelStoreOwner viewModelStoreOwner,List<String> finishedLessons, StudentViewModel studentViewModel) {
        this.sectionList = sectionList;
        this.context = context;
        this.courseId = courseId;
        this.user = firebaseAuth.getCurrentUser();
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.finishedLessons = finishedLessons;
        this.studentViewModel = studentViewModel;
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

        if (position == 0) {
            bindSection(holder, section, true);
        } else {
            Map<String, Object> previousSection = sectionList.get(position - 1);
            checkPreviousSectionCompleted(previousSection, sectionList, position - 1, isCompleted -> {
                bindSection(holder, section, isCompleted);
            });
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

    private interface CompletionCallback {
        void onResult(boolean isCompleted);
    }

    private void checkPreviousSectionCompleted(Map<String, Object> section, List<Map<String, Object>> sectionList, int position,  CompletionCallback callback) {
        if (section.containsKey("lesson")) {
            String lessonId = ((Map<String, Object>) section.get("lesson")).get("id").toString();
            callback.onResult(finishedLessons.contains(lessonId));
        } else if (section.containsKey("quiz")) {
            Map<String, Object> quiz = (Map<String, Object>) section.get("quiz");
            String type = quiz.get("type").toString();
            String quizId = quiz.get("id").toString();

            if (type.contains("Writing") || type.contains("Speaking")) {
                if (position == 0) callback.onResult(true);
                else checkPreviousSectionCompleted(sectionList.get(position - 1), sectionList, position, callback);
            } else if (type.contains("Multiple Choice Quiz")){
                String studentId = user.getUid();

                FirebaseFirestore.getInstance()
                        .collection("multiple_choice_quiz_result")
                        .document(studentId + "_" + quizId)
                        .get()
                        .addOnSuccessListener(resultDoc -> {
                            if (resultDoc.exists()) {
                                float score = Float.parseFloat(resultDoc.get("score").toString());

                                // Lấy passPoint từ multiple_choice_quizs collection
                                FirebaseFirestore.getInstance()
                                        .collection("multiple_choice_quizs")
                                        .document(quizId)
                                        .get()
                                        .addOnSuccessListener(quizDoc -> {
                                            if (quizDoc.exists()) {
                                                float passPoint = Float.parseFloat(quizDoc.get("passPoint").toString());

                                                if (score >= passPoint) {
                                                    callback.onResult(true);
                                                } else {
                                                    callback.onResult(false);
                                                }
                                            } else {
                                                callback.onResult(false);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("SectionAdapter", "Failed to get passPoint", e);
                                            callback.onResult(false);
                                        });

                            } else {
                                callback.onResult(false);
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("SectionAdapter", "Failed to check quiz result", e);
                            callback.onResult(false);
                        });
            }

        } else {
            callback.onResult(false);
        }
    }

    private void bindSection(SectionViewHolder holder, Map<String, Object> section, boolean isUnlocked) {
        if (section.containsKey("lesson")) {
            Map<String, Object> lesson = (Map<String, Object>) section.get("lesson");
            String lessonId = lesson.get("id").toString();
            String type = lesson.get("type").toString();
            String name = lesson.get("name").toString();

            holder.sectionName.setText(name);
            holder.sectionType.setText(type);
            holder.txtTypeFinish.setText(type);

            boolean isFinished = finishedLessons.contains(lessonId);
            if (isFinished) holder.sectionTypeFinish.setVisibility(View.VISIBLE);
            else holder.sectionType.setVisibility(View.VISIBLE);

            holder.itemView.setAlpha(isUnlocked ? 1.0f : 0.4f);
            holder.itemView.setOnClickListener(view -> {
                if (!isUnlocked) {
                    Toast.makeText(context, "Vui lòng hoàn thành phần trước!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, type.equals("Reading") ? LessonPDFDetailActivity.class : LessonVideoActivity.class);
                intent.putExtra("lessonId", lessonId);
                intent.putExtra("courseId", courseId);
                context.startActivity(intent);
            });
        } else if (section.containsKey("quiz")) {
            Map<String, Object> quiz = (Map<String, Object>) section.get("quiz");
            String type = quiz.get("type").toString();
            String quizId = quiz.get("id").toString();
            String name = quiz.get("name").toString();
            float maxPoint = Float.parseFloat(quiz.get("maxPoints").toString());

            holder.sectionName.setText(type.contains("Writing") ? "Writing: " + name : name);
            holder.sectionType.setText(type);
            holder.txtTypeFinish.setText(type);

            holder.itemView.setAlpha(isUnlocked ? 1.0f : 0.4f);

            if (type.contains("Multiple Choice")) {
                float passPoint = Float.parseFloat(quiz.get("passPoint").toString());

                FirebaseFirestore.getInstance()
                        .collection("multiple_choice_quiz_result")
                        .document(user.getUid() + "_" + quizId)
                        .get()
                        .addOnSuccessListener(doc -> {
                            boolean quizComplete = false;
                            if (doc.exists()) {
                                quizComplete = true;
                                Float score = Float.parseFloat(doc.get("score").toString());
                                holder.txtPoint.setText("Score: " + score + "/" + maxPoint);
                                holder.txtPoint.setVisibility(View.VISIBLE);
                                if (score >= passPoint) holder.sectionTypeFinish.setVisibility(View.VISIBLE);
                                else holder.sectionType.setVisibility(View.VISIBLE);
                            }
                            else holder.sectionType.setVisibility(View.VISIBLE);
                            boolean finalQuizCompleted = quizComplete;
                            holder.itemView.setOnClickListener(view -> {
                                if (!isUnlocked ) {
                                    Toast.makeText(context, "Vui lòng hoàn thành các phần trước!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
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
                        });

            }
            else if (type.contains("Writing")) {
                String userId = user.getUid();

                WritingTestViewModel writingTestViewModel = new ViewModelProvider(viewModelStoreOwner).get(WritingTestViewModel.class);

                writingTestViewModel.getWritingQuizSubmissionByTasKId(quizId, userId).observeForever(writingQuizSubmission -> {
                    if (writingQuizSubmission != null) {
                        String submissionState = writingQuizSubmission.getState();
                        if (submissionState != null && !submissionState.equals("pass")) {
                            holder.txtState.setText(submissionState);
                            holder.txtState.setVisibility(View.VISIBLE);
                            holder.sectionType.setVisibility(View.VISIBLE);
                        } else {
                            holder.sectionTypeFinish.setVisibility(View.VISIBLE);
                        }
                        holder.txtPoint.setText("Score: " + writingQuizSubmission.getPoints() + "/" + maxPoint);
                        holder.txtPoint.setVisibility(View.VISIBLE);
                    } else {
                        holder.sectionType.setVisibility(View.VISIBLE);
                    }
                    holder.itemView.setOnClickListener(view -> {
                        if (!isUnlocked ) {
                            Toast.makeText(context, "Vui lòng hoàn thành các phần trước!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(context, WritingQuizActivity.class);
                        intent.putExtra("id", quizId);
                        intent.putExtra("courseId", courseId);
                        context.startActivity(intent);
                    });
                });
            } else if (type.contains("Speaking")) {
                holder.sectionName.setText(name);
                holder.sectionType.setText(type);
                holder.txtTypeFinish.setText(type);
                holder.sectionType.setVisibility(View.VISIBLE);

                holder.itemView.setAlpha(isUnlocked ? 1.0f : 0.4f);
                holder.itemView.setOnClickListener(view -> {
                    if (!isUnlocked ) {
                        Toast.makeText(context, "Vui lòng hoàn thành các phần trước!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    handleSpeakingNavigate(quizId);
                });
            }

        }
    }

    private void handleSpeakingNavigate(String speakingTestId) {
        studentViewModel.fetchStudentBookedSpeakingById(speakingTestId, user.getUid()).observe((LifecycleOwner) viewModelStoreOwner, booked -> {
            Intent intent;
            if (booked != null) {
                intent = new Intent(context, SpeakingWaitingActivity.class);
            }
            else {
                intent = new Intent(context, SpeakingBookingActivity.class);
            }
            intent.putExtra("quizId", speakingTestId);
            intent.putExtra("courseId", courseId);
            context.startActivity(intent);
        });
    }
}
