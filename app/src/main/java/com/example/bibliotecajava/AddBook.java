package com.example.bibliotecajava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotecajava.model.Book;
import com.example.bibliotecajava.model.User;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddBook extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editTextTitle;
    private EditText editTextPublisher;
    private EditText editTextGenre;
    private ImageView imageViewCover;
    private Uri imageUri;
    private byte[] coverImageData;
    private UserManager userManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextPublisher = findViewById(R.id.editTextPublisher);
        editTextGenre = findViewById(R.id.editTextGenre);
        imageViewCover = findViewById(R.id.imageViewCover);
        Button btnSaveBook = findViewById(R.id.btnSaveBook);
        Button buttonBack = findViewById(R.id.buttonBack);

        userManager = UserManager.getInstance();
        String email = getIntent().getStringExtra("USER_EMAIL");
        currentUser = userManager.getUserByEmail(email);

        if (currentUser == null) {
            Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSaveBook.setOnClickListener(v -> saveBook());
        buttonBack.setOnClickListener(v -> finish());

        imageViewCover.setOnClickListener(this::selectImage);
    }

    public void selectImage(View view) {
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

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                coverImageData = byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveBook() {
        String title = editTextTitle.getText().toString().trim();
        String publisher = editTextPublisher.getText().toString().trim();
        String genre = editTextGenre.getText().toString().trim();

        if (title.isEmpty() || publisher.isEmpty() || genre.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Book newBook = new Book(title, publisher, genre, coverImageData);

        currentUser.addBook(newBook);

        userManager.addBook(newBook);

        Toast.makeText(this, "Livro salvo com sucesso!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddBook.this, HomePage.class);
        intent.putExtra("USER_EMAIL", currentUser.getEmail());
        startActivity(intent);
        finish();
    }
}
