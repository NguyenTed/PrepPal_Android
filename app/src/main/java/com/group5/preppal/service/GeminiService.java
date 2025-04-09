package com.group5.preppal.service;

import android.util.Log;

import com.group5.preppal.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GeminiService {

    private static final String API_KEY = BuildConfig.GEMINI_API_KEY;
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    public static String generateFeedback(String studentEssay) {
        String prompt = "You are an IELTS Writing examiner. Please read the following essay written by a student, " +
                "and provide constructive feedback to help improve it. Highlight grammar, vocabulary, coherence, and overall structure. " +
                "Also give suggestions for a better version. Just answer directly, don't respond to me.\n\nStudent's essay:\n\n" + studentEssay;

        return callGeminiAPI(prompt);
    }

    public static String generateGrade(String studentEssay) {
        String prompt = "You are an IELTS examiner. Please read the following essay and provide ONLY the estimated IELTS writing band score (e.g., '6.0', '7.5'). " +
                "Do not include any explanation or comments.\n\nStudent's essay:\n\n" + studentEssay;

        return callGeminiAPI(prompt);
    }

    private static String callGeminiAPI(String prompt) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject textObject = new JSONObject();
            textObject.put("text", prompt);

            JSONArray partsArray = new JSONArray();
            partsArray.put(textObject);

            JSONObject contentObject = new JSONObject();
            contentObject.put("parts", partsArray);

            JSONArray contentsArray = new JSONArray();
            contentsArray.put(contentObject);

            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", contentsArray);

            OutputStream os = connection.getOutputStream();
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            br.close();

            JSONObject root = new JSONObject(response.toString());
            JSONArray candidates = root.getJSONArray("candidates");
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            JSONObject part = parts.getJSONObject(0);

            return part.getString("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi gọi Gemini API: " + e.getMessage();
        }
    }
}
