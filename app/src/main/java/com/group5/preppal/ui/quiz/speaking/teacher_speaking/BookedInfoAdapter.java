package com.group5.preppal.ui.quiz.speaking.teacher_speaking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.BookedTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class BookedInfoAdapter extends RecyclerView.Adapter<BookedInfoAdapter.BookedInfoViewHolder>{
    private List<BookedTime> bookedTimeList;

    public BookedInfoAdapter(List<BookedTime> bookedTimeList) {
        this.bookedTimeList = bookedTimeList;
    }

    public void updateData(List<BookedTime> newData) {
        newData.sort(Comparator.comparing(BookedTime::getDate));
        this.bookedTimeList = newData;
        notifyDataSetChanged(); // Refresh lại giao diện
    }

    @NonNull
    @Override
    public BookedInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_booked_day, parent, false);
        return new BookedInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedInfoAdapter.BookedInfoViewHolder holder, int position) {
        BookedTime bookedTime = bookedTimeList.get(position);
        holder.tvStudentId.setText("Student - " + bookedTime.getStudentId());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeStr = timeFormat.format(bookedTime.getDate());
        holder.tvBookedTime.setText(timeStr);

        // AM/PM indicator
        SimpleDateFormat amPmFormat = new SimpleDateFormat("a", Locale.ENGLISH);
        String amPm = amPmFormat.format(bookedTime.getDate());
        holder.tvAmPmIndicator.setText(amPm.toLowerCase()); // "am" or "pm"

        // Range 30 phút: Tính toán giờ kết thúc
        Calendar cal = Calendar.getInstance();
        cal.setTime(bookedTime.getDate());
        String startTime = timeFormat.format(cal.getTime());
        cal.add(Calendar.MINUTE, 30); // +30 phút
        String endTime = timeFormat.format(cal.getTime());
        holder.bookedRangeTime.setText(startTime + " - " + endTime);
    }

    @Override
    public int getItemCount() {
        return bookedTimeList.size();
    }

    public static class BookedInfoViewHolder extends  RecyclerView.ViewHolder {
        TextView tvStudentId, tvBookedTime, tvAmPmIndicator, bookedRangeTime;
        public BookedInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvBookedTime = itemView.findViewById(R.id.tvBookedTime);
            tvAmPmIndicator = itemView.findViewById(R.id.tvAmPmIndicator);
            bookedRangeTime = itemView.findViewById(R.id.bookedRangeTime);
        }
    }
}
