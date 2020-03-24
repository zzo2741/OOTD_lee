package com.example.ootd.ootd_1.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ootd.ootd_1.R;
import com.example.ootd.ootd_1.model.SignUpModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private Button suBtn;
    private Spinner spSex;
    private EditText email, name, pw, day;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.etID);
        name = findViewById(R.id.etName);
        pw = findViewById(R.id.etPW);
        day = findViewById(R.id.etDay);

        spSex = findViewById(R.id.spSex);
        suBtn = findViewById(R.id.suBtn);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(in2);
            }
        });
        suBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().length() == 0 || pw.getText().length() == 0 || name.getText().length() == 0 || day.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    SignUpModel signUpModel = new SignUpModel(email.getText().toString(), pw.getText().toString(), name.getText().toString(), spSex.getSelectedItem().toString(), day.getText().toString(), "");
                    signUp(signUpModel);
                }
            }
        });
    }

    public void signUp(final SignUpModel signUpModel) {

        final ProgressDialog asyncDialog = new ProgressDialog(SignUpActivity.this);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("wait..");
        asyncDialog.show();

        mAuth.createUserWithEmailAndPassword(signUpModel.user_email, signUpModel.user_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mDatabase.child("users").child(task.getResult().getUser().getUid()).setValue(signUpModel);
                            Toast.makeText(getApplicationContext(), "가입 성공!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
