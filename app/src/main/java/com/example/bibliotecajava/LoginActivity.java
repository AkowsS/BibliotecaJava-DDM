package com.example.bibliotecajava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private UserManager userManager;
    private EditText userEmail;
    private EditText userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userManager = UserManager.getInstance();

        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if (userManager.authenticateUser(email, password)) {
                    Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomePage.class);
                    String emailIntent = userEmail.getText().toString().trim();
                    intent.putExtra("USER_EMAIL", emailIntent);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Email ou senha incorretos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
