package com.group5.preppal.data.model;

public class Lesson {
  protected  String lessonId;
  protected  String name;
  protected  String readingUrl;
  protected  String type;
  protected String videoUrl;

    public Lesson(String videoUrl, String type, String readingUrl, String name, String lessonId) {
        this.videoUrl = videoUrl;
        this.type = type;
        this.readingUrl = readingUrl;
        this.name = name;
        this.lessonId = lessonId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReadingUrl() {
        return readingUrl;
    }

    public void setReadingUrl(String readingUrl) {
        this.readingUrl = readingUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
