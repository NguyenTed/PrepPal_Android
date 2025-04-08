package com.group5.preppal.ui.quiz.speaking.teacher_speaking;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.BookedTime;
import com.group5.preppal.data.model.SpeakingTest;
import com.group5.preppal.ui.quiz.speaking.SpeakingBookingActivity;
import com.group5.preppal.ui.video_call.VideoCallActivity;
import com.group5.preppal.viewmodel.SpeakingTestViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SpeakingCalendarActivity extends AppCompatActivity {
    private GridView calendarGrid;
    private TextView monthYearText;
    private List<String> dayList;
    private Calendar calendar;
    private String courseId;
    private SpeakingTestViewModel speakingTestViewModel;
    private RecyclerView bookedInfoRecyclerView;
    private BookedInfoAdapter bookedInfoAdapter;
    private ImageButton backButton;
    private ImageButton btnPrevMonth, btnNextMonth;
    private int selectedPosition = -1;
    private Button btnJoin;


    // Backup speaking test
    private SpeakingTest currentTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_calendar_speaking);

        speakingTestViewModel = new ViewModelProvider(this).get(SpeakingTestViewModel.class);


        bookedInfoRecyclerView = findViewById(R.id.bookedInfoRecyclerView);
        bookedInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bookedInfoAdapter = new BookedInfoAdapter((new ArrayList<>()));
        bookedInfoRecyclerView.setAdapter(bookedInfoAdapter);
        courseId = getIntent().getStringExtra("courseId");
        btnJoin = findViewById(R.id.btnJoin);
        calendarGrid = findViewById(R.id.calendarGrid);
        monthYearText = findViewById(R.id.monthYearText);
        btnPrevMonth = findViewById(R.id.btnPrevMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        btnPrevMonth.setOnClickListener(v -> {
            handleChangeMonth(-1);
        });

        btnNextMonth.setOnClickListener(v -> {
            handleChangeMonth(1);
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            this.finish();
        });

        calendar = Calendar.getInstance();

        speakingTestViewModel.getSpeakingTestByCourseId(courseId).observe(this, test -> {
            if (test != null) {
                currentTest = test;
                setUpCalendar(test);
                btnJoin.setOnClickListener(view -> {
                    navigateToSpeakingBookingActivity(test);
                });
            }
        });
    }

    private void setUpCalendar(SpeakingTest test) {
        dayList = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int monthStartDay = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 1 → 0-based
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        selectedPosition = today + monthStartDay - 1;


        for (int i = 0; i < monthStartDay; i++) {
            dayList.add("");
        }

        for (int i = 1; i <= maxDay; i++) {
            dayList.add(String.valueOf(i));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
        monthYearText.setText(sdf.format(calendar.getTime()));

        List<Date> bookedDates = new ArrayList<>();
        if (test.getBookedTime() != null) {
            for (BookedTime booked : test.getBookedTime())
                bookedDates.add(booked.getDate());
        }
        // Set adapter
        CalendarAdapter adapter = new CalendarAdapter(this, dayList, bookedDates, (Calendar) calendar.clone(), selectedPosition);
        calendarGrid.setAdapter(adapter);
        calendarGrid.setOnItemClickListener((parent, view, position, id) -> {
            handleSelectDay(position, test);
        });
        // Tự động hiển thị booked info cho ngày hiện tại
        if (selectedPosition >= 0 && selectedPosition < dayList.size()) {
           handleSelectDay(selectedPosition, test);
        }
    }

    private void handleChangeMonth(int changeNumber) {
        calendar.add(Calendar.MONTH, changeNumber);
        setUpCalendar(currentTest);
    }

    private void handleSelectDay(int position, SpeakingTest test) {
        String selectedDay = dayList.get(position);
        if (!selectedDay.isEmpty()) {
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(selectedDay)); // Set ngày đang được chọn
            int selectedYear = calendar.get(Calendar.YEAR);
            int selectedMonth = calendar.get(Calendar.MONTH);
            int selectedDate = calendar.get(Calendar.DAY_OF_MONTH);

            // Lọc các bookedTime khớp ngày
            List<BookedTime> filteredList = new ArrayList<>();

            for (BookedTime bt : test.getBookedTime()) {
                Calendar btCal = Calendar.getInstance();
                btCal.setTime(bt.getDate());

                boolean isSameDay = btCal.get(Calendar.YEAR) == selectedYear &&
                        btCal.get(Calendar.MONTH) == selectedMonth &&
                        btCal.get(Calendar.DAY_OF_MONTH) == selectedDate;

                if (isSameDay) {
                    filteredList.add(bt);
                }
            }

            bookedInfoAdapter.updateData(filteredList);
            ((CalendarAdapter) calendarGrid.getAdapter()).setSelectedPosition(position);
        }
    }

    private void navigateToSpeakingBookingActivity(SpeakingTest test) {
        Intent intent = new Intent(this, VideoCallActivity.class);
        intent.putExtra("role", "Teacher");
        intent.putExtra("startTime", "12:30");
        intent.putExtra("courseId", courseId);
        intent.putExtra("quizId", test.getId());
        startActivity(intent);
    }
}

