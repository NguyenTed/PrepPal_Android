package com.group5.preppal.ui.test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.R;
import com.group5.preppal.data.model.WritingQuizSubmission;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.data.repository.WritingQuizSubmissionRepository;
import com.group5.preppal.ui.course.CourseDetailActivity;
import com.group5.preppal.ui.quiz.writing_quiz.WritingQuizActivity;
import com.group5.preppal.viewmodel.TaskViewModel;
import com.group5.preppal.viewmodel.WritingTestViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WritingTestFragment extends Fragment {
    private TextView tvQuestion, tvComment, tvCommentInteract;
    private EditText etAnswer;
    private Button btnSubmit, btnSubmitQuiz;
    private ImageView imgQuestion;
    private FirebaseFirestore db;
    private TaskViewModel taskViewModel;

    private WritingTestViewModel writingTestViewModel;

    @Inject
    AuthRepository authRepository;
    private FirebaseUser user;

    private String taskId;

    public WritingTestFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_writing_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        writingTestViewModel = new ViewModelProvider(this).get(WritingTestViewModel.class);

        tvCommentInteract = view.findViewById(R.id.tvCommentInteract);
        tvComment = view.findViewById(R.id.tvComment);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        etAnswer = view.findViewById(R.id.etAnswer);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        imgQuestion = view.findViewById(R.id.imgQuestion);

        tvComment.setMovementMethod(new ScrollingMovementMethod());
        tvComment.setVerticalScrollBarEnabled(true);
        tvComment.setFocusable(true);
        tvComment.setFocusableInTouchMode(true);
        tvComment.setClickable(true);
        tvComment.setOnTouchListener((v, event) -> {
            handleEditTextScroll(v, event);
            return false;
        });


        taskId = getActivity().getIntent().getStringExtra("id");
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        if (taskId != null) {
            taskViewModel.fetchTaskById(taskId);
        } else {
            Log.e("WritingTestFragment", "Task ID không hợp lệ!");
        }

        taskViewModel.getTaskLiveData().observe(getViewLifecycleOwner(), task -> {
            if (task != null) {
                tvQuestion.setText(task.getDescription());
                final String imgUrl = task.getImgUrl();

                if (getActivity() instanceof WritingTestActivity) {
                    ((WritingTestActivity) getActivity()).updateTitle(task.getTitle());
                    btnSubmit.setVisibility(View.VISIBLE);
                } else if (getActivity() instanceof WritingQuizActivity) {
                    ((WritingQuizActivity) getActivity()).updateTitle(task.getTitle(), task.getTaskType());
                }


                if (imgUrl != null && !imgUrl.isEmpty()) {
                    imgQuestion.setVisibility(View.VISIBLE);

                    // Hiển thị GIF khi ảnh chính đang tải
                    Glide.with(this)
                            .asGif()
                            .load(R.drawable.loading_gif) // Thay thế bằng GIF của bạn
                            .into(imgQuestion);

                    Glide.with(this)
                            .load(imgUrl)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    imgQuestion.setVisibility(View.GONE); // Ẩn ảnh nếu lỗi
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    imgQuestion.setVisibility(View.VISIBLE); // Hiển thị ảnh chính
                                    return false;
                                }
                            })
                            .into(imgQuestion);
                } else {
                    imgQuestion.setVisibility(View.GONE);
                }
            }
        });

        // Kiểm tra người dùng đăng nhập
        user = authRepository.getCurrentUser();
        if (user == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return;
        }


        if (getActivity() instanceof  WritingQuizActivity) {
            btnSubmitQuiz = getActivity().findViewById(R.id.btnSubmitQuiz);
            btnSubmitQuiz.setOnClickListener(v -> submitAnswer(true));
            writingTestViewModel.getWritingQuizSubmissionByTasKId(taskId, user.getUid()).observe(getViewLifecycleOwner(), submission -> {
                if (submission != null) {
                    if (submission.getComment() != "") {
                        tvComment.setVisibility(View.VISIBLE);
                        tvComment.setText(submission.getComment());
                        tvCommentInteract.setText("Teacher comment: ");
                    } else tvCommentInteract.setText("Teacher has not reviewed yet.");
                    etAnswer.setText(submission.getAnswer());
                    if (!Objects.equals(submission.getState(), "pass") && !Objects.equals(submission.getState(), "pending")) {
                        btnSubmitQuiz.setText("Update");
                    } else {
                        btnSubmitQuiz.setEnabled(false);
                        btnSubmitQuiz.setText("Submitted");
                        btnSubmitQuiz.setBackgroundResource(R.drawable.rounded_5dp_white_2dp_border_gray);
                        btnSubmitQuiz.setTextColor(Color.parseColor("#A3A5A4"));
                        etAnswer.setEnabled(false);
                        etAnswer.setTextColor(Color.parseColor("#000000"));
                    }
                }
            });
        } else {
            btnSubmit.setOnClickListener(v -> submitAnswer(false));
            db.collection("writing_submissions")
                    .whereEqualTo("taskId", taskId)
                    .whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Lấy document đầu tiên phù hợp
                            String existingAnswer = queryDocumentSnapshots.getDocuments().get(0).getString("answer");
                            if (existingAnswer != null) {
                                etAnswer.setText(existingAnswer);
                            }
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(requireContext(), "Lỗi khi tải câu trả lời trước đó", Toast.LENGTH_SHORT).show());
        }

    }

    private void submitAnswer(boolean isQuiz) {
        String answer = etAnswer.getText().toString().trim();
        if (answer.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();

        // Tạo dữ liệu để gửi lên Firestore
        Map<String, Object> submission = new HashMap<>();
        submission.put("userId", userId);
        submission.put("answer", answer);
        submission.put("taskId", taskId);
        submission.put("timestamp", System.currentTimeMillis());
        if (isQuiz) {
            submission.put("points", 0);
        }

        if (isQuiz) {
            WritingQuizSubmission writingQuizSubmission = new WritingQuizSubmission(answer, 0.0F, taskId, user.getUid(), "pending", "");

            writingTestViewModel.saveWritingQuizSubmission(writingQuizSubmission, taskId, user.getUid(), new WritingQuizSubmissionRepository.SubmissionCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    if (getActivity() instanceof WritingQuizActivity) {
                        Intent intent = new Intent(requireContext(), CourseDetailActivity.class);
                        intent.putExtra("courseId", requireActivity().getIntent().getStringExtra("courseId"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    requireActivity().finish();
                }
                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            // Lưu vào Firestore
            db.collection("writing_submissions")
                    .whereEqualTo("taskId", taskId)
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Nếu có document, cập nhật lại dữ liệu
                            String docId = queryDocumentSnapshots.getDocuments().get(0).getId();
                            db.collection("writing_submissions")
                                    .document(docId)
                                    .update("answer", answer, "timestamp", System.currentTimeMillis())
                                    .addOnSuccessListener(aVoid ->{
                                        Toast.makeText(requireContext(), "Answer updated successfully!", Toast.LENGTH_SHORT).show();
                                        requireActivity().finish();})
                                    .addOnFailureListener(e ->
                                            Toast.makeText(requireContext(), "Failed to update answer", Toast.LENGTH_SHORT).show());
                        } else {
                            // Nếu không có document, tạo mới
                            db.collection("writing_submissions")
                                    .add(submission)
                                    .addOnSuccessListener(documentReference ->{
                                        Toast.makeText(requireContext(), "Answer submitted successfully!", Toast.LENGTH_SHORT).show();
                                        requireActivity().finish();})
                                    .addOnFailureListener(e ->
                                            Toast.makeText(requireContext(), "Failed to submit answer", Toast.LENGTH_SHORT).show());
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(requireContext(), "Error checking existing submissions", Toast.LENGTH_SHORT).show());
        }

    }

    private void handleEditTextScroll(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);

        if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
            v.getParent().requestDisallowInterceptTouchEvent(false);
        }
    }
}
