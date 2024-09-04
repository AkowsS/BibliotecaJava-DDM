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

import com.example.bibliotecajava.model.User;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText userName;
    private EditText userPassword;
    private ImageView userImage;
    private Uri imageUri;
    private UserManager userManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        userManager = UserManager.getInstance();

        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        Intent intent = getIntent();
        String email = intent.getStringExtra("USER_EMAIL");
        currentUser = userManager.getUserByEmail(email);

        if (currentUser != null) {
            loadUserData(currentUser);
        } else {
            Toast.makeText(this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });
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
                userImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUserData(User user) {
        userName.setText(user.getName());
        userPassword.setText(user.getPassword());

        if (user.getProfileImageUri() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getProfileImageUri(), 0, user.getProfileImageUri().length);
            userImage.setImageBitmap(bitmap);
        } else {
            userImage.setImageResource(R.drawable.baseline_person_24);
        }
    }

    private void updateUserProfile() {
        String name = userName.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Algum campo está vazio.", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageData = getImageData();
        currentUser.setName(name);
        currentUser.setPassword(password);
        currentUser.setProfileImageUri(imageData);

        Toast.makeText(getApplicationContext(), "Perfil atualizado com sucesso", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
        intent.putExtra("USER_EMAIL", currentUser.getEmail());
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
    }

    private byte[] getImageData() {
        byte[] imageData = null;
        try {
            Bitmap bitmap;
            if (imageUri != null) {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.baseline_person_24);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            imageData = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageData;
    }
}
