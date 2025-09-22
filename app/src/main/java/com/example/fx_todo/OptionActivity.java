package com.example.fx_todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class OptionActivity extends AppCompatActivity {
    private LogoutActivity customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        ImageButton MoveOutButton=(ImageButton)findViewById(R.id.outView);     //MoveOutButton 버튼 생성 out = imageButton 연동

        //MoveButton 누를시 Plus 화면으로 전환시키는 수식
        MoveOutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),PlusActivity.class);
            startActivity(intent);
        });

        Button MoveAccountButton = (Button) findViewById(R.id.accountView);     //MoveAccountButton 버튼 생성 account = Button 연동

        MoveAccountButton.setOnClickListener(v -> {          //MoveButton 누를시 Account 화면으로 전환시키는 수식
            Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
            startActivity(intent);
        });

        Button MoveLogoutButton=(Button)findViewById(R.id.logoutView);     //MoveLogoutButton 버튼 생성 logout = Button 연동

        MoveLogoutButton.setOnClickListener(v -> {            //MoveButton 누를시 Logout 화면으로 전환시키는 수식
            Intent intent = new Intent(getApplicationContext(),LogoutActivity.class);
            startActivity(intent);
        });

        Button MoveWithdrawalButton=(Button)findViewById(R.id.withdrawalView);     //MoveLogoutButton 버튼 생성 logout = Button 연동


        MoveWithdrawalButton.setOnClickListener(v -> {            //MoveButton 누를시 Logout 화면으로 전환시키는 수식
            Intent intent = new Intent(getApplicationContext(),Withdrawal.class);
            startActivity(intent);
        });
    }
}