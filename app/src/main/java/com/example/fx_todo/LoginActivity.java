package com.example.fx_todo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    Button mLoginBtn, mResigeterBtn;
    EditText mEmailText, mPasswordText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        // 액션바 숨김

        firebaseAuth =  FirebaseAuth.getInstance();
        //버튼 등록하기
        mResigeterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.loginBtn);
        mEmailText = findViewById(R.id.email_login);
        mPasswordText = findViewById(R.id.password_login);

        mResigeterBtn.setOnClickListener(v -> { //가입버튼
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class)); //회원가입 화면으로 전환
        });

        mLoginBtn.setOnClickListener(v -> { //로그인버튼
            String email = mEmailText.getText().toString().trim();
            String pwd = mPasswordText.getText().toString().trim();
            //이메일과 비밀번호 문자열로 저장
            firebaseAuth.signInWithEmailAndPassword(email,pwd) //파이어베이스에서 기본으로 제공하는 함수(이메일과 비밀번호를 데이터베이스에서 가져와 로그인)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if(task.isSuccessful()) //성공 시
                        {
                            Toast.makeText(LoginActivity.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        }else//실패 시
                        {
                            Toast.makeText(LoginActivity.this,"로그인 오류",Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
