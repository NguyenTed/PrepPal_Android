package com.group5.preppal.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.group5.preppal.data.model.test.TestAttempt;
import com.group5.preppal.data.model.test.listening.ListeningAttempt;
import com.group5.preppal.data.model.test.listening.ListeningGrader;
import com.group5.preppal.data.model.test.listening.ListeningPart;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.data.repository.practise_test.TestAttemptRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ListeningViewModel extends ViewModel {

    private final MutableLiveData<ListeningPart> currentPart = new MutableLiveData<>();
    private final TestAttemptRepository attemptRepository;

    @Inject
    public ListeningViewModel(TestAttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public void setPart(ListeningPart part) {
        currentPart.setValue(part);
    }

    public LiveData<ListeningPart> getCurrentPart() {
        return currentPart;
    }

    public void submitListeningAttempt(
            @NonNull String testId,
            @NonNull String testSetId,
            @NonNull ListeningSection section,
            @NonNull Map<Integer, String> userAnswers,
            @NonNull OnSuccessListener<Void> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        int score = ListeningGrader.gradeListeningSection(section, userAnswers);

        Map<String, String> convertedAnswers = new HashMap<>();
        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            convertedAnswers.put(String.valueOf(entry.getKey()), entry.getValue());
        }

        ListeningAttempt listeningAttempt = new ListeningAttempt();
        listeningAttempt.setAnswers(convertedAnswers);
        listeningAttempt.setScore(score);
        listeningAttempt.setStartedAt(new Date());
        listeningAttempt.setSubmittedAt(new Date());

        TestAttempt attempt = new TestAttempt();
        attempt.setTestId(testId);
        attempt.setTestSetId(testSetId);
        attempt.setListeningAttempt(listeningAttempt);

        attemptRepository.saveTestAttempt(attempt, null, onSuccess, onFailure);
    }
}
