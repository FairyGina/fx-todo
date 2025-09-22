package com.example.fx_todo;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;

public class EventEditActivity extends AppCompatActivity {
    private EditText eventName;
    private TextView eventDate;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private String uid = null;
    private String category;

    final int max = 10;
    final int min =  1;
    final int step = 1;
    final String t_title = "Time : ";
    final String i_title = "Importance : ";
    TextView t;
    SeekBar time;
    TextView i;
    SeekBar importance;
    String t_str;
    String i_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        //액션바 숨김

        firebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 접근 설정

        eventName = findViewById(R.id.event_name);
        eventDate = findViewById(R.id.event_date);
        eventDate.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        // 일정을 추가하는 페이지에서 날짜를 알 수 있도록 함

        t = (TextView) findViewById(R.id.t);
        time  = (SeekBar) findViewById(R.id.time);
        i = (TextView) findViewById(R.id.i);
        importance = (SeekBar) findViewById(R.id.importance);


        t_seekbar();
        i_seekbar();



    }



    public void saveEventAction(View view) {
        String event_name = eventName.getText().toString();
        String event_date = eventDate.getText().toString();
//        String skey = databaseReference.push().getKey();
//        if (skey != null)
//        {
//            databaseReference.child(skey).child("todo").setValue(event_name);
//        }
        Event newEvent = new Event(event_name, CalendarUtils.selectedDate);
        Event.eventsList.add(newEvent);

//        Intent intent = new Intent();
//        intent.putExtra("event", event_name);
//        setResult(RESULT_OK, intent);

        //현재 사용자의 정보 저장

        final FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 사용자 받아오기
        if (user != null) {
            this.uid = user.getUid();
        }
        uid = user.getUid();

        HashMap<String, Object> real_hashMap = new HashMap<>();
        real_hashMap.put("todo", event_name);
        real_hashMap.put("date", event_date);
        real_hashMap.put("time", t_str);
        real_hashMap.put("importance", i_str);


        //데이터베이스에 저장할 키와 값 설정

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("todo").child(uid).push().setValue(real_hashMap);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("todo", event_name);
        hashMap.put("date", event_date);
        //데이터베이스에 저장할 키와 값 설정

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("todo").document().set(hashMap);
        //데이터베이스에 데이터 저장


        finish();
    } // 일정을 문자열로 받아와서 리스트에 저장

    public void t_seekbar()
    {
        setSeekBarMax(time, max);
        time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                t_setSeekBarChange(progress, t);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        setSeekBarAnimation(time);
    }

    public void i_seekbar()
    {
        setSeekBarMax(importance, max);
        importance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                i_setSeekBarChange(progress, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        setSeekBarAnimation(importance);
    }

    private void setSeekBarMax(SeekBar sb, int max_value) {
        sb.setMax((int)((max_value-min) / step));
    }

    private void t_setSeekBarChange(int progress, TextView tv) {
        int value = min + (progress * step);
        DecimalFormat format = new DecimalFormat(".#");
        t_str = format.format(value);
        tv.setText(t_title +" ( "+t_str+" )");
    }
    private void i_setSeekBarChange(int progress, TextView tv) {
        int value = min + (progress * step);
        DecimalFormat format = new DecimalFormat(".#");
        i_str = format.format(value);
        tv.setText(i_title +" ( "+i_str+" )");
    }

    // 최초 중간 위치 설정
    private void setSeekBarAnimation(SeekBar sb) {

        int progress_half = (int)(((max / min) / 2));

        ObjectAnimator animation = ObjectAnimator.ofInt(sb, "progress", progress_half);
        animation.setDuration(100); // 0.5 second
        animation.setInterpolator(new /*DecelerateInterpolator()*/LinearInterpolator());
        animation.start();
    }
}