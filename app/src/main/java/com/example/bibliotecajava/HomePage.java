package com.example.bibliotecajava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecajava.model.User;
import com.example.bibliotecajava.model.Book;

import java.util.List;

public class HomePage extends AppCompatActivity {

    private UserManager userManager;
    private User currentUser;
    private ImageView userProfileImage;
    private TextView userName;
    private TextView userEmail;
    private Button addBookButton;
    private ImageView editProfileButton;
    private RecyclerView recyclerViewBooks;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        userManager = UserManager.getInstance();
        String email = getIntent().getStringExtra("USER_EMAIL");

        currentUser = userManager.getUserByEmail(email);

        userProfileImage = findViewById(R.id.user_profile_image);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        addBookButton = findViewById(R.id.addBook);
        editProfileButton = findViewById(R.id.button_edit_profile);
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);

        if (currentUser != null) {
            loadUserData(currentUser);

            recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));

            List<Book> bookList = currentUser.getBookList();
            bookAdapter = new BookAdapter(this, bookList, currentUser.getEmail());
            recyclerViewBooks.setAdapter(bookAdapter);
        }

        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, AddBook.class);
            intent.putExtra("USER_EMAIL", currentUser.getEmail());
            startActivity(intent);
        });

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, EditProfileActivity.class);
            intent.putExtra("USER_EMAIL", currentUser.getEmail());
            startActivity(intent);
        });
    }

    private void loadUserData(User user) {
        userName.setText(user.getName());
        userEmail.setText(user.getEmail());

        if (user.getProfileImageUri() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getProfileImageUri(), 0, user.getProfileImageUri().length);
            userProfileImage.setImageBitmap(bitmap);
        } else {
            userProfileImage.setImageResource(R.drawable.baseline_person_24);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HomePage.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
