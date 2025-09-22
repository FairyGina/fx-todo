package com.example.fx_todo;

import static com.example.fx_todo.CalendarUtils.selectedDate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

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

public class PriorityGraphActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> array = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    private String uid;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_graph);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 접근 설정
        user = firebaseAuth.getCurrentUser(); //현재 사용자 받아오기
        uid = user.getUid();

        listView = (ListView) findViewById(R.id.todoListView);

        ImageButton MoveOutButton = (ImageButton) findViewById(R.id.outView);     //MoveOutButton 버튼 생성 out = imageButton 연동

        //MoveButton 누를시 Plus 화면으로 전환시키는 수식
        MoveOutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        show_todo(uid);
    }

    public void show_todo(String uid) {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        listView.setAdapter(adapter);

//        category_ref = databaseReference.child("category").child(uid);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("todo").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                String str = dataSnapshot.child("todo").getValue(String.class); //uid 밑의 랜덤키 밑의 category 가져옴
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