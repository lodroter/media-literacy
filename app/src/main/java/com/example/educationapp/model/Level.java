package com.example.educationapp.model;

import androidx.annotation.NonNull;

public enum Level {

    LEVEL1("Level 1"), LEVEL2("Level 2"), LEVEL3("Level 3"), LEVEL4("Level 4");

    private final String level;

    Level(String category) { this.level = category; }

    @NonNull
    @Override
    public String toString() { return level; }
}
