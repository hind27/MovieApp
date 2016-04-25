package com.example.hend.Themovieapp.Models;

public class Review {

    private String id;

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String author;
    private String content;

    public Review() {

    }


    public String getId() { return id; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }
}
