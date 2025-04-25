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
import com.group5.preppal.utils.ShowToast;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.data.model.enums.CourseSectionType;
import com.group5.preppal.data.model.enums.SubmissionState;
import com.group5.preppal.ui.lesson.LessonPDFDetailActivity;
import com.group5.preppal.ui.lesson.LessonVideoActivity;
import com.group5.preppal.ui.quiz.multiple_choice_quiz.MultipleChoiceActivity;
import com.group5.preppal.ui.quiz.multiple_choice_quiz.MultipleChoiceAnswerActivity;
import com.group5.preppal.ui.quiz.speaking.SpeakingBookingActivity;
import com.group5.preppal.ui.quiz.speaking.SpeakingWaitingActivity;
import com.group5.preppal.ui.quiz.writing_quiz.WritingQuizActivity;
import com.group5.preppal.viewmodel.MultipleChoiceQuizViewModel;
import com.group5.preppal.viewmodel.StudentViewModel;
import com.group5.preppal.viewmodel.WritingTestViewModel;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            callbackLessonState(section, callback);

        } else if (section.containsKey("quiz")) {
            callbackQuizState(section, callback, position);

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

            if (finishedLessons != null) {
                boolean isFinished = finishedLessons.contains(lessonId);
                if (isFinished) holder.sectionTypeFinish.setVisibility(View.VISIBLE);
                else holder.sectionType.setVisibility(View.VISIBLE);
            }  else holder.sectionType.setVisibility(View.VISIBLE);

            holder.itemView.setAlpha(isUnlocked ? 1.0f : 0.4f);
            holder.itemView.setOnClickListener(view -> {
                if (!isUnlocked) {
                    ShowToast.show(context, "Vui lòng hoàn thành phần trước!", ShowToast.ToastType.WARNING);
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
                MultipleChoiceQuizViewModel multipleChoiceQuizViewModel = new ViewModelProvider(viewModelStoreOwner).get(MultipleChoiceQuizViewModel.class);
                float passPoint = Float.parseFloat(quiz.get("passPoint").toString());

                multipleChoiceQuizViewModel.getQuizResult(quizId).observe((LifecycleOwner) viewModelStoreOwner, quizResult -> {
                    boolean quizComplete = false;
                    if (quizResult != null) {
                        quizComplete = true;
                        float score = quizResult.getScore();
                        holder.txtPoint.setText("Score: " + score + "/" + maxPoint);
                        holder.txtPoint.setVisibility(View.VISIBLE);
                        if (score >= passPoint) holder.sectionTypeFinish.setVisibility(View.VISIBLE);
                        else holder.sectionType.setVisibility(View.VISIBLE);
                    }
                    else holder.sectionType.setVisibility(View.VISIBLE);
                    boolean finalQuizCompleted = quizComplete;
                    holder.itemView.setOnClickListener(view -> {
                        if (!isUnlocked ) {
                            ShowToast.show(context, "Vui lòng hoàn thành phần trước!", ShowToast.ToastType.WARNING);
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
                WritingTestViewModel writingTestViewModel = new ViewModelProvider(viewModelStoreOwner).get(WritingTestViewModel.class);

                writingTestViewModel.getWritingQuizSubmissionByTaskId(quizId).observe((LifecycleOwner) viewModelStoreOwner,  writingQuizSubmission -> {
                    if (writingQuizSubmission != null) {
                        String state = writingQuizSubmission.getState();
                        Log.d("State", "Writing State: " + state);
                        if (state != null && !state.equals(SubmissionState.PASSED.getDisplayName())) {
                            holder.txtState.setText(state);
                            holder.txtState.setVisibility(View.VISIBLE);
                            holder.sectionType.setVisibility(View.VISIBLE);
                        } else {
                            holder.sectionTypeFinish.setVisibility(View.VISIBLE);
                        }
                        if (!Objects.equals(state, SubmissionState.PENDING.getDisplayName())) {
                            holder.txtPoint.setText("Score: " + writingQuizSubmission.getPoints() + "/" + maxPoint);
                            holder.txtPoint.setVisibility(View.VISIBLE);
                        } else {
                            float score = 0.0F;
                            holder.txtPoint.setText("Score: " + score + "/" + maxPoint);
                            holder.txtPoint.setVisibility(View.VISIBLE);
                        }
                    } else {
                        holder.sectionType.setVisibility(View.VISIBLE);
                    }
                    holder.itemView.setOnClickListener(view -> {
                        if (!isUnlocked ) {
                            ShowToast.show(context, "Vui lòng hoàn thành phần trước!", ShowToast.ToastType.WARNING);
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
                        ShowToast.show(context, "Vui lòng hoàn thành phần trước!", ShowToast.ToastType.WARNING);
                        return;
                    }
                    handleSpeakingNavigate(quizId);
                });
            }

        }
    }

    private void callbackLessonState(Map<String, Object> section, CompletionCallback callback) {
        String lessonId = ((Map<String, Object>) section.get("lesson")).get("id").toString();
        if (finishedLessons != null && finishedLessons.contains(lessonId)) {
            callback.onResult(true);
        } else {
            callback.onResult(false);
        }
    }

    private void callbackQuizState(Map<String, Object> section, CompletionCallback callback, int position) {
        Map<String, Object> quiz = (Map<String, Object>) section.get("quiz");
        String type = quiz.get("type").toString();
        String quizId = quiz.get("id").toString();

        if (type.contains(CourseSectionType.WRITING.getDisplayName()) || type.contains(CourseSectionType.SPEAKING.getDisplayName())) {
            if (position == 0) callback.onResult(true);
            else checkPreviousSectionCompleted(sectionList.get(position - 1), sectionList, position, callback);
        } else if (type.contains(CourseSectionType.MULTIPLE_CHOICE.getDisplayName())){
            MultipleChoiceQuizViewModel multipleChoiceQuizViewModel = new ViewModelProvider(viewModelStoreOwner).get(MultipleChoiceQuizViewModel.class);

            multipleChoiceQuizViewModel.getQuizResult(quizId).observe((LifecycleOwner) viewModelStoreOwner, quizResult -> {
                if (quizResult != null) {
                    float score = quizResult.getScore();
                    multipleChoiceQuizViewModel.getQuizById(quizId).observe((LifecycleOwner) viewModelStoreOwner, multipleChoiceQuiz -> {
                        if (quiz != null) {
                            float passPoint = multipleChoiceQuiz.getPassPoint();
                            if (score >= passPoint) {
                                callback.onResult(true);
                            } else {
                                callback.onResult(false);
                            }
                        } else {
                            callback.onResult(false);
                        }
                    });
                } else {
                    callback.onResult(false);
                }
            });
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
