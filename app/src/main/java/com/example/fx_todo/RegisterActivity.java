package com.example.fx_todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity"; //로그 확인할 때 사용할 태그
    EditText  mEmailText, mPasswordText, mPasswordcheckText, mName;
    Button minsertBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //액션바 숨김

        firebaseAuth =  FirebaseAuth.getInstance(); //파이어베이스 접근 설정

        mEmailText = findViewById(R.id.email);
        mPasswordText = findViewById(R.id.password);
        mPasswordcheckText = findViewById(R.id.passwordcheck);
        minsertBtn = findViewById(R.id.insertBtn);
        mName = findViewById(R.id.name);


        minsertBtn.setOnClickListener(new View.OnClickListener() //가입 버튼
        {
            @Override
            public void onClick(View v)
            {
                final String email = mEmailText.getText().toString().trim();
                String pwd = mPasswordText.getText().toString().trim();
                String pwdcheck = mPasswordcheckText.getText().toString().trim();
                //가입할 때 입력한 정보 문자열로 가져오기

                if (pwd.equals(pwdcheck))
                {
                    Toast.makeText(RegisterActivity.this, "가입 중입니다.", Toast.LENGTH_SHORT).show();
                    // 입력한 비밀번호와 체크하기 위해 입력한 비밀번호가 같을 때

                    //파이어베이스에 신규계정 등록하기
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd) //이메일과 비밀번호를 받아 데이터베이스에 저장
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) //성공 시
                                    {
                                        FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 사용자 받아오기
                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(mName.getText().toString().trim()).build();
                                        user.updateProfile(profileChangeRequest);

                                        String email = user.getEmail();
                                        String uid = user.getUid();
                                        //String name = mName.getText().toString().trim();
                                        String name = user.getDisplayName();
                                        //현재 사용자의 정보 저장

                                        HashMap<Object, String> hashMap = new HashMap<>();
                                        hashMap.put("uid", uid);
                                        hashMap.put("email", email);
                                        hashMap.put("name", name);
                                        //데이터베이스에 저장할 키와 값 설정


                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Users");
                                        reference.child(uid).setValue(hashMap);
                                        //데이터베이스에 데이터 저장

                                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                        firestore.collection("Users").document().set(hashMap);
                                        //데이터스토어에 데이터 저장


                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                        //회원가입 성공 시 로그인 화면으로 돌아가기
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                        //존재하는 정보일 경우 회원가입 실패
                                    }
                                }
                            });
                } else
                {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } //입력한 비밀번호와 체크를 위한 비밀번호 입력이 다를 경우 회원가입 실패
            }
        });
    }
}