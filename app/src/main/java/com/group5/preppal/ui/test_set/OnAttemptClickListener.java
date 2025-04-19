package com.group5.preppal.ui.test_set;

import com.group5.preppal.data.model.test.listening.ListeningAttempt;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.data.model.test.reading.ReadingAttempt;
import com.group5.preppal.data.model.test.reading.ReadingSection;

public interface OnAttemptClickListener {
    void onListeningAttemptClick(ListeningAttempt attempt, ListeningSection section);
    void onReadingAttemptClick(ReadingAttempt attempt, ReadingSection section);
}
