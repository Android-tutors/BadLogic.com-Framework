package com.badlogic.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pool.PoolObjectFactory;

public class SingleTouchHandler implements TouchHandler {
    boolean isTouched;
    Point touch = new Point(0, 0);
    Point startTouch = new Point(0, 0);
    Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    
    public SingleTouchHandler(View view) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }            
        };
        touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);
    }
    
    @Override
    public void reset() {
    	touchEvents.clear();
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized(this) {
            TouchEvent touchEvent = touchEventPool.newObject();
            touchEvent.x = touch.x = (int)(event.getX());
            touchEvent.y = touch.y = (int)(event.getY());
            
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchEvent.type = TouchEvent.TOUCH_DOWN;
                isTouched = true;
                startTouch.set(touch.x, touch.y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                isTouched = true;
                break;
            case MotionEvent.ACTION_CANCEL:                
            case MotionEvent.ACTION_UP:
                touchEvent.type = TouchEvent.TOUCH_UP;
                isTouched = false;
                break;
            }
            
            touchEventsBuffer.add(touchEvent);                        
            return true;
        }
    }

    @Override
    public boolean isTouchDown(int pointer) {
        synchronized(this) {
            if(pointer == 0)
                return isTouched;
            else
                return false;
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized(this) {
            return touch.x;
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized(this) {
            return touch.y;
        }
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized(this) {     
            int len = touchEvents.size();
            for( int i = 0; i < len; i++ )
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

	@Override
	public Point getStartPoint(int pointer) {
		return startTouch;
	}

	@Override
	public Point getCurrentPoint(int pointer) {
		return touch;
	}
}