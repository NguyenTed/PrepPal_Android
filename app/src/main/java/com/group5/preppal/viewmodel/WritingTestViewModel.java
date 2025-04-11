package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.Task;
import com.group5.preppal.data.model.WritingQuizSubmission;
import com.group5.preppal.data.model.WritingTest;
import com.group5.preppal.data.repository.WritingQuizSubmissionRepository;
import com.group5.preppal.data.repository.WritingTestRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WritingTestViewModel extends ViewModel {
    private final WritingTestRepository repository;
    private final MutableLiveData<List<WritingTest>> writingTests = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // To get submission from firebase
    private final WritingQuizSubmissionRepository writingQuizSubmissionRepository;

    @Inject
    public WritingTestViewModel(WritingTestRepository repository, WritingQuizSubmissionRepository writingQuizSubmissionRepository) {
        this.repository = repository;
        fetchWritingTests();
        this.writingQuizSubmissionRepository = writingQuizSubmissionRepository;
        writingQuizSubmissionRepository.init();
    }

    public LiveData<List<WritingQuizSubmission>> getAllWritingQuizSubmission() {
        return writingQuizSubmissionRepository.getAllQuizSubmission();
    }

    public LiveData<Task> getTaskById(String taskId) {
        return repository.getTaskById(taskId);
    }

    public LiveData<List<WritingQuizSubmission>> getAllPendingWritingQuizSubmissionWithTaskId(String taskId) {
        return writingQuizSubmissionRepository.getPendingSubmissionsWithTaskId(taskId);
    }

    public LiveData<WritingQuizSubmission> getQuizSubmissionById(String submissionId) {
        return writingQuizSubmissionRepository.getQuizSubmissionById(submissionId);
    }

    public LiveData<WritingQuizSubmission> getWritingQuizSubmissionByTaskId(String taskId) {
        return writingQuizSubmissionRepository.getQuizSubmissionByTaskId(taskId);
    }

    public void saveWritingQuizSubmission(WritingQuizSubmission submission, String taskId, String userId, WritingQuizSubmissionRepository.SubmissionCallback callback) {
        writingQuizSubmissionRepository.saveWritingQuizSubmission(submission, taskId, userId, callback);
    }


    public LiveData<List<WritingTest>> getAllWritingTests() {
        return writingTests;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchWritingTests() {
        repository.getAllWritingTests(new WritingTestRepository.FirestoreCallback() {
            @Override
            public void onSuccess(List<WritingTest> tests) {
                writingTests.setValue(tests);
            }
            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Lỗi khi lấy dữ liệu: " + e.getMessage());
            }
        });
    }
}
