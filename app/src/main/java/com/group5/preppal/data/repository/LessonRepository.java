package com.group5.preppal.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.Lesson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LessonRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference lessonCollection;

    @Inject
    public LessonRepository (FirebaseFirestore firestore) {
        this.firestore = firestore;
        this.lessonCollection = firestore.collection("lessons");
    }

    public LiveData<Lesson> getLessonById(String lessonId) {
        MutableLiveData<Lesson> lessonLiveData = new MutableLiveData<>();

        lessonCollection.document(lessonId).addSnapshotListener((documentSnapshot, error) -> {
            if (error != null || documentSnapshot == null || !documentSnapshot.exists()) {
                return;
            }

            try {
                String name = documentSnapshot.getString("name");
                String type = documentSnapshot.getString("type");
                String readingUrl = documentSnapshot.getString("readingUrl");
                String videoUrl = documentSnapshot.getString("videoUrl");

                Lesson lesson = new Lesson(videoUrl, type, readingUrl, name, lessonId);
                lessonLiveData.setValue(lesson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return lessonLiveData;
    }
}
