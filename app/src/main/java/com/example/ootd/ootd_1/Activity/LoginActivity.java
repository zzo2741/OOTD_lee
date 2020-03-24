package com.example.ootd.ootd_1.Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ootd.ootd_1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn, suBtn;
    private EditText etID, etPW;
    private FirebaseAuth mAuth;
    private String email, pw;

    public static Activity Login_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login_activity = LoginActivity.this;

        mAuth = FirebaseAuth.getInstance(); //사용자 인증 객체

        etID = findViewById(R.id.etID);
        etPW = findViewById(R.id.etPW);
        suBtn = findViewById(R.id.suBtn);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etID.getText().length() == 0 || etPW.getText().length() == 0) { //값을 입력했는지 검사
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    email = etID.getText().toString();
                    pw = etPW.getText().toString();
                    signIn(email, pw);
                }
            }
        });

        suBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(in);
            }
        });
    }

    public void signIn(String email, String password) {
        final ProgressDialog asyncDialog = new ProgressDialog(
                LoginActivity.this);

        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("wait..");
        asyncDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            asyncDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(in);
                            finish();
                        } else {
                            asyncDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void onStart() { // 시작 시 호출
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser(); //현재 유저의 정보 가져옴
        if (currentUser != null) { //로그인된 유저 없음
            Intent in1 = new Intent(LoginActivity.this, MainActivity.class); //로그인 액티비티로 이동
            startActivity(in1);
            finish();
        } else {
        }
    }
}

