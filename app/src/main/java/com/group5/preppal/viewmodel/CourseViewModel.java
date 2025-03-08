package com.group5.preppal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group5.preppal.data.model.Course;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.User;
import com.group5.preppal.data.repository.CourseRepository;
import com.group5.preppal.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CourseViewModel extends ViewModel {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<List<Course>> filteredCoursePayment = new MutableLiveData<>(); // Course payment (not include band under student's current band and student's courses)
    private final MutableLiveData<List<Course>> studentCourses = new MutableLiveData<>(); // Courses that student have bought

    @Inject
    public CourseViewModel(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        loadFilteredCoursePayment();
        loadStudentCourses();
    }

    private void loadFilteredCoursePayment() {
        LiveData<List<Course>> allCourses = courseRepository.getAllCourses();
        LiveData<User> currentUser = userRepository.getCurrentUser();

        allCourses.observeForever(courseList -> {
            Log.d("CourseViewModel", "All courses from Firestore: " + courseList);
            currentUser.observeForever(user -> {
                if (user instanceof Student && courseList != null) {
                    Student student = (Student) user;
//                    Log.d("CourseViewModel", "Current user: " + student.getUid());
//                    Log.d("CourseViewModel", "Student courses: " + student.getCourses());

                    List<String> enrolledCourses = student.getCourses();
                    float currentBand = student.getCurrentBand();
//                    Log.d("CourseViewModel", "Current band" + currentBand);

                    List<Course> filteredList = new ArrayList<>();
                    for (Course course : courseList) {
//                        Log.d("CourseViewModel", "Checking course: " + course.getCourseId());
//                        Log.d("CourseViewModel", "" +  course.getEntryLevel());
                        if (!enrolledCourses.contains(course.getCourseId()) &&
                                course.getEntryLevel() == currentBand) {
                            filteredList.add(course);
                        }
                    }

//                    Log.d("CourseViewModel", "Filtered courses: " + filteredList);
                    filteredCoursePayment.setValue(filteredList);
                } else {
//                    Log.d("CourseViewModel", "User is not a student or no courses found.");
                    filteredCoursePayment.setValue(new ArrayList<>());
                }
            });
        });
    }

    private void loadStudentCourses() {
        LiveData<List<Course>> allCourses = courseRepository.getAllCourses();
        LiveData<User> currentUser = userRepository.getCurrentUser();

        allCourses.observeForever(courseList -> {
            currentUser.observeForever(user -> {
                if (courseList == null) {
                    studentCourses.setValue(new ArrayList<>());
                    return;
                }
                if (user instanceof Student && courseList != null) {
                    Student student = (Student) user;

                    List<String> enrolledCourses = student.getCourses();
                    List<Course> filteredList = new ArrayList<>();
                    for (Course course : courseList) {
                        if (enrolledCourses.contains(course.getCourseId())) {
                            filteredList.add(course);
                        }
                    }
                    studentCourses.setValue(filteredList);
                } else {
                    studentCourses.setValue(new ArrayList<>());
                }
            });
        });
    }


    public LiveData<List<Course>> getFilteredCourses() {
        return filteredCoursePayment;
    }

    public  LiveData<List<Course>> getStudentCourses() {
        return studentCourses;
    }
}


