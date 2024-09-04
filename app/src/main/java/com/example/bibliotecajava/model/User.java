package com.example.bibliotecajava.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String email;
    private String password;
    private byte[] profileImageUri;
    private List<Book> bookList;

    public User(String name, String email, String password, byte[] profileImageUri) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageUri = profileImageUri;
        this.bookList = new ArrayList<>();
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(byte[] profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void addBook(Book book) {
        this.bookList.add(book);
    }
}
