package com.example.project9_4;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static int LINE = 1, CIRCLE = 2, RECT = 3;
    static int curShape = LINE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyGraphicView(this));
        setTitle("간단 그림판");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, LINE, 0, "선 추가");
        menu.add(0, CIRCLE, 0, "원 추가");
        menu.add(0, RECT, 0, "사각형 추가");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        curShape = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    // 추상 Shape 클래스와 자식 클래스들 정의
    abstract class Shape {
        int startX, startY, stopX, stopY;

        public Shape(int startX, int startY, int stopX, int stopY) {
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
        }

        public abstract void draw(Canvas canvas, Paint paint);
    }

    class Line extends Shape {
        public Line(int startX, int startY, int stopX, int stopY) {
            super(startX, startY, stopX, stopY);
        }
        @Override
        public void draw(Canvas canvas, Paint paint) {
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    class Circle extends Shape {
        public Circle(int startX, int startY, int stopX, int stopY) {
            super(startX, startY, stopX, stopY);
        }
        @Override
        public void draw(Canvas canvas, Paint paint) {
            int radius = (int) Math.sqrt(Math.pow(stopX - startX, 2) + Math.pow(stopY - startY, 2));
            canvas.drawCircle(startX, startY, radius, paint);
        }
    }

    class Rect extends Shape {
        public Rect(int startX, int startY, int stopX, int stopY) {
            super(startX, startY, stopX, stopY);
        }
        @Override
        public void draw(Canvas canvas, Paint paint) {
            int left = Math.min(startX, stopX);
            int top = Math.min(startY, stopY);
            int right = Math.max(startX, stopX);
            int bottom = Math.max(startY, stopY);
            canvas.drawRect(left, top, right, bottom, paint);
        }
    }

    private class MyGraphicView extends View {
        int startX = -1, startY = -1;
        int stopX = -1, stopY = -1;
        ArrayList<Shape> shapes = new ArrayList<>();

        public MyGraphicView(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    stopX = (int) event.getX();
                    stopY = (int) event.getY();
                    this.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    stopX = (int) event.getX();
                    stopY = (int) event.getY();
                    switch (curShape) {
                        case LINE:
                            shapes.add(new Line(startX, startY, stopX, stopY));
                            break;
                        case CIRCLE:
                            shapes.add(new Circle(startX, startY, stopX, stopY));
                            break;
                        case RECT:
                            shapes.add(new Rect(startX, startY, stopX, stopY));
                            break;
                    }
                    this.invalidate();
                    break;
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);

            // 저장된 모든 도형을 그린다.
            for (Shape s : shapes) {
                s.draw(canvas, paint);
            }
        }
    }
}
