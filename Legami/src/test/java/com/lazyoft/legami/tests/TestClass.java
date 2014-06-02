package com.lazyoft.legami.tests;

public class TestClass {
    public String text;
    public int number;

    private String hidden;
    private String encapsulated;
    private int answer;

    public String getEncapsulated() {
        return encapsulated;
    }

    public void setEncapsulated(String value) {
        encapsulated = value;
    }

    private String getHidden() {
        return hidden;
    }

    private void setHidden(String value) {
        hidden = value;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int value) {
        answer = value;
    }
}
