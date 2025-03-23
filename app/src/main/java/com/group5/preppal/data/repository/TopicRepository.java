package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Topic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopicRepository {
    private final FirebaseFirestore db;

    @Inject
    public TopicRepository(FirebaseFirestore db) {
        this.db = db;
    }

    public LiveData<List<Topic>> getTopics() {
        MutableLiveData<List<Topic>> topicsLiveData = new MutableLiveData<>();

        db.collection("vocabulary").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Topic> topicList = new ArrayList<>();

                    for (DocumentSnapshot doc : querySnapshot) {
                        Topic topic = doc.toObject(Topic.class);
                        topicList.add(topic);
                    }

                    topicsLiveData.setValue(topicList);
                    Log.d("TopicRepository", "Loading topics successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("TopicRepository", "Error loading topics", e);
                    topicsLiveData.setValue(null);
                });

        return topicsLiveData;
    }
}
