package com.example.android.notesy;

public class Notes {
    private String tittle;
    private String content;

    public Notes() {
    }

    public Notes(String tittle, String content) {
        this.tittle = tittle;
        this.content = content;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
