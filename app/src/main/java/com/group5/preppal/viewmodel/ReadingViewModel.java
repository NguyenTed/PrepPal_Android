package com.group5.preppal.viewmodel;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.group5.preppal.data.model.test.reading.ReadingAttempt;
import com.group5.preppal.data.model.test.reading.ReadingGrader;
import com.group5.preppal.data.model.test.reading.ReadingPassage;
import com.group5.preppal.data.model.test.reading.ReadingSection;
import com.group5.preppal.data.repository.practise_test.ReadingAttemptRepository;
import com.group5.preppal.data.repository.practise_test.TestAttemptRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReadingViewModel extends ViewModel {

    private final ReadingAttemptRepository attemptRepository;
    private final TestAttemptRepository testAttemptRepository;

    private final MutableLiveData<Integer> currentPart = new MutableLiveData<>(1);
    private final MutableLiveData<ReadingPassage> currentPassage = new MutableLiveData<>();
    private ReadingSection readingSection;

    private final Map<Integer, String> userAnswers = new HashMap<>();
    private boolean isTimeUp = false;
    private final MutableLiveData<String> timeLeft = new MutableLiveData<>();
    private CountDownTimer timer;

    @Inject
    public ReadingViewModel(ReadingAttemptRepository attemptRepository, TestAttemptRepository testAttemptRepository) {
        this.attemptRepository = attemptRepository;
        this.testAttemptRepository = testAttemptRepository;
    }

    public LiveData<Integer> getCurrentPart() {
        return currentPart;
    }

    public LiveData<ReadingPassage> getCurrentPassage() {
        return currentPassage;
    }

    public void setReadingSection(ReadingSection section) {
        this.readingSection = section;
        loadCurrentPassage();
    }

    public void goToNextPart() {
        if (currentPart.getValue() != null && currentPart.getValue() < 3) {
            currentPart.setValue(currentPart.getValue() + 1);
            loadCurrentPassage();
        }
    }

    public void goToPreviousPart() {
        if (currentPart.getValue() != null && currentPart.getValue() > 1) {
            currentPart.setValue(currentPart.getValue() - 1);
            loadCurrentPassage();
        }
    }

    private void loadCurrentPassage() {
        if (readingSection == null || currentPart.getValue() == null) return;

        switch (currentPart.getValue()) {
            case 1:
                currentPassage.setValue(readingSection.getPassage1());
                break;
            case 2:
                currentPassage.setValue(readingSection.getPassage2());
                break;
            case 3:
                currentPassage.setValue(readingSection.getPassage3());
                break;
        }
    }

    public Map<Integer, String> getUserAnswers() {
        return userAnswers;
    }

    public void updateAnswer(int questionNumber, String answer) {
        userAnswers.put(questionNumber, answer);
    }

    public boolean isTimeUp() {
        return isTimeUp;
    }

    public void setTimeUp(boolean timeUp) {
        this.isTimeUp = timeUp;
    }

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

    public void submitReadingAttempt(
            String testId,
            String testSetId,
            Date startedAt,
            Date submittedAt,
            @NonNull OnSuccessListener<Void> onSuccess,
            @NonNull OnFailureListener onFailure
    ) {
        Log.d("ReadingViewModel", "User Answers: " + userAnswers);
        int rawScore = ReadingGrader.grade(readingSection, userAnswers);
        float bandScore = ReadingGrader.convertRawScoreToBand(rawScore);

        // Firestore maps must have string keys
        Map<String, String> stringAnswers = new HashMap<>();
        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            stringAnswers.put(String.valueOf(entry.getKey()), entry.getValue());
        }

        ReadingAttempt attempt = new ReadingAttempt();
        attempt.setTestId(testId);
        attempt.setTestSetId(testSetId);
        attempt.setAnswers(stringAnswers);
        attempt.setRawScore(rawScore);
        attempt.setBandScore(bandScore);
        attempt.setStartedAt(startedAt);
        attempt.setSubmittedAt(submittedAt);

        attemptRepository.submitReadingAttempt(attempt, onSuccess, onFailure);
        testAttemptRepository.updateSkillBandScore(testId, "reading", bandScore, unused -> Log.d("Firestore", "Listening band score updated"),
                e -> Log.e("Firestore", "Failed to update band score", e));
    }

    @Override
    protected void onCleared() {
        if (timer != null) timer.cancel();
        super.onCleared();
    }
}

