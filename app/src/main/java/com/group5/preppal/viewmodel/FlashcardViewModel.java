package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Vocabulary;
import com.group5.preppal.data.repository.VocabularyProgressRepository;
import com.group5.preppal.data.repository.VocabularyRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FlashcardViewModel extends ViewModel {

    private final VocabularyRepository vocabRepo;
    private final VocabularyProgressRepository progressRepo;

    private final MutableLiveData<List<Vocabulary>> vocabularies = new MutableLiveData<>();

    @Inject
    public FlashcardViewModel(
            VocabularyRepository vocabRepo,
            VocabularyProgressRepository progressRepo
    ) {
        this.vocabRepo = vocabRepo;
        this.progressRepo = progressRepo;
    }

    public void loadUnlearnedVocabularies(String topicId, String userId) {
        MutableLiveData<List<Vocabulary>> result = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Step 1: Get all vocab from topic
        db.collection("vocabulary").document(topicId).get()
                .addOnSuccessListener(doc -> {
                    List<Vocabulary> allVocab = new ArrayList<>();

                    List<Map<String, Object>> raw = (List<Map<String, Object>>) doc.get("vocabularies");
                    if (raw != null) {
                        for (Map<String, Object> map : raw) {
                            String word = (String) map.get("word");
                            String phonetic = (String) map.get("phonetic");
                            String audio = (String) map.get("audio");
                            List<String> meanings = (List<String>) map.get("meanings");
                            List<String> examples = (List<String>) map.get("examples");

                            allVocab.add(new Vocabulary(word, phonetic, audio, meanings, examples));
                        }
                    }

                    // Step 2: Get learned words
                    db.collection("students")
                            .document(userId)
                            .collection("vocabularyProgress")
                            .document(topicId)
                            .get()
                            .addOnSuccessListener(progressDoc -> {
                                List<String> learned = (List<String>) progressDoc.get("learnedVocabs");
                                Set<String> learnedSet = learned != null ? new HashSet<>(learned) : new HashSet<>();

                                // Step 3: Filter
                                List<Vocabulary> unlearned = new ArrayList<>();
                                for (Vocabulary vocab : allVocab) {
                                    if (!learnedSet.contains(vocab.getWord())) {
                                        unlearned.add(vocab);
                                    }
                                }

                                vocabularies.setValue(unlearned);
                            });
                });
    }


    public LiveData<List<Vocabulary>> getVocabularies() {
        return vocabularies;
    }

    public void markWordAsLearned(String userId, String topicId, String word,
                                  Runnable onSuccess, Consumer<Exception> onError) {
        progressRepo.markAsLearned(userId, topicId, word, onSuccess, onError);
    }
}

