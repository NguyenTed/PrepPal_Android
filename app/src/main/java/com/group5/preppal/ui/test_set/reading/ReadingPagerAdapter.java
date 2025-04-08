package com.group5.preppal.ui.test_set.reading;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group5.preppal.R;
import com.group5.preppal.data.model.test.reading.ReadingPassage;
import com.group5.preppal.data.model.test.reading.ReadingQuestionGroup;

import java.util.List;
import java.util.Map;

public class ReadingPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PASSAGE = 0;
    private static final int TYPE_QUESTIONS = 1;

    private final ReadingPassage passage;
    private final Map<Integer, String> userAnswers;
    private final boolean isTimeUp;

    public ReadingPagerAdapter(ReadingPassage passage, Map<Integer, String> userAnswers, boolean isTimeUp) {
        this.passage = passage;
        this.userAnswers = userAnswers;
        this.isTimeUp = isTimeUp;
    }

    @Override
    public int getItemCount() {
        return 2; // Page 0: questions | Page 1: PDF
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_PASSAGE : TYPE_QUESTIONS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PASSAGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_reading_passages, parent, false);
            return new ImageGroupViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_reading_questions, parent, false);
            return new QuestionGroupViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageGroupViewHolder) {
            ((ImageGroupViewHolder) holder).bind(passage.getPassageImageUrls());
        } else if (holder instanceof QuestionGroupViewHolder) {
            ((QuestionGroupViewHolder) holder).bind(passage.getReadingQuestionGroups(), userAnswers, isTimeUp);
        }
    }

    static class ImageGroupViewHolder extends RecyclerView.ViewHolder {
        LinearLayout imageContainer;

        public ImageGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            imageContainer = itemView.findViewById(R.id.imageContainer);
        }

        public void bind(List<String> imageUrls) {
            imageContainer.removeAllViews();
            Context context = itemView.getContext();

            for (String url : imageUrls) {
                ImageView imageView = new ImageView(context);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 32); // 16dp bottom margin (adjust as needed)
                imageView.setLayoutParams(params);

                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_START);

                Glide.with(context).load(url).into(imageView);
                imageContainer.addView(imageView);
            }
        }
    }

    static class QuestionGroupViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public QuestionGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.readingQuestionRecyclerView);
        }

        public void bind(List<ReadingQuestionGroup> groups, Map<Integer, String> userAnswers, boolean isTimeUp) {
            if (groups == null || groups.isEmpty()) {
                Log.e("ReadingPagerAdapter", "No question groups to display.");
                return;
            }

            Log.d("ReadingPager", "Binding " + groups.size() + " question groups");

            ReadingQuestionGroupAdapter adapter = new ReadingQuestionGroupAdapter();
            adapter.setUserAnswers(userAnswers);
            adapter.setTimeUp(isTimeUp);
            adapter.submitList(groups);

            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerView.setAdapter(adapter);
        }
    }
}
