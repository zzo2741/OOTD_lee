package com.example.ootd.ootd_1.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ootd.ootd_1.Fragment.BookmarkFragment;
import com.example.ootd.ootd_1.R;
import com.example.ootd.ootd_1.model.BookmarkImage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class PhotoActivity extends AppCompatActivity {

    private ImageButton backBtn, bookmarkBtn, bookmark_remove_Btn;
    private ImageView image;
    private EditText memo;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private String uid;
    private DatabaseReference mUser;
    private BookmarkImage bookmarkImage_model;
    private String imageUrl, imageMemo, imageUrl_back5;

    //테스트
    List<String> url_8 = new ArrayList<>();
    private Bitmap filePath;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        image = findViewById(R.id.ivPhoto);
        memo = findViewById(R.id.edMemo);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        uid = mAuth.getUid();
        imageUrl = StringReplace(getIntent().getStringExtra("image_url")); //이미지URL를 북마크에서 구별하는 고유이름으로 사용
        imageUrl_back5 = imageUrl.substring(imageUrl.length() - 8, imageUrl.length() - 3);

        mUser = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("BookmarkImage").child(imageUrl_back5);

        getIncomingIntent();

        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(PhotoActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });

        //추가 버튼 클릭시 데이터베이스에 생성
        bookmarkBtn = findViewById(R.id.bookmarkBtn);
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkImage_model = new BookmarkImage(getIntent().getStringExtra("image_url"), memo.getText().toString());
                mUser.setValue(bookmarkImage_model);

                BookmarkFragment bmFragment = new BookmarkFragment();
                Bundle bundle = new Bundle();
                bundle.putString("image_url", imageUrl);
                bundle.putString("image_memo", imageMemo);
                bmFragment.setArguments(bundle);
                finish();
            }
        });

        //삭제 버튼 클릭
        bookmark_remove_Btn = findViewById(R.id.bookmark_remove_Btn);
        bookmark_remove_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUser.removeValue();
                Intent in = new Intent(PhotoActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });
    }

    //이미지뷰에 이미지출력
    private void setImage(String imageUrl, String imageMemo) {
        memo.setText(imageMemo);

        GlideApp.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(image);
    }

    //이모티콘 제거
    public static String StringReplace(String str) {
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        str = str.replaceAll(match, "");
        return str;
    }

    //인텐트값 불러오기
    private void getIncomingIntent() {
        if (getIntent().hasExtra("image_url")) {
            imageUrl = getIntent().getStringExtra("image_url");
            System.out.println(imageUrl);
            imageMemo = getIntent().getStringExtra("image_memo");
            System.out.println(imageMemo);
            setImage(imageUrl, imageMemo);
        }
    }
}