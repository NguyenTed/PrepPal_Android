package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VocabularyRepository {

    private final FirebaseFirestore db;

    @Inject
    public VocabularyRepository(FirebaseFirestore db) {
        this.db = db;
    }

    public LiveData<List<Vocabulary>> getVocabularies(String topicId) {
        MutableLiveData<List<Vocabulary>> vocabLiveData = new MutableLiveData<>();

        db.collection("vocabulary").document(topicId)
                .get()
                .addOnSuccessListener(doc -> {
                    List<Vocabulary> vocabList = new ArrayList<>();

                    if (doc.exists()) {
                        Object raw = doc.get("vocabularies");
                        if (raw instanceof List) {
                            for (Object item : (List<?>) raw) {
                                if (item instanceof Map) {
                                    Map<String, Object> vocabMap = (Map<String, Object>) item;

                                    String word = (String) vocabMap.get("word");
                                    String phonetic = (String) vocabMap.get("phonetic");
                                    String audio = (String) vocabMap.get("audio");

                                    List<String> meanings = vocabMap.get("meanings") instanceof List
                                            ? (List<String>) vocabMap.get("meanings") : new ArrayList<>();

                                    List<String> examples = vocabMap.get("examples") instanceof List
                                            ? (List<String>) vocabMap.get("examples") : new ArrayList<>();

                                    vocabList.add(new Vocabulary(word, phonetic, audio, meanings, examples));
                                }
                            }
                        }
                    }

                    vocabLiveData.setValue(vocabList);
                    Log.d("VocabularyRepository", "Loading vocab successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("VocabularyRepository", "Error loading vocab", e);
                    vocabLiveData.setValue(null);
                });

        return vocabLiveData;
    }
}

