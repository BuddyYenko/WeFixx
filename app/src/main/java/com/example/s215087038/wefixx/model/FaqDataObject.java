package com.example.s215087038.wefixx.model;

import com.google.gson.annotations.SerializedName;

public class FaqDataObject {
    @SerializedName("name")
    private String question;
    private String answer;

    public FaqDataObject() {
    }

    public FaqDataObject(String question, String answer) {
        this.question = question;
        this.answer = answer;

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
