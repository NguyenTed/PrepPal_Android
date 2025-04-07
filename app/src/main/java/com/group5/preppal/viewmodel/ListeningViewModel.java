package com.group5.preppal.viewmodel;

import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.group5.preppal.data.model.test.listening.ListeningAttempt;
import com.group5.preppal.data.model.test.listening.ListeningGrader;
import com.group5.preppal.data.model.test.listening.ListeningPart;
import com.group5.preppal.data.model.test.listening.ListeningQuestion;
import com.group5.preppal.data.model.test.listening.ListeningQuestionGroup;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.data.repository.practise_test.ListeningAttemptRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ListeningViewModel extends ViewModel {

    private final ListeningAttemptRepository attemptRepository;

    private final MutableLiveData<Integer> currentPart = new MutableLiveData<>(1);
    private final MutableLiveData<ListeningPart> currentPartData = new MutableLiveData<>();
    private ListeningSection listeningSection;

    private final Map<Integer, String> userAnswers = new HashMap<>();
    private boolean isTimeUp = false;
    private final MutableLiveData<String> timeLeft = new MutableLiveData<>();
    private CountDownTimer timer;

    // Meta
    private String testId;
    private String testSetId;
    private Date startedAt;

    @Inject
    public ListeningViewModel(ListeningAttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    // --- Meta ---
    public void setMeta(String testId, String testSetId, Date startedAt) {
        this.testId = testId;
        this.testSetId = testSetId;
        this.startedAt = startedAt;
    }

    // --- Section Setup ---
    public void setListeningSection(ListeningSection section) {
        this.listeningSection = section;
        loadCurrentPart();
    }

    // --- Navigation ---
    public LiveData<Integer> getCurrentPart() {
        return currentPart;
    }

    public LiveData<ListeningPart> getCurrentPartData() {
        return currentPartData;
    }

    public void goToNextPart() {
        if (currentPart.getValue() != null && currentPart.getValue() < 4) {
            currentPart.setValue(currentPart.getValue() + 1);
            loadCurrentPart();
        }
    }

    public void goToPreviousPart() {
        if (currentPart.getValue() != null && currentPart.getValue() > 1) {
            currentPart.setValue(currentPart.getValue() - 1);
            loadCurrentPart();
        }
    }

    private void loadCurrentPart() {
        if (listeningSection == null || currentPart.getValue() == null) return;

        ListeningPart part = null;
        switch (currentPart.getValue()) {
            case 1: part = listeningSection.getPart1(); break;
            case 2: part = listeningSection.getPart2(); break;
            case 3: part = listeningSection.getPart3(); break;
            case 4: part = listeningSection.getPart4(); break;
        }

        currentPartData.setValue(part);
    }

    // --- Answers ---
    public Map<Integer, String> getUserAnswers() {
        return userAnswers;
    }

    public void updateAnswer(int questionNumber, String answer) {
        userAnswers.put(questionNumber, answer);
    }

    // --- Timer ---
    public LiveData<String> getTimeLeft() {
        return timeLeft;
    }

    public void startTimer(long durationMillis) {
        timer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timeLeft.setValue(time);
            }

            @Override
            public void onFinish() {
                timeLeft.setValue("00:00");
                isTimeUp = true;
            }
        };
        timer.start();
    }

    public boolean isTimeUp() {
        return isTimeUp;
    }

    public void setTimeUp(boolean timeUp) {
        this.isTimeUp = timeUp;
    }

    // --- Submission ---
    public void submitListeningAttempt(
            @NonNull Date submittedAt,
            @NonNull OnSuccessListener<Void> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        int rawScore = ListeningGrader.grade(listeningSection, userAnswers);
        float bandScore = ListeningGrader.convertRawScoreToBand(rawScore);

        Map<String, String> stringAnswers = new HashMap<>();
        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            stringAnswers.put(String.valueOf(entry.getKey()), entry.getValue());
        }

        ListeningAttempt attempt = new ListeningAttempt();
        attempt.setTestId(testId);
        attempt.setTestSetId(testSetId);
        attempt.setAnswers(stringAnswers);
        attempt.setRawScore(rawScore);
        attempt.setBandScore(bandScore);
        attempt.setStartedAt(startedAt);
        attempt.setSubmittedAt(submittedAt);

        attemptRepository.submitListeningAttempt(attempt, onSuccess, onFailure);
    }

    @Override
    protected void onCleared() {
        if (timer != null) timer.cancel();
        super.onCleared();
    }
}
