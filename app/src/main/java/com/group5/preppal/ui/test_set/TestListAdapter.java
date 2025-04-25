package com.group5.preppal.ui.test_set;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.preppal.R;
import com.group5.preppal.data.model.test.Test;
import com.group5.preppal.data.model.test.TestAttempt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestViewHolder> {

    private final List<Test> testList = new ArrayList<>();
    private final OnTestClickListener listener;
    private Map<String, TestAttempt> testAttemptsMap = new HashMap<>();

    public interface OnTestClickListener {
        void onTestClick(Test test);
    }
    public Bitmap createBitmapWithText(String text) {
        int width = 120;  // Chiều rộng của hình tròn
        int height = 120; // Chiều cao của hình tròn
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Vẽ background hình tròn
        Paint paint = new Paint();
        paint.setColor(Color.WHITE); // Màu cam cho viền
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, width / 2 -10, paint);

        // Vẽ chữ (số điểm)
        paint.setColor(Color.parseColor("#2196F3"));   // Màu trắng cho chữ
        paint.setTextSize(40f);  // Kích thước chữ
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, width / 2, height / 2 + (paint.getTextSize() / 4), paint);

        return bitmap;
    }
    public TestListAdapter(OnTestClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Test> newTests) {
        testList.clear();
        testList.addAll(newTests);
        notifyDataSetChanged();
    }

    public void setTestAttemptsMap(Map<String, TestAttempt> map) {
        this.testAttemptsMap = map != null ? map : new HashMap<>();
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_2, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test test = testList.get(position);
        holder.testTitle.setText(test.getName());

        TestAttempt attempt = testAttemptsMap.get(test.getId());

        // Kiểm tra điểm nếu có, nếu không có thì giữ icon
        String lScore = (attempt != null && attempt.getListeningBandScore() != null)
                ? String.format(Locale.US, "%.1f", attempt.getListeningBandScore())
                : null; // null nếu không có điểm

        String rScore = (attempt != null && attempt.getReadingBandScore() != null)
                ? String.format(Locale.US, "%.1f", attempt.getReadingBandScore())
                : null;

        // Set scores into buttons if available, otherwise keep the icon
        if (lScore != null) {
            Bitmap bitmap = createBitmapWithText(lScore);  // Tạo bitmap với số điểm
            holder.listeningScoreButton.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(holder.itemView.getContext().getResources(), bitmap), null, null, null);
        } else {
            holder.listeningScoreButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_listening_padded, 0, 0, 0); // Keep icon
        }

        if (rScore != null) {
            Bitmap bitmap = createBitmapWithText(rScore);  // Tạo bitmap với số điểm
            holder.readingScoreButton.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(holder.itemView.getContext().getResources(), bitmap), null, null, null);
        } else {
            holder.readingScoreButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_reading_padded, 0, 0, 0); // Keep icon
        }

        // Nếu bạn muốn xử lý Writing và Speaking như trên:
        // Cập nhật code tương tự cho Writing và Speaking nếu cần.

        // Open detail activity on item click
        holder.itemView.setOnClickListener(v -> listener.onTestClick(test));
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView testTitle, bandScores;
        Button listeningScoreButton, readingScoreButton, writingScoreButton, speakingScoreButton;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            testTitle = itemView.findViewById(R.id.testTitleTextView);
            listeningScoreButton = itemView.findViewById(R.id.listeningScoreButton);
            readingScoreButton = itemView.findViewById(R.id.readingScoreButton);
            writingScoreButton = itemView.findViewById(R.id.writingScoreButton);
            speakingScoreButton = itemView.findViewById(R.id.speakingScoreButton);
        }
    }
}
