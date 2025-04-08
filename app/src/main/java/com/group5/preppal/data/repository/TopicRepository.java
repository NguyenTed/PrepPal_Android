package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.TopicWithProgress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopicRepository {
    private final FirebaseFirestore db;

    @Inject
    public TopicRepository(FirebaseFirestore db) {
        this.db = db;
    }

    public LiveData<List<TopicWithProgress>> getTopicsWithProgress(String userId) {
        MutableLiveData<List<TopicWithProgress>> liveData = new MutableLiveData<>();

        db.collection("vocabulary").get().addOnSuccessListener(snapshot -> {
            if (snapshot == null || snapshot.isEmpty()) {
                liveData.setValue(Collections.emptyList());
                return;
            }

            List<TopicWithProgress> result = Collections.synchronizedList(new ArrayList<>());
            List<Task<Void>> progressTasks = new ArrayList<>();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                String topicId = doc.getId();
                String topicName = doc.getString("topic"); // Make sure this exists in Firestore
                List<?> vocabList = (List<?>) doc.get("vocabularies");
                int totalCount = vocabList != null ? vocabList.size() : 0;

                Task<Void> progressTask = db.collection("students")
                        .document(userId)
                        .collection("vocabularyProgress")
                        .document(topicId)
                        .get()
                        .continueWith(task -> {
                            DocumentSnapshot progressDoc = task.getResult();
                            List<String> learned = progressDoc != null ? (List<String>) progressDoc.get("learnedVocabs") : null;
                            int learnedCount = learned != null ? learned.size() : 0;

                            TopicWithProgress topic = new TopicWithProgress(topicId, topicName, learnedCount, totalCount);
                            result.add(topic);

                            return null;
                        });

                progressTasks.add(progressTask);
            }

            Tasks.whenAllComplete(progressTasks).addOnSuccessListener(all -> {
                liveData.setValue(result);
            });
        }).addOnFailureListener(e -> {
            Log.e("TopicRepository", "Failed to fetch topics", e);
            liveData.setValue(Collections.emptyList());
        });

        return liveData;
    }
}