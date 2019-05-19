package com.example.ootd.ootd_1.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ootd.ootd_1.Fragment.BottomSheetDialog;
import com.example.ootd.ootd_1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.SignUpModel;

public class ModifyInfoActivity extends AppCompatActivity {

    private ImageButton backBtn, homeBtn, imBtn;
    private EditText name, pw, day;
    private Spinner spSex;
    private Button comBtn;
    SignUpModel user;

    private DatabaseReference mUser;
    private FirebaseUser cUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        user = new SignUpModel();

        name = findViewById(R.id.etName);
        pw = findViewById(R.id.etPW_change);
        day = findViewById(R.id.etDay);
        spSex = findViewById(R.id.spSex);

        backBtn = findViewById(R.id.backBtn);
        homeBtn = findViewById(R.id.homeBtn);
        comBtn = findViewById(R.id.comBtn);
        imBtn = findViewById(R.id.imBtn);

        mUser = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        cUser = FirebaseAuth.getInstance().getCurrentUser();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ModifyInfoActivity.this, ProfileActivity.class);
                startActivity(in);
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(ModifyInfoActivity.this, MainActivity.class);
                startActivity(in2);
            }
        });
        comBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw.getText().length() * name.getText().length() * day.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    cUser.updatePassword(pw.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mUser.child("user_password").setValue(pw.getText().toString());
                        }
                    });
                    mUser.child("user_name").setValue(name.getText().toString());
                    mUser.child("user_sex").setValue(spSex.getSelectedItem().toString());
                    mUser.child("user_day").setValue(day.getText().toString());
                    Toast.makeText(getApplicationContext(), "수정 성공", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        imBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();
                bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
            }
        });
    }

}
