package com.group5.preppal.ui.test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
    private TextView tvQuestion;
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

    public WritingTestFragment() {
        // Constructor rỗng bắt buộc
    }

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

        tvQuestion = view.findViewById(R.id.tvQuestion);
        etAnswer = view.findViewById(R.id.etAnswer);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        imgQuestion = view.findViewById(R.id.imgQuestion);

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
                    Glide.with(this)
                            .load(imgUrl)
                            .placeholder(R.drawable.loading)
                            .error(new ColorDrawable(Color.TRANSPARENT))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    imgQuestion.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    imgQuestion.setVisibility(View.VISIBLE);
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
            writingTestViewModel.getWritingQuizSubmissionById(taskId, user.getUid()).observe(getViewLifecycleOwner(), submission -> {
                if (submission != null) {
                    etAnswer.setText(submission.getAnswer());
                    if (!Objects.equals(submission.getState(), "pass")) {
                        btnSubmitQuiz.setText("Update");
                    }
                }
            });
        }

        btnSubmit.setOnClickListener(v -> submitAnswer(false));
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
            WritingQuizSubmission writingQuizSubmission = new WritingQuizSubmission(answer, 0.0F, taskId, user.getUid(), "pending");

            writingTestViewModel.saveWritingQuizSubmission(writingQuizSubmission, taskId, user.getUid(), new WritingQuizSubmissionRepository.SubmissionCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
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
            db.collection( "writing_submissions")
                    .add(submission)
                    .addOnSuccessListener(documentReference ->
                            Toast.makeText(requireContext(), "Answer submitted successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(requireContext(), "Failed to submit answer", Toast.LENGTH_SHORT).show());
        }
    }
}
