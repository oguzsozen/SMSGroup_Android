package com.example.smsgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SinginActivity extends AppCompatActivity {

    Button singinBtnLogin, singinBtnSingin;
    TextView singinTxtMail, singinTxtPassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);

        singinBtnLogin = (Button) findViewById(R.id.singinBtnLogin);
        singinBtnSingin = (Button) findViewById(R.id.singinBtnSingin);
        singinTxtMail = (TextView) findViewById(R.id.singinTxtMail);
        singinTxtPassword = (TextView) findViewById(R.id.singinTxtPassword);

        mAuth = FirebaseAuth.getInstance();

        singinBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SinginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        singinBtnSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = singinTxtMail.getText().toString();
                String password = singinTxtPassword.getText().toString();

                if (mail == null || password == null) {
                    Toast.makeText(SinginActivity.this,"Lütfen tüm alanları doldurunuz...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6){
                    Toast.makeText(SinginActivity.this,"Parola 6 haneden uzun olmalı...", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SinginActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(SinginActivity.this,"Kayıt işlemi başarısız oldu.", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}