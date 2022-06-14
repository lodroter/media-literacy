package com.example.educationapp.model;

public class Statistics {

    private Long answered;
    private Long correct;
    private Long wrong;
    private String best_lesson;
    private String worst_lesson;
    private String best_score;
    private String worst_score;


    public Long getAnswered() {
        return answered;
    }

    public void setAnswered(Long answered) {
        this.answered = answered;
    }

    public Long getCorrect() {
        return correct;
    }

    public void setCorrect(Long correct) {
        this.correct = correct;
    }

    public Long getWrong() {
        return wrong;
    }

    public void setWrong(Long wrong) {
        this.wrong = wrong;
    }

    public String getBest_lesson() {
        return best_lesson;
    }

    public void setBest_lesson(String best_lesson) {
        this.best_lesson = best_lesson;
    }

    public String getWorst_lesson() {
        return worst_lesson;
    }

    public void setWorst_lesson(String worst_lesson) {
        this.worst_lesson = worst_lesson;
    }

    public String getBest_score() {
        return best_score;
    }

    public void setBest_score(String best_score) {
        this.best_score = best_score;
    }

    public String getWorst_score() {
        return worst_score;
    }

    public void setWorst_score(String worst_score) {
        this.worst_score = worst_score;
    }
}
