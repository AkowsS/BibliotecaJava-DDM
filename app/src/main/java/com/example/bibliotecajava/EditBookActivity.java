package com.example.bibliotecajava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotecajava.model.Book;
import com.example.bibliotecajava.model.User;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class EditBookActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextPublisher, editTextGenre;
    private ImageView imageViewCover;
    private Button btnSaveChanges, buttonBack;
    private Book currentBook;
    private UserManager userManager;
    private User currentUser;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextPublisher = findViewById(R.id.editTextPublisher);
        editTextGenre = findViewById(R.id.editTextGenre);
        imageViewCover = findViewById(R.id.imageViewCover);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        buttonBack = findViewById(R.id.buttonBack);

        userManager = UserManager.getInstance();

        String email = getIntent().getStringExtra("USER_EMAIL");
        currentUser = userManager.getUserByEmail(email);

        if (currentUser == null) {
            Toast.makeText(this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int bookId = getIntent().getIntExtra("BOOK_ID", -1);
        if (bookId != -1) {
            currentBook = userManager.getBookById(bookId);
            if (currentBook != null) {
                loadBookData(currentBook);
            } else {
                Toast.makeText(this, "Livro não encontrado!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID do livro inválido!", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSaveChanges.setOnClickListener(v -> saveBookChanges());
        buttonBack.setOnClickListener(v -> finish());

        imageViewCover.setOnClickListener(v -> selectImage());
    }

    private void loadBookData(Book book) {
        editTextTitle.setText(book.getTitle());
        editTextPublisher.setText(book.getPublisher());
        editTextGenre.setText(book.getGenre());
        if (book.getCoverImage() != null) {
            imageViewCover.setImageBitmap(BitmapFactory.decodeByteArray(book.getCoverImage(), 0, book.getCoverImage().length));
        } else {
            imageViewCover.setImageResource(R.drawable.baseline_book_24);
        }
    }

    private void saveBookChanges() {
        String title = editTextTitle.getText().toString().trim();
        String publisher = editTextPublisher.getText().toString().trim();
        String genre = editTextGenre.getText().toString().trim();

        if (!title.isEmpty() && !publisher.isEmpty() && !genre.isEmpty()) {
            currentBook.setTitle(title);
            currentBook.setPublisher(publisher);
            currentBook.setGenre(genre);

            if (imageUri != null) {
                byte[] imageData = getImageData();
                currentBook.setCoverImage(imageData);
            }

            userManager.updateBook(currentBook);

            Toast.makeText(this, "Livro atualizado com sucesso!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(EditBookActivity.this, HomePage.class);
            intent.putExtra("USER_EMAIL", currentUser.getEmail());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewCover.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getImageData() {
        byte[] imageData = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            imageData = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageData;
    }
}
