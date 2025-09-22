package com.example.fx_todo;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.nio.file.Path;


public class Canvas extends View {

    public Canvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = Bitmap.createBitmap(800,800, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(30f);
        canvas.drawPoint(360, 640, paint);
        canvas.drawColor(Color.WHITE);


    }
}

