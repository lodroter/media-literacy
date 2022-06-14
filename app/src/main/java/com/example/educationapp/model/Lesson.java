package com.example.educationapp.model;


public class Lesson {

    private String name;
    private String tip;
    private String definition;
    private String content;
    private byte[] visual;
    private byte[] photo;
    private byte[] example;

    public String getName() {
        return name;
    }

    public String getTip() {
        return tip;
    }

    public String getDefinition() {
        return definition;
    }

    public String getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getVisual() {
        return visual;
    }

    public void setVisual(byte[] visual) {
        this.visual = visual;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getExample() {
        return example;
    }

    public void setExample(byte[] example) {
        this.example = example;
    }
}
