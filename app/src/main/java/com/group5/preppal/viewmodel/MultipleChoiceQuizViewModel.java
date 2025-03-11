package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.MultipleChoiceQuiz;
import com.group5.preppal.data.repository.MultipleChoiceQuizRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MultipleChoiceQuizViewModel extends ViewModel {
    private final MultipleChoiceQuizRepository quizRepository;

    @Inject
    public MultipleChoiceQuizViewModel(MultipleChoiceQuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }


    public LiveData<MultipleChoiceQuiz> getQuizById(String quizId) {
        return quizRepository.getQuizById(quizId);
    }
}
