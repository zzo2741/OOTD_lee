package com.example.ootd.ootd_1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.signature.ObjectKey;
import com.example.ootd.ootd_1.Adapter.FragmentPagerAdapter;
import com.example.ootd.ootd_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import model.SignUpModel;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button modifyBtn, addPhotoBtn;
    private TextView email, name;
    private DatabaseReference mDatabase;
    private ImageView imageview_profileimage;
    private String uid;
    private SignUpModel user;

    //테스트
    private StorageReference ref;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabLayout tab_layout = findViewById(R.id.tab_layout);
        modifyBtn = findViewById(R.id.modifyBtn);
        addPhotoBtn = findViewById(R.id.addPhotoBtn);
        imageview_profileimage = findViewById(R.id.iv_pi);
        email = findViewById(R.id.tvEmail);
        name = findViewById(R.id.tvName);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        //GildeApp 사용
        String filename = uid + ".jpg";
        storage = FirebaseStorage.getInstance();
        ref = storage.getReference().child("profileimage/" + filename);

        GlideApp.with(this)
                .load(ref) //Storege 참조
                .signature(new ObjectKey(UUID.randomUUID().toString()))
                .into(imageview_profileimage); //ImageView에 직접 로드;

        final ViewPager viewPager = findViewById(R.id.viewpager);
        final FragmentPagerAdapter myPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        tab_layout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));

        mDatabase.addValueEventListener(new ValueEventListener() { //데이터베이스 읽어올때
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(SignUpModel.class);
                    name.setText(user.user_name);
                    email.setText(user.user_email);
                } else {
                    Intent in = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(in);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(in);
            }
        });
        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, AddImageActivity.class);
                startActivity(in);
            }
        });
    }

    public void onStart() { // 시작 시 호출
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser(); //현재 유저의 정보 가져옴
        if (currentUser == null) { //로그인된 유저 없음
            Intent in1 = new Intent(MainActivity.this, LoginActivity.class); //로그인 액티비티로 이동
            startActivity(in1);
            finish();
        } else {
        }
    }
}
