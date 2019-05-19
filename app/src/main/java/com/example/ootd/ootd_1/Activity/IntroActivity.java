package com.example.ootd.ootd_1.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ootd.ootd_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroActivity extends Activity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance(); //사용자 인증 객체
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout); //xml , java 소스 연결
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent); //다음화면으로 넘어감
                finish();
            }
        }, 2000); //2초 뒤에 Runner객체 실행하도록 함
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
