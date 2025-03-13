package com.group5.preppal.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.Lesson;
import com.group5.preppal.data.repository.LessonRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class LessonViewModel extends ViewModel {
    private final LessonRepository lessonRepository;
    private final MutableLiveData<Lesson> selectedLesson = new MutableLiveData<>();

    @Inject
    public LessonViewModel(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public void fetchLessonById(String lessonId) {
        lessonRepository.getLessonById(lessonId).observeForever(lesson -> {
            selectedLesson.setValue(lesson);
        });
    }

    public LiveData<Lesson> getSelectedLesson() {
        return selectedLesson;
    }
}
