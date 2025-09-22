package com.example.fx_todo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth  firebaseAuth;
    private String uid;
    FirebaseUser user;
    TextView nameView2, emailView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        ImageButton MoveOutButton=(ImageButton)findViewById(R.id.outView);     //MoveOutButton 버튼 생성 out = imageButton 연동

        firebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 접근 설정
        user = firebaseAuth.getCurrentUser(); //현재 사용자 받아오기



        nameView2 = (TextView) findViewById(R.id.name2);
        emailView2 = (TextView) findViewById(R.id.email2);

        if (user != null) {
            //Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            nameView2.setText(name);
            emailView2.setText(email);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();
        }

        //MoveButton 누를시 Plus 화면으로 전환시키는 수식
        MoveOutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),OptionActivity.class);
            startActivity(intent);
        });
    }
}