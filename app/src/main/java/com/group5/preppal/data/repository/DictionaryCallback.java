package com.group5.preppal.data.repository;

import com.group5.preppal.data.model.DictionaryResponse;

import java.util.List;

public interface DictionaryCallback {
    void onSuccess(List<DictionaryResponse> response);
    void onFailure(String error);
}
