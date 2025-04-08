package com.group5.preppal.ui.quiz.speaking.teacher_speaking;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.group5.preppal.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter  extends BaseAdapter {
    private final Context context;
    private final List<String> dayList;
    private final List<Date> bookedDates;
    private final Calendar currentCalendar;
    private int selectedPosition = -1;

    public CalendarAdapter(Context context, List<String> dayList, List<Date> bookedDates, Calendar currentCalendar, int selectedPosition) {
        this.context = context;
        this.dayList = dayList;
        this.bookedDates = bookedDates;
        this.currentCalendar = currentCalendar;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public int getCount() {
        return dayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean isBooked(int day) {
        for (Date date: bookedDates) {
            Calendar bookedCal = Calendar.getInstance();
            bookedCal.setTime(date);
            if (bookedCal.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
            bookedCal.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
            bookedCal.get(Calendar.DATE) == day) {
                return true;
            }
        }
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_calendar_cell, null) ;
        TextView dayText = view.findViewById(R.id.calendar_day);
        TextView markDot = view.findViewById(R.id.markDot);

        String day = dayList.get(position);
        dayText.setText(day);

        if (!day.isEmpty()) {
            int dayInt = Integer.parseInt(day);
            if (isBooked(dayInt)) {
                markDot.setVisibility(View.VISIBLE);
            } else {
                markDot.setVisibility(View.GONE);
            }
        } else {
            markDot.setVisibility(View.GONE);
        }

        if (position == selectedPosition) {
            dayText.setBackgroundResource(R.drawable.circle_dark_azure_background);
            dayText.setTextColor(context.getColor(R.color.white));
        } else {
            dayText.setBackgroundResource(R.drawable.bg_calendar_day);
            dayText.setTextColor(context.getColor(R.color.black));
        }

        return view;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }


}
