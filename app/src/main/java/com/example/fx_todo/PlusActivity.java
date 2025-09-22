package com.example.fx_todo;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class PlusActivity extends AppCompatActivity {

    EditText editText;
    ListView listView;
    TextView nameView, emailView;
    Button button;
    ArrayList<String> array = new ArrayList<>();
    ArrayList<Category> categorylist = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ExpandableListAdapt ad;


    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    private String uid;
    FirebaseUser user;


    //옵션 그래프 화면으로 이동하는 옵션 이미지 버튼(MoveOptionButton) 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 접근 설정
        user = firebaseAuth.getCurrentUser(); //현재 사용자 받아오기


        editText = (EditText) findViewById(R.id.categoryName);
        listView = (ListView) findViewById(R.id.categoryListView);
        ImageButton MoveOutButton = (ImageButton) findViewById(R.id.outView);     //MoveOutButton 버튼 생성 out = imageButton 연동

        nameView = (TextView) findViewById(R.id.name);
        emailView = (TextView) findViewById(R.id.email);

        if (user != null) {
            //Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            nameView.setText(name);
            emailView.setText(email);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();
        }

       show_category(uid);


        //MoveButton 누를시 Main 화면으로 전환시키는 수식
        MoveOutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        ImageButton MoveOptionButton = (ImageButton) findViewById(R.id.optionView);     //MoveOptionButton 버튼을 option = imageButton 연동

        //MoveButton 누를시 option 화면으로 전환시키는 수식
        MoveOptionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OptionActivity.class);
            startActivity(intent);

        });


    }

    public void save(View view)
    {
        String str = editText.getText().toString();
        if (str.length() != 0) {
//            array.add(str + "");
//            adapter.notifyDataSetChanged();

            editText.setText("");
        }


        if (user != null) {
            this.uid = user.getUid();
        }
        uid = user.getUid();

        HashMap<String, Object> real_hashMap = new HashMap<>();
        real_hashMap.put("category", str);
        //데이터베이스에 저장할 키와 값 설정

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("category").child(uid).push().setValue(real_hashMap);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("category", str);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("category").document().set(hashMap);
        //데이터베이스에 데이터 저장

    }

    public void show_category(String uid){
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        listView.setAdapter(adapter);

//        category_ref = databaseReference.child("category").child(uid);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("category").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Category c = dataSnapshot.getValue(Category.class);
                String str = dataSnapshot.child("category").getValue(String.class); //uid 밑의 랜덤키 밑의 category 가져옴
                array.add(str);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}