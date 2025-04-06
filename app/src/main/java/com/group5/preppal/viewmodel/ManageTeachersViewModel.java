package com.group5.preppal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.Teacher;
import com.group5.preppal.data.repository.TeacherRepository;

import java.util.ArrayList;
import java.util.List;

public class ManageTeachersViewModel extends ViewModel {

    private final TeacherRepository repository = new TeacherRepository();

    private final MutableLiveData<List<Teacher>> allTeachers = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Teacher>> filteredTeachers = new MutableLiveData<>(new ArrayList<>());

    public ManageTeachersViewModel() {
        loadTeachers();
    }

    public void reload() {
        loadTeachers(); // just call the same method again
    }

    public LiveData<List<Teacher>> getFilteredTeachers() {
        return filteredTeachers;
    }

    private void loadTeachers() {
        repository.fetchAllTeachers(teachers -> {
            allTeachers.setValue(teachers);
            filteredTeachers.setValue(teachers);
        });
    }

    public void filter(String query) {
        List<Teacher> all = allTeachers.getValue();
        if (query == null || query.isEmpty()) {
            filteredTeachers.setValue(all);
            return;
        }

        String lower = query.toLowerCase();
        List<Teacher> filtered = new ArrayList<>();
        for (Teacher t : all) {
            if (t.getName().toLowerCase().contains(lower) ||
                    t.getEmail().toLowerCase().contains(lower) ||
                    t.getUid().toLowerCase().contains(lower)) {
                filtered.add(t);
            }
        }
        filteredTeachers.setValue(filtered);
    }
}