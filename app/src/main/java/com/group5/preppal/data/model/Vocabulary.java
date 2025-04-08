package com.group5.preppal.data.model;

import java.util.List;

public class Vocabulary {
    private String word;
    private String phonetic;
    private String audio;
    private String partOfSpeech;
    private List<Meaning> meanings;

    public Vocabulary () {}

    public Vocabulary(String word, String phonetic, String audio, String partOfSpeech, List<Meaning> meanings) {
        this.word = word;
        this.phonetic = phonetic;
        this.audio = audio;
        this.partOfSpeech = partOfSpeech;
        this.meanings = meanings;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
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

    public List<Vocabulary.Meaning> getMeanings() {
        return meanings;
    }

    public static class Meaning {
        private String definition;
        private List<String> examples;
        private List<String> synonyms;
        private List<String> antonyms;

        public Meaning() {}

        public Meaning(String definition, List<String> examples, List<String> synonyms, List<String> antonyms) {
            this.definition = definition;
            this.examples = examples;
            this.synonyms = synonyms;
            this.antonyms = antonyms;
        }

        public String getDefinition() {
            return definition;
        }

        public List<String> getExamples() {
            return examples;
        }

        public List<String> getSynonyms() {
            return synonyms;
        }

        public List<String> getAntonyms() {
            return antonyms;
        }
    }
}
