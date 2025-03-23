package com.group5.preppal.data.model;

import java.util.List;

public class Vocabulary {
    private String word;
    private String phonetic;
    private String audio;
    private List<String> meanings;
    private List<String> examples;

    public Vocabulary () {}

    public Vocabulary(String word, String phonetic, String audio, List<String> meanings, List<String> examples) {
        this.word = word;
        this.phonetic = phonetic;
        this.audio = audio;
        this.meanings = meanings;
        this.examples = examples;
    }

    public String getWord() {
        return word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public String getAudio() {
        return audio;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public List<String> getExamples() {
        return examples;
    }
}
