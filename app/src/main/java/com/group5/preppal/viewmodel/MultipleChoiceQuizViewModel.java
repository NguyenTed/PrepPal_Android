package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.MultipleChoiceAnsweredQuestion;
import com.group5.preppal.data.model.MultipleChoiceQuiz;
import com.group5.preppal.data.model.MultipleChoiceQuizResult;
import com.group5.preppal.data.repository.MultipleChoiceQuizRepository;
import com.group5.preppal.data.repository.MultipleChoiceQuizResultRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MultipleChoiceQuizViewModel extends ViewModel {
    private final Map<Integer, Integer> selectedAnswers = new HashMap<>();
    private final MultipleChoiceQuizRepository quizRepository;
    private final MultipleChoiceQuizResultRepository quizResultRepository;
    private MultipleChoiceQuizResult savedQuizResult;


    @Inject
    public MultipleChoiceQuizViewModel(MultipleChoiceQuizRepository quizRepository, MultipleChoiceQuizResultRepository quizResultRepository) {
        this.quizRepository = quizRepository;

        this.quizResultRepository = quizResultRepository;
        quizResultRepository.init();
    }

    public LiveData<MultipleChoiceQuiz> getQuizById(String quizId) {
        return quizRepository.getQuizById(quizId);
    }

    public LiveData<MultipleChoiceQuiz> getAnsweredQuizByResult(MultipleChoiceQuizResult quizResult) {
        return quizRepository.getAnsweredQuizByResult(quizResult);
    }

    public LiveData<MultipleChoiceQuizResult> getQuizResult(String quizId) {
        return quizResultRepository.getQuizResult(quizId);
    }

    public void saveAnswer(int questionIndex, int selectedOptionIndex) {
        selectedAnswers.put(questionIndex, selectedOptionIndex);
    }

    public Integer getSavedAnswer(int questionIndex) {
        return selectedAnswers.get(questionIndex);
    }

    public void setSavedQuizResult(MultipleChoiceQuizResult result) {
        this.savedQuizResult = result;
    }

    public MultipleChoiceQuizResult getSavedQuizResult() {
        return savedQuizResult;
    }

    public void saveQuizResult(String studentId, String quizId, float score, float passPoint,
                               List<MultipleChoiceAnsweredQuestion> answeredQuestions,
                               MultipleChoiceQuizResultRepository.SaveResultCallback callback) {

        boolean pass = score >= passPoint;
        MultipleChoiceQuizResult quizResult = new MultipleChoiceQuizResult(studentId, quizId, score, pass, answeredQuestions);

        this.savedQuizResult = quizResult;

        quizResultRepository.saveQuizResult(quizResult, callback);
    }
}
