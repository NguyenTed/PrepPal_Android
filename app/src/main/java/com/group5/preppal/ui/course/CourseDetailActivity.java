package com.group5.preppal.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.preppal.R;
import com.group5.preppal.data.model.Course;
import com.group5.preppal.data.model.Student;
import com.group5.preppal.data.model.enums.CourseSectionType;
import com.group5.preppal.utils.HandleFinish;
import com.group5.preppal.viewmodel.CourseViewModel;
import com.group5.preppal.viewmodel.StudentViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseDetailActivity extends AppCompatActivity {
    private String courseId;
    private CourseViewModel courseViewModel;
    private TextView courseName, courseLevel, txtIntroduction;
    private ImageButton backBtn;
    private RecyclerView recyclerViewSections;
    private SectionAdapter sectionAdapter;
    private StudentViewModel studentViewModel;
    private LinearLayout congratulation;

    @Inject
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseId = getIntent().getStringExtra("courseId");
        if (courseId == null) {
            Toast.makeText(this, "Course ID is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        congratulation = findViewById(R.id.congratulation);
        courseName = findViewById(R.id.courseName);
        courseLevel = findViewById(R.id.courseLevel);
        txtIntroduction = findViewById(R.id.txtIntroduction);
        recyclerViewSections = findViewById(R.id.recyclerViewSections);
        recyclerViewSections.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CourseListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        String studentId = firebaseAuth.getCurrentUser().getUid();

        studentViewModel.getStudentById(studentId).observe(this, student -> {
            if (student == null) return;
            HandleFinish.HandleFinishSpeakingTest(student);
            fetchCourseAndSetupUI(student);
        });
    }

    private void fetchCourseAndSetupUI(Student student) {
        final List<String> finishedLessons = student.getFinishedLessons() != null ? student.getFinishedLessons() : new ArrayList<>();
        final List<String> finishedSpeakingTests = student.getFinishedSpeakingTests() != null ? student.getFinishedSpeakingTests() : new ArrayList<>();
        final List<String> finishedCourses = student.getFinishedCourses() != null ? student.getFinishedCourses() : new ArrayList<>();

        final Student finalStudent = student; // 👈 tạo biến final cho student

        courseViewModel.fetchCourseById(courseId);
        courseViewModel.getSelectedCourse().observe(this, course -> {
            if (course != null) {
                courseName.setText(course.getName());
                courseLevel.setText("Entry Level: " + course.getEntryLevel() + " - " + (course.getEntryLevel() + 0.5) +
                        " / Target Level: " + (course.getTargetLevel() == 6.5 ? "6.5 - 7.0+" : course.getTargetLevel() + "+"));
                txtIntroduction.setText(course.getIntroduction());

                List<Map<String, Object>> sections = course.getSections();
                if (sections != null && !sections.isEmpty()) {
                    sectionAdapter = new SectionAdapter(
                            sections,
                            this,
                            courseId,
                            firebaseAuth,
                            this,
                            finishedLessons,
                            finishedSpeakingTests,
                            studentViewModel
                    );
                    recyclerViewSections.setAdapter(sectionAdapter);

                    // ⬇ Bây giờ dùng biến final
                    checkAndFinishCourse(finalStudent, course, sections, finishedLessons, finishedSpeakingTests, finishedCourses);
                } else {
                    Log.e("CourseDetailActivity", "Sections is null or empty!");
                }
            }
        });
    }


    private void checkAndFinishCourse(Student student, Course course, List<Map<String, Object>> sections, List<String> finishedLessons, List<String> finishedSpeakingTests, List<String> finishedCourses) {
        boolean allCompleted = true;

        for (Map<String, Object> section : sections) {
            if (section.containsKey("lesson")) {
                Map<String, Object> lesson = (Map<String, Object>) section.get("lesson");
                String lessonId = lesson.get("id").toString();
                if (!finishedLessons.contains(lessonId)) {
                    allCompleted = false;
                    break;
                }
            } else if (section.containsKey("quiz")) {
                Map<String, Object> quiz = (Map<String, Object>) section.get("quiz");
                String type = quiz.get("type").toString();
                String quizId = quiz.get("id").toString();

                if (type.contains(CourseSectionType.SPEAKING.getDisplayName())) {
                    if (!finishedSpeakingTests.contains(quizId)) {
                        allCompleted = false;
                        break;
                    }
                }
            }
        }

        if (allCompleted) {
            if (!finishedCourses.contains(courseId)) {
                finishedCourses.add(courseId);
                student.setFinishedCourses(finishedCourses);
                student.setCurrentBand(course.getTargetLevel());

                studentViewModel.updateStudent(student).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "🎉 Congratulation you on your course's finish", Toast.LENGTH_LONG).show();
                    recyclerViewSections.postDelayed(() -> {
                        Intent intent = new Intent(CourseDetailActivity.this, CourseListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }, 1500);
                }).addOnFailureListener(e -> {
                    Log.e("CourseDetailActivity", "Error updating finished course: ", e);
                    Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                });
            } else {
                congratulation.setVisibility(View.VISIBLE);
            }
        }
    }

}



