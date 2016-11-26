package com.badlogic.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.Pool.PoolObjectFactory;

public class MultiTouchHandler implements TouchHandler {
	
	// максимальное количество точек обработки (пальцев на экране)
	public static int MAX_POINTERS = 20;
	
    boolean[] isTouched = new boolean[MAX_POINTERS];
    Point[] touch = new Point[MAX_POINTERS];
    Point[] startTouch = new Point[MAX_POINTERS];
    Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();

    public MultiTouchHandler(View view) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);
        
        for(int i=0; i<MAX_POINTERS; i++) {
        	touch[i] = new Point(0, 0);
        	startTouch[i] = new Point(0, 0);
        }
    }
    
    @Override
    public void reset() {
    	touchEvents.clear();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            int pointerId = event.getPointerId(pointerIndex);
            TouchEvent touchEvent;

            switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touchEvent = touchEventPool.newObject();
                touchEvent.type = TouchEvent.TOUCH_DOWN;
                touchEvent.pointer = pointerId;
                touchEvent.x = startTouch[pointerId].x = touch[pointerId].x = (int) (event.getX(pointerIndex));
                touchEvent.y = startTouch[pointerId].y = touch[pointerId].y = (int) (event.getY(pointerIndex));
                isTouched[pointerId] = true;
                touchEventsBuffer.add(touchEvent);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touchEvent = touchEventPool.newObject();
                touchEvent.type = TouchEvent.TOUCH_UP;
                touchEvent.pointer = pointerId;
                touchEvent.x = touch[pointerId].x = (int) (event.getX(pointerIndex));
                touchEvent.y = touch[pointerId].y = (int) (event.getY(pointerIndex));
                isTouched[pointerId] = false;
                touchEventsBuffer.add(touchEvent);
                break;

            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);

                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touch[pointerId].x = (int) (event.getX(pointerIndex));
                    touchEvent.y = touch[pointerId].y = (int) (event.getY(pointerIndex));
                    touchEventsBuffer.add(touchEvent);
                }
                break;
            }

            return true;
        }
    }

    @Override
    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= 20)
                return false;
            else
                return isTouched[pointer];
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= MAX_POINTERS)
                return 0;
            else
                return touch[pointer].x;
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized (this) {
            if (pointer < 0 || pointer >= MAX_POINTERS)
                return 0;
            else
                return touch[pointer].y;
        }
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++)
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

	@Override
	public Point getStartPoint(int pointer) {
		synchronized (this) {
            if (pointer < 0 || pointer >= MAX_POINTERS)
                return startTouch[0];
            else
                return startTouch[pointer];
        }
	}

	@Override
	public Point getCurrentPoint(int pointer) {
		synchronized (this) {
            if (pointer < 0 || pointer >= MAX_POINTERS)
                return touch[0];
            else
                return touch[pointer];
        }
	}
}