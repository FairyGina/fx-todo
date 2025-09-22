package com.example.fx_todo;

import static com.example.fx_todo.CalendarUtils.daysInWeekArray;
import static com.example.fx_todo.CalendarUtils.monthYearWeekFromDate;
import static com.example.fx_todo.CalendarUtils.selectedDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private ExpandableListView mListView;
    private ExpandableListAdapt ad;
    private ArrayList<Category> mGroupList = null;
    private ArrayList<ArrayList<Event>> mChildList = null;
    private ArrayList<Event> mChildListContent = null;
    String str;
    String category;

    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    private String uid;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance(); //파이어베이스 접근 설정
        user = firebaseAuth.getCurrentUser(); //현재 사용자 받아오기
        uid = user.getUid();

        ImageButton MoveP_graphButton=(ImageButton)findViewById(R.id.p_graphView);     //MoveP_graphButton 버튼 생성 p_graph = imageButton 연동

        //MoveButton 누를시 PriorityGraph 화면으로 전환시키는 수식
        MoveP_graphButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),PriorityGraphActivity.class);
            startActivity(intent);
        });

        ImageButton MovePlusButton = (ImageButton) findViewById(R.id.plusView);     //MovePlusButton 버튼 생성 plus = imageButton 연동

        //MoveButton 누를시 Plus 화면으로 전환시키는 수식
        MovePlusButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),PlusActivity.class);
            startActivity(intent);
        });



        //달력

        calendarRecyclerView = findViewById(R.id.recyclerview);
        monthYearText = findViewById(R.id.year_month_week);
        ImageView dayImage = findViewById(R.id.day_image);
        eventListView = (ListView) findViewById(R.id.eventListView);
        //id값과 객체 연결
        setWeekView();
        getWindow().setWindowAnimations(0); //화면 전환 애니메이션 없음
    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearWeekFromDate(CalendarUtils.selectedDate)); //설정한 주간 달력 텍스트 포맷으로 텍스트 설정
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate); //배열에 날짜 넣기

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        //달력 화면 설정
        calendarRecyclerView.setAdapter(calendarAdapter); //달력 어댑터 연결
        setEventAdpater(); //일정 추가 어댑터 연결
    }

    public void month_change(View view)
    {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    } //월간 달력으로 바꾸기

    public void previous_week_button(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    } //현재 주에서 -1한 주를 출력

    public void next_week_button(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    } //현재 주에서 +1한 주를 출력

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    } //주간 달력 화면에서 누른 위치 넘김

    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater() {

        mChildListContent = Event.eventsForDate(CalendarUtils.selectedDate);
        //EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), mChildListContent);

        mGroupList = new ArrayList<Category>();
        mChildList = new ArrayList<ArrayList<Event>>();
        mChildListContent = new ArrayList<Event>();
        mListView = (ExpandableListView) findViewById(R.id.eventListView);
        mListView.setGroupIndicator(null);


//        mGroupList.add(new Category("1"));
////        mGroupList.add(new Category("2"));
////        mGroupList.add(new Category("3"));
////
////        mChildListContent.add(new Event("1", selectedDate));
////        mChildListContent.add(new Event("2", selectedDate));
////        mChildListContent.add(new Event("3", selectedDate));
////
////        mChildList.add(mChildListContent);
////        mChildList.add(mChildListContent);
////        mChildList.add(mChildListContent);

        show_category(uid);
        show_todo(uid);

//        setLayout();

        for (int i = 0; i<ad.getGroupCount(); i++)
        {
            mListView.expandGroup(i);
        }


        // 그룹 클릭 했을 경우 이벤트
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Toast.makeText(getApplicationContext(), "g click = " + groupPosition, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), ad.getGroup(groupPosition).getCategory(), Toast.LENGTH_SHORT).show();
                category = ad.getGroup(groupPosition).getCategory();
                return true;
            }

        });

        // 차일드 클릭 했을 경우 이벤트
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), "c click = " + childPosition, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // 그룹이 닫힐 경우 이벤트
        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), "g Collapse = " + groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        // 그룹이 열릴 경우 이벤트
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), "g Expand = " + groupPosition, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * Layout
     */

//    private void setLayout()
//    {
//
//        ad = new ExpandableListAdapt(this, mGroupList, mChildList);
//        mListView.setAdapter(ad);
//        mListView.setGroupIndicator(null);
//
//        show_category(uid);
////        show_todo(uid);
////        setListItems();
//
//
//    }

//    public void setListItems()
//    {
//        mGroupList.clear();
//        mChildList.clear();
//
//        mChildList.add(mChildListContent);
//
//
//        ad.notifyDataSetChanged();
//    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    } //일정 추가 화면으로 전환

    public void show_category(String uid){
        ad = new ExpandableListAdapt(this, mGroupList, mChildList);
        mListView.setAdapter(ad);

//        category_ref = databaseReference.child("category").child(uid);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("category").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Category c = dataSnapshot.getValue(Category.class);
                str = dataSnapshot.child("category").getValue(String.class); //uid 밑의 랜덤키 밑의 category 가져옴
                mGroupList.add(new Category(str));
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

    public void show_todo(String uid){
        ad = new ExpandableListAdapt(this, mGroupList, mChildList);
        mListView.setAdapter(ad);

//        category_ref = databaseReference.child("category").child(uid);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("todo").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //Event e = dataSnapshot.getValue(Event.class);
                String event = dataSnapshot.child("todo").getValue(String.class); //uid 밑의 랜덤키 밑의 category 가져옴
                mChildListContent.add(new Event(event, selectedDate));  //데이터는 잘 받아옴
                mChildList.add(mChildListContent);
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