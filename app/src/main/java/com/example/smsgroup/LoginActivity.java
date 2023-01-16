package com.example.smsgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button loginBtnLogin, loginBtnSingin;
    TextView loginTxtMail, loginTxtPassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtnLogin = (Button) findViewById(R.id.loginBtnLogin);
        loginBtnSingin = (Button) findViewById(R.id.loginBtnSingin);
        loginTxtMail = (TextView) findViewById(R.id.loginTxtMail);
        loginTxtPassword = (TextView) findViewById(R.id.loginTxtPassword);

        mAuth = FirebaseAuth.getInstance();

        loginBtnSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SinginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = loginTxtMail.getText().toString();
                String password = loginTxtPassword.getText().toString();

                if (mail == null || password == null) {
                    Toast.makeText(LoginActivity.this,"Lütfen tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Giriş başarısız oldu.", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}