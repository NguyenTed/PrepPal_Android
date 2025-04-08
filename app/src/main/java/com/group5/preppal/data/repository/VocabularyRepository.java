package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Vocabulary;

import java.util.ArrayList;
import java.util.Collections;
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
                            String partOfSpeech = (String) map.get("partOfSpeech");

                            List<Map<String, Object>> rawMeanings = (List<Map<String, Object>>) map.get("meanings");
                            List<Vocabulary.Meaning> meanings = new ArrayList<>();

                            if (rawMeanings != null) {
                                for (Map<String, Object> meaningMap : rawMeanings) {
                                    String definition = (String) meaningMap.get("definition");
                                    List<String> examples = (List<String>) meaningMap.get("examples");
                                    List<String> synonyms = (List<String>) meaningMap.get("synonyms");
                                    List<String> antonyms = (List<String>) meaningMap.get("antonyms");

                                    meanings.add(new Vocabulary.Meaning(
                                            definition,
                                            examples != null ? examples : new ArrayList<>(),
                                            synonyms != null ? synonyms : new ArrayList<>(),
                                            antonyms != null ? antonyms : new ArrayList<>()
                                    ));
                                }
                            }

                            list.add(new Vocabulary(word, phonetic, audio, partOfSpeech, meanings));
                        }
                    }

                    Collections.shuffle(list); // Optional: shuffle for randomness
                    liveData.setValue(list);
                });

        return liveData;
    }
}
