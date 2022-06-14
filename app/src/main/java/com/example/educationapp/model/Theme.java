package com.example.educationapp.model;


import android.graphics.Bitmap;

import java.sql.Blob;

public class Theme {

    private String themename;
    private Bitmap visual;

    public Theme(){

    }

    public Theme(String name, Bitmap visual) {

        this.themename = name;
        this.visual = visual;
    }

    public void setThemename(String themename) {
        this.themename = themename;
    }

    public void setVisual(Bitmap visual) {
        this.visual = visual;
    }

    public String getThemename() {
        return themename;
    }

    public Bitmap getVisual() {
        return visual;
    }
}
