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
        MutableLiveData<List<Vocabulary>> liveData = new MutableLiveData<>();

        db.collection("vocabulary").document(topicId).get()
                .addOnSuccessListener(doc -> {
                    List<Vocabulary> list = new ArrayList<>();

                    List<Map<String, Object>> rawList = (List<Map<String, Object>>) doc.get("vocabularies");
                    if (rawList != null) {
                        for (Map<String, Object> map : rawList) {
                            String word = (String) map.get("word");
                            String phonetic = (String) map.get("phonetic");
                            String audio = (String) map.get("audio");
                            List<String> meanings = (List<String>) map.get("meanings");
                            List<String> examples = (List<String>) map.get("examples");

                            list.add(new Vocabulary(word, phonetic, audio, meanings, examples));
                        }
                    }

                    liveData.setValue(list);
                });

        return liveData;
    }
}
