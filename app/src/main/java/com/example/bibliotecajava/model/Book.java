package com.example.bibliotecajava.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Book {
    private static final AtomicInteger idCounter = new AtomicInteger();
    private int id;
    private String title;
    private String publisher;
    private String genre;
    private byte[] coverImage;

    public Book(String title, String publisher, String genre, byte[] coverImage) {
        this.id = idCounter.incrementAndGet();
        this.title = title;
        this.publisher = publisher;
        this.genre = genre;
        this.coverImage = coverImage;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }
}
