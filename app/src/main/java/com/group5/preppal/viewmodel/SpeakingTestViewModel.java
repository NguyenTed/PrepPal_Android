package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.StudentBookedSpeaking;
import com.group5.preppal.data.model.SpeakingTest;
import com.group5.preppal.data.repository.SpeakingTestRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SpeakingTestViewModel extends ViewModel {
    private final SpeakingTestRepository speakingTestRepository;

    @Inject
    public SpeakingTestViewModel(SpeakingTestRepository speakingTestRepository) {
        this.speakingTestRepository = speakingTestRepository;
    }

    public LiveData<SpeakingTest> getSpeakingTestById(String speakingTestId) {
        return speakingTestRepository.getSpeakingTestById(speakingTestId);
    }

    public LiveData<SpeakingTest> getSpeakingTestByCourseId(String courseId) {
        return speakingTestRepository.getSpeakingTestByCourseId(courseId);
    }

    public void saveBookedTime (String studentId, StudentBookedSpeaking studentBookedSpeaking) {
        speakingTestRepository.saveBookedTime(studentId, studentBookedSpeaking);
    }

}
