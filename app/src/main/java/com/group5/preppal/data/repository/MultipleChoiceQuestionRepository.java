package com.group5.preppal.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.MultipleChoiceQuestion;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MultipleChoiceQuestionRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference questionCollection;

    @Inject
    public MultipleChoiceQuestionRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
        this.questionCollection = firestore.collection("multiple_choice_questions");
    }

    public LiveData<List<MultipleChoiceQuestion>> getQuestionsByIds(List<String> ids) {
        MutableLiveData<List<MultipleChoiceQuestion>> questionsLiveData = new MutableLiveData<>();

        if (ids == null || ids.isEmpty()) {
            questionsLiveData.setValue(new ArrayList<>());
            return questionsLiveData;
        }

        questionCollection
                .whereIn(FieldPath.documentId(), ids)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<MultipleChoiceQuestion> questions = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        MultipleChoiceQuestion question = doc.toObject(MultipleChoiceQuestion.class);
                        if (question != null) {
                            question.setId(doc.getId()); // nếu cần giữ lại ID
                            questions.add(question);
                        }
                    }
                    questionsLiveData.setValue(questions);
                });

        return questionsLiveData;
    }

}
