package com.group5.preppal.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group5.preppal.data.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CourseRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference courseCollection;

    @Inject
    public CourseRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
        this.courseCollection = firestore.collection("courses");
    }

    public LiveData<List<Course>> getAllCourses() {
        MutableLiveData<List<Course>> coursesLiveData = new MutableLiveData<>();

        courseCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                if (error != null) {
                    return; // Handle error (có thể log nếu cần)
                }

                if (value != null) {
                    List<Course> courseList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        try {
                            String courseId = document.getId();
                            String name = document.getString("name");
                            String introduction = document.getString("introduction");
                            float entryLevel = document.getDouble("entryLevel").floatValue();
                            float targetLevel = document.getDouble("targetLevel").floatValue();
                            List<Map<String, Object>> sections = (List<Map<String, Object>>) document.get("sections");
                            Course course = new Course(courseId, name, introduction, entryLevel, targetLevel, sections);
                            courseList.add(course);
                        } catch (Exception e) {
                            e.printStackTrace(); // Log lỗi nếu cần
                        }
                    }
                    coursesLiveData.setValue(courseList);
                }
            }
        });

        return coursesLiveData;
    }
    public LiveData<Course> getCourseById(String courseId) {
        MutableLiveData<Course> courseLiveData = new MutableLiveData<>();

        courseCollection.document(courseId).addSnapshotListener((documentSnapshot, error) -> {
            if (error != null || documentSnapshot == null || !documentSnapshot.exists()) {
                return;
            }

            try {
                String name = documentSnapshot.getString("name");
                String introduction = documentSnapshot.getString("introduction");
                float entryLevel = documentSnapshot.getDouble("entryLevel").floatValue();
                float targetLevel = documentSnapshot.getDouble("targetLevel").floatValue();
                List<Map<String, Object>> sections = (List<Map<String, Object>>) documentSnapshot.get("sections");

                Course course = new Course(courseId, name, introduction, entryLevel, targetLevel, sections);
                courseLiveData.setValue(course);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return courseLiveData;
    }

}
