package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.group5.preppal.data.model.Task;
import com.group5.preppal.data.repository.TaskRepository;

public class TaskViewModel extends ViewModel {
    private final TaskRepository repository;
    private final MutableLiveData<Task> taskLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public TaskViewModel() {
        repository = new TaskRepository();
    }

    public LiveData<Task> getTaskLiveData() {
        return taskLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchTaskById(String taskId) {
        repository.findTaskById(taskId, new TaskRepository.FirestoreTaskCallback() {
            @Override
            public void onSuccess(Task task) {
                taskLiveData.setValue(task);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Không tìm thấy Task: " + e.getMessage());
            }
        });
    }
}
