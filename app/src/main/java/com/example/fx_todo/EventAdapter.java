package com.example.fx_todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    } //입력받은 내용과 날짜 연결

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position); //누른 곳의 위치 받아옴

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);
        //리스트뷰의 뷰 하나하나를 재사용하기 위함

        TextView eventCell = convertView.findViewById(R.id.event_cell);

        String eventTitle = event.getName();
        eventCell.setText(eventTitle);
        return convertView;
    }
}
