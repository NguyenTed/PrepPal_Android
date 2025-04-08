package com.group5.preppal.ui.quiz.speaking;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.group5.preppal.R;
import com.group5.preppal.data.model.StudentBookedSpeaking;
import com.group5.preppal.data.model.BookedTime;
import com.group5.preppal.data.model.SpeakingTest;
import com.group5.preppal.data.repository.AuthRepository;
import com.group5.preppal.ui.course.CourseDetailActivity;
import com.group5.preppal.viewmodel.SpeakingTestViewModel;
import com.group5.preppal.viewmodel.StudentViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BookingSpeakingFragment extends Fragment {
    private SpeakingTestViewModel speakingTestViewModel;
    private TextView morningSlot, afternoonSlot;
    private String selectedHour = null;
    private Calendar selectedDate = null;
    private Button btnBook;
    @Inject
    AuthRepository authRepository;
    private FirebaseUser user;
    private StudentViewModel studentViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_speaking, container, false);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        morningSlot = view.findViewById(R.id.tvMorningSlot);
        afternoonSlot = view.findViewById(R.id.tvAfternoonSlot);
        user = authRepository.getCurrentUser();
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        btnBook = getActivity().findViewById(R.id.btnBook);
        setButtonDisable(btnBook);

        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());

        String speakingTestId = getActivity().getIntent().getStringExtra("quizId");
        speakingTestViewModel = new ViewModelProvider(this).get(SpeakingTestViewModel.class);
        speakingTestViewModel.getSpeakingTestById(speakingTestId).observe(getViewLifecycleOwner(), test -> {
            ((SpeakingBookingActivity) getActivity()).updateTitle(test.getName(), test.getType());
            morningSlot.setText(test.getAvailableMorningTime());
            afternoonSlot.setText(test.getAvailableAfternoonTime());
            // Lấy ngày hôm nay từ calendarView
            Calendar initialSelectedDate = Calendar.getInstance();
            initialSelectedDate.setTimeInMillis(calendarView.getDate());

            // Gọi hàm xử lý giống như khi chọn ngày
            handleDateSelection(initialSelectedDate, test);
            calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                Calendar selectedCal = Calendar.getInstance();
                selectedCal.set(year, month, dayOfMonth);
                handleDateSelection(selectedCal, test);
            });

            btnBook.setOnClickListener(v -> {
                if (selectedHour != null && selectedDate != null) {
                    String[] parts = selectedHour.split(":");
                    int hour = Integer.parseInt(parts[0]);
                    int minute = Integer.parseInt(parts[1]);
                    selectedDate.set(Calendar.HOUR_OF_DAY, hour);
                    selectedDate.set(Calendar.MINUTE, minute);
                    selectedDate.set(Calendar.SECOND, 0);
                    selectedDate.set(Calendar.MILLISECOND, 0);

                    Date finalDate = selectedDate.getTime();
                    StudentBookedSpeaking studentBookedSpeaking = new StudentBookedSpeaking(finalDate, speakingTestId);
                    studentViewModel.saveBookedSpeaking(user.getUid(), studentBookedSpeaking);
                    speakingTestViewModel.saveBookedTime(user.getUid(), studentBookedSpeaking);

                    Toast.makeText(requireContext(), "Booked successfully: " + finalDate.toString(), Toast.LENGTH_SHORT).show();
                    navigateToCoursePage();
                }
            });
        });

        return view;
    }
    private void handleDateSelection(Calendar selectedCal, SpeakingTest test) {
        setButtonDisable(btnBook);
        selectedHour = null;
        selectedDate = selectedCal;
        boolean morningExpired = isSlotExpired(selectedCal, test.getAvailableMorningTime());
        boolean afternoonExpired = isSlotExpired(selectedCal, test.getAvailableAfternoonTime());

        morningSlot.setEnabled(!morningExpired);
        afternoonSlot.setEnabled(!afternoonExpired);
        morningSlot.setAlpha(morningExpired ? 0.5f : 1f);
        afternoonSlot.setAlpha(afternoonExpired ? 0.5f : 1f);

        int selectedDay = selectedCal.get(Calendar.DAY_OF_MONTH);
        int selectedMonth = selectedCal.get(Calendar.MONTH);
        int selectedYear = selectedCal.get(Calendar.YEAR);

        boolean morningBooked = false;
        boolean afternoonBooked = false;
        List<BookedTime> bookedTimes = test.getBookedTime();
        String morningTime = test.getAvailableMorningTime();
        String afternoonTime = test.getAvailableAfternoonTime();

        if (bookedTimes != null) {
            for (BookedTime bookedTime : bookedTimes) {
                Calendar bookedCal = Calendar.getInstance();
                Date date = bookedTime.getDate();
                if (date != null) {
                    bookedCal.setTime(date);
                } else {
                    Log.e("Booking", "Ngày bị null, không thể setTime");
                }


                boolean sameDay = bookedCal.get(Calendar.DAY_OF_MONTH) == selectedDay &&
                        bookedCal.get(Calendar.MONTH) == selectedMonth &&
                        bookedCal.get(Calendar.YEAR) == selectedYear;

                if (sameDay) {
                    int hour = bookedCal.get(Calendar.HOUR_OF_DAY);
                    int minute = bookedCal.get(Calendar.MINUTE);
                    String hourFormatted = String.format("%02d:%02d", hour, minute);
                    if (hourFormatted.equals(morningTime)) morningBooked = true;
                    if (hourFormatted.equals(afternoonTime)) afternoonBooked = true;
                }
            }
        }

        morningSlot.setBackgroundResource(morningBooked ? R.drawable.rounded_10dp_red : R.drawable.rounded_10dp_green);
        afternoonSlot.setBackgroundResource(afternoonBooked ? R.drawable.rounded_10dp_red : R.drawable.rounded_10dp_green);

        boolean finalMorningBooked = morningBooked;
        boolean finalAfternoonBooked = afternoonBooked;

        morningSlot.setOnClickListener(v -> {
            if (!finalMorningBooked) {
                selectedHour = morningTime;
                setButtonEnable(btnBook);
                morningSlot.setBackgroundResource(R.drawable.rounded_10dp_green_border_blue);
                afternoonSlot.setBackgroundResource(finalAfternoonBooked ? R.drawable.rounded_10dp_red : R.drawable.rounded_10dp_green);
            }
        });

        afternoonSlot.setOnClickListener(v -> {
            if (!finalAfternoonBooked) {
                selectedHour = afternoonTime;
                setButtonEnable(btnBook);
                afternoonSlot.setBackgroundResource(R.drawable.rounded_10dp_green_border_blue);
                morningSlot.setBackgroundResource(finalMorningBooked ? R.drawable.rounded_10dp_red : R.drawable.rounded_10dp_green);
            }
        });
    }

    private boolean isSlotExpired(Calendar selectedDate, String timeStr) {
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        Calendar slotTime = (Calendar) selectedDate.clone();
        slotTime.set(Calendar.HOUR_OF_DAY, hour);
        slotTime.set(Calendar.MINUTE, minute);
        slotTime.set(Calendar.SECOND, 0);
        slotTime.set(Calendar.MILLISECOND, 0);

        return Calendar.getInstance().after(slotTime);
    }


    private void setButtonDisable(Button button) {
        button.setEnabled(false);
        button.setBackgroundResource(R.drawable.rounded_10dp_gray_deep);
        button.setTextColor(Color.parseColor("#000000"));
    }
    private void setButtonEnable(Button button) {
        button.setEnabled(true);
        button.setBackgroundResource(R.drawable.rounded_10dp_green);
        button.setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void navigateToCoursePage() {
        Intent intent = new Intent(requireContext(), CourseDetailActivity.class);
        intent.putExtra("courseId", getActivity().getIntent().getStringExtra("courseId"));
        startActivity(intent);
    }

    private String setFormatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
    }
}