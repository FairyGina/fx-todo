package com.example.fx_todo;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days; //배열 선언
    private final OnItemListener onItemListener; //아이템을 클릭했을 때 발생하는 이벤트...?

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener)
    {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        //날짜를 반복할 수 있도록 함
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) //월간 달력 레이아웃
            layoutParams.height = 180;
        else //주간 달력 레이아웃
            layoutParams.height = 180;

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        final LocalDate date = days.get(position); //날짜 적용
        if(date == null)
        {
            holder.dayOfMonth.setText(""); //누른 위치에 날짜가 없으면 아무것도 표시 안함
            holder.dayImage.setImageResource(0); //날짜가 없으면 이미지 표시 안함
        }
        else
        {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if(date.equals(CalendarUtils.selectedDate))
            {
                holder.dayOfMonth.setTextColor(Color.BLACK); //누른 날짜 글씨색 검정으로 변경
                holder.dayOfMonth.setTextSize(12); //누른 날짜 글씨 크기 12로 변경
                holder.dayOfMonth.setPaintFlags(holder.dayOfMonth.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //누른 날짜 글씨에 밑줄 추가
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    } //리사이클러뷰의 개수 결정

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    } //날짜를 눌렀을 때 위치를 넘김
}
