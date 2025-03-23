package com.group5.preppal.data.repository;

import com.group5.preppal.data.model.DictionaryResponse;
import com.group5.preppal.data.remote.DictionaryApiService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DictionaryRepository {
    private final DictionaryApiService apiService;

    @Inject
    public DictionaryRepository(DictionaryApiService apiService) {
        this.apiService = apiService;
    }

    public void fetchWordDefinition(String word, DictionaryCallback callback) {
        apiService.getWordDefinition(word).enqueue(new Callback<List<DictionaryResponse>>() {
            @Override
            public void onResponse(Call<List<DictionaryResponse>> call, Response<List<DictionaryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Word not found!");
                }
            }

            @Override
            public void onFailure(Call<List<DictionaryResponse>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}

