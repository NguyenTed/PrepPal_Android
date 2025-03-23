package com.group5.preppal.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.repository.StudentRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StudentViewModel extends ViewModel {
    private StudentRepository studentRepository;

    @Inject
    public StudentViewModel(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public LiveData<Student> getStudentById(String id) {
        return studentRepository.getStudentById(id);
    }

    public void saveFinishedLesson(String lessonId, String studentId) {
        studentRepository.saveFinishedLesson(lessonId, studentId);
    }
}
