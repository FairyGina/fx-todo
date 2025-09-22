package com.example.fx_todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

    }

    public void logout(View v)
    {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
        finish();
    }

    public void close(View v)
    {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) //바깥레이어 클릭시 안닫히게
    {
        if (event.getAction()==MotionEvent.ACTION_OUTSIDE)
        {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() //안드로이드 백버튼 막기
    {
        return;
    }
}