package com.example.fx_todo;

import static com.example.fx_todo.CalendarUtils.selectedDate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ExpandableListAdapt extends BaseExpandableListAdapter {
    private ArrayList<Event> events;
    private ArrayList<Category> groupList;
    private ArrayList<ArrayList<Event>> childList;
    private ArrayList<Event> mChildListContent;
    private LayoutInflater inflater;
    private ViewHolder viewHolder = null;





    public ExpandableListAdapt(@NonNull Context c, ArrayList<Category> groupList, ArrayList<ArrayList<Event>> childList){
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
    }

    // 그룹 포지션을 반환한다.
    @Override
    public Category getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    // 그룹 사이즈를 반환한다.
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    // 차일드뷰를 반환한다.
    @Override
    public Event getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    // 차일드뷰 사이즈를 반환한다.
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 그룹뷰 각각의 ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View v = convertView;
        Context context = parent.getContext();

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.parent_listview , parent, false);
            viewHolder.tv_groupName = (TextView) v.findViewById(R.id.parent);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }


        // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
        if(isExpanded){

        }
        else{

        }
        viewHolder.tv_groupName.setText(getGroup(groupPosition).getCategory());

        return v;
    }

    // 차일드뷰 각각의 ROW
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        View v = convertView;
        Context context = parent.getContext();

        if(v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.child_listview, null);
            viewHolder.tv_childName = (TextView) v.findViewById(R.id.chlid);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.tv_childName.setText(getChild(groupPosition, childPosition).getName());

        return v;
    }

    @Override
    public boolean hasStableIds() {	
        return true; 
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { 
        return true;
    }

    class ViewHolder {
        public TextView tv_groupName;
        public TextView tv_childName;
    }


    public void addItem(int groupPosition, Event item) {
        childList.get(groupPosition).add(item);
    }



}
