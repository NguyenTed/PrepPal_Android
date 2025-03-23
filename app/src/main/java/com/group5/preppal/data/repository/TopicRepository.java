package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Topic;
import com.group5.preppal.data.model.TopicWithProgress;

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

    public LiveData<List<TopicWithProgress>> getTopicsWithProgress(String userId) {
        MutableLiveData<List<TopicWithProgress>> liveData = new MutableLiveData<>();

        db.collection("vocabulary").get().addOnSuccessListener(snapshot -> {
            List<TopicWithProgress> result = new ArrayList<>();
            List<Task<DocumentSnapshot>> progressTasks = new ArrayList<>();

            for (DocumentSnapshot doc : snapshot) {
                String topicId = doc.getId();
                String topicName = doc.getString("topic");
                List<?> vocabList = (List<?>) doc.get("vocabularies");
                int totalCount = vocabList != null ? vocabList.size() : 0;

                Task<DocumentSnapshot> progressTask = db.collection("students")
                        .document(userId)
                        .collection("vocabularyProgress")
                        .document(topicId)
                        .get()
                        .addOnSuccessListener(progressDoc -> {
                            List<String> learned = (List<String>) progressDoc.get("learnedVocabs");
                            int learnedCount = learned != null ? learned.size() : 0;

                            result.add(new TopicWithProgress(topicId, topicName, learnedCount, totalCount));
                        });

                progressTasks.add(progressTask);
            }

            Tasks.whenAllComplete(progressTasks).addOnSuccessListener(done -> {
                liveData.setValue(result);
            });

        });

        return liveData;
    }
}
