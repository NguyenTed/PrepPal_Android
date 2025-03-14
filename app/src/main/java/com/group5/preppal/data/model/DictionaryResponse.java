package com.group5.preppal.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictionaryResponse {
    private String word;
    private List<Phonetic> phonetics;
    private List<Meaning> meanings;

    public String getWord() {
        return word;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public List<Phonetic> getPhonetics() {
        List<Phonetic> result = new ArrayList<>();
        for (int i = 0; i < phonetics.size(); i++) {
            if (phonetics.get(i).getAudio() != null && !phonetics.get(i).getAudio().isEmpty() && phonetics.get(i).getText() != null && !phonetics.get(i).getText().isEmpty()) {
                result.add(phonetics.get(i));
            }
        }
        return result;
    }

    public static class Phonetic {
        private String text;
        private String audio;

        public String getText() {
            return text;
        }

        public String getAudio() {
            return audio;
        }
    }

    public static class Meaning {
        private String partOfSpeech;
        private List<Definition> definitions;
        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public List<Definition> getDefinitions() {
            return definitions;
        }

        public static class Definition {
            private String definition;
            private String example;
            private List<String> synonyms;
            private List<String> antonyms;

            public String getDefinition() {
                return definition;
            }

            public String getExample() {
                return example;
            }

            public List<String> getSynonyms() {
                return synonyms;
            }

            public List<String> getAntonyms() {
                return antonyms;
            }
        }
    }
}
