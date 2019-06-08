package com.example.ootd.ootd_1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.signature.ObjectKey;
import com.example.ootd.ootd_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import model.SignUpModel;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutBtn, checkBtn, modifyBtn;
    private TextView name, email, sex, day;
    private ImageView iv_image;
    private DatabaseReference mDatabase;
    private String uid;
    private SignUpModel user;
    private FirebaseAuth mAuth;
    private StorageReference gsReference;
    private FirebaseStorage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutBtn = findViewById(R.id.logoutBtn);
        checkBtn = findViewById(R.id.checkBtn);
        modifyBtn = findViewById(R.id.modifyBtn);
        iv_image = findViewById(R.id.iv_image);

        name = findViewById(R.id.tvName_input);
        email = findViewById(R.id.tvID_input);
        sex = findViewById(R.id.tvSex_input);
        day = findViewById(R.id.tvDay_input);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        //테스트
        String filename = uid + ".jpg";
        storage = FirebaseStorage.getInstance();
        gsReference = storage.getReference().child("profileimage/" + filename);

        GlideApp.with(this)
                .load(gsReference)
                .signature(new ObjectKey(UUID.randomUUID().toString()))
                .into(iv_image);

        mDatabase.addValueEventListener(new ValueEventListener() { //데이터베이스 읽어올때
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(SignUpModel.class);
                    name.setText(user.user_name);
                    email.setText(user.user_email);
                    day.setText(user.user_day);
                    sex.setText(user.user_sex);
                }
                else  {
                    Intent in = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(in);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent in = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(in2);
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in3 = new Intent(ProfileActivity.this, ModifyInfoActivity.class);
                startActivity(in3);
            }
        });

    }
}
