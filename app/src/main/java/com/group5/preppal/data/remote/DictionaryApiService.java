package com.group5.preppal.data.remote;

import com.group5.preppal.data.model.DictionaryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApiService {
    @GET("en/{word}")  // Base URL will be set later in Retrofit instance
    Call<List<DictionaryResponse>> getWordDefinition(@Path("word") String word);
}
