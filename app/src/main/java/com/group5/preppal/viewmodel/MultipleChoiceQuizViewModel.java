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

    @Inject
    public MultipleChoiceQuizViewModel(MultipleChoiceQuizRepository quizRepository, MultipleChoiceQuizResultRepository quizResultRepository) {
        this.quizRepository = quizRepository;
        this.quizResultRepository = quizResultRepository;
    }

    public LiveData<MultipleChoiceQuiz> getQuizById(String quizId) {
        return quizRepository.getQuizById(quizId);
    }

    public LiveData<MultipleChoiceQuizResult> getQuizResultById(String quizResultId) {
        return quizResultRepository.getQuizResultById(quizResultId);
    }

    public void saveAnswer(int questionIndex, int selectedOptionIndex) {
        selectedAnswers.put(questionIndex, selectedOptionIndex);
    }

    public Integer getSavedAnswer(int questionIndex) {
        return selectedAnswers.get(questionIndex);
    }

    public void saveQuizResult(String studentId, String quizId, float score, float passPoint, List<MultipleChoiceAnsweredQuestion> answeredQuestions) {
        boolean pass = score >= passPoint;
        MultipleChoiceQuizResult quizResult = new MultipleChoiceQuizResult(studentId, quizId, score, pass, answeredQuestions);

        quizResultRepository.saveQuizResult(quizResult, new MultipleChoiceQuizResultRepository.SaveResultCallback() {
            @Override
            public void onSuccess() {
                // Kết quả được lưu thành công
            }

            @Override
            public void onFailure(Exception e) {
                // Xử lý lỗi
            }
        });
    }
}
