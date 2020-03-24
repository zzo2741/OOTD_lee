package com.example.ootd.ootd_1.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.signature.ObjectKey;
import com.example.ootd.ootd_1.Adapter.FragmentPagerAdapter;
import com.example.ootd.ootd_1.Adapter.MyPagerAdapter;
import com.example.ootd.ootd_1.Fragment.BookmarkFragment;
import com.example.ootd.ootd_1.Fragment.GridFragment;
import com.example.ootd.ootd_1.Fragment.ScrollFragment;
import com.example.ootd.ootd_1.R;
import com.example.ootd.ootd_1.model.SignUpModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button modifyBtn, addPhotoBtn;
    private TextView email, name;
    private DatabaseReference mDatabase;
    private ImageView iv_pi;
    private String uid;
    private SignUpModel user;
    //파이어베이스
    private StorageReference ref;
    private FirebaseStorage storage;
    public static HashSet<String> strings;
    //뷰페이저 어댑터 네비
    private BottomNavigationView bottomNavigationView;
    private MenuItem menuItem;
    private FrameLayout fragmentView;
    public SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private GridFragment gridFragment = new GridFragment();
    private ScrollFragment scrollFragment = new ScrollFragment();
    private BookmarkFragment bookmarkFragment = new BookmarkFragment();
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //네비
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        //버튼 이미지뷰 텍스트뷰 등
        modifyBtn = findViewById(R.id.modifyBtn);
        addPhotoBtn = findViewById(R.id.addPhotoBtn);
        iv_pi = findViewById(R.id.iv_pi);
        email = findViewById(R.id.tvEmail);
        name = findViewById(R.id.tvName);
        sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        //파이어베이스
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
                .into(iv_pi); //ImageView에 직접 로드;

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_view, gridFragment).commitAllowingStateLoss();
        //BottomNavigationView에서 아이템이 선택됐을때 ViewPager가 item이 가리키는 Page로 이동하게 해주는 Event Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                transaction = fragmentManager.beginTransaction();
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {
                    case R.id.menu_grid:
                        transaction.replace(R.id.fragment_view, gridFragment).commitAllowingStateLoss();
                        break;

                    case R.id.menu_scroll:
                        transaction.replace(R.id.fragment_view, scrollFragment).commitAllowingStateLoss();
                        break;

                    case R.id.menu_bookmark:
                        transaction.replace(R.id.fragment_view, bookmarkFragment).commitAllowingStateLoss();
                        break;
                }
                return false;
            }
        });

        //파이어베이스 데이터베이스 읽어올때
        mDatabase.addValueEventListener(new ValueEventListener() {
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

        //회원정보수정
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, ProfileActivity.class);
                startActivityForResult(in, 2);
            }
        });

        //사진추가
        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, AddImageActivity.class);
                startActivityForResult(in, 1);
            }
        });
    }

    //시작 시 호출
    public void onStart() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        HashSet<String> set = new HashSet<String>();
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

            }
        }
        gridFragment.updateView();
        scrollFragment.updateView();
    }
}

