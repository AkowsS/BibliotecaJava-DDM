package com.example.bibliotecajava;

import com.example.bibliotecajava.model.Book;
import com.example.bibliotecajava.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private List<User> userList;
    private List<Book> bookList;

    private UserManager() {
        userList = new ArrayList<>();
        bookList = new ArrayList<>();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public List<User> getUsers() {
        return userList;
    }

    public User getUserByEmail(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public boolean authenticateUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null) {
            return password.equals(user.getPassword());
        }
        return false;
    }

    public Book getBookById(int id) {
        for (Book book : bookList) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public void updateBook(Book updatedBook) {
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            if (book.getId() == updatedBook.getId()) {
                bookList.set(i, updatedBook);
                return;
            }
        }
    }


    public void addBook(Book book) {
        bookList.add(book);
    }

    public List<Book> getBooks() {
        return bookList;
    }
}
