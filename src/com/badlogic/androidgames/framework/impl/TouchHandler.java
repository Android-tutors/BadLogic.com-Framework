package com.badlogic.androidgames.framework.impl;

import java.util.List;

import android.graphics.Point;
import android.view.View.OnTouchListener;

import com.badlogic.androidgames.framework.Input.TouchEvent;

public interface TouchHandler extends OnTouchListener {
	public void reset();
	
    public boolean isTouchDown(int pointer);
    
    public int getTouchX(int pointer);
    
    public int getTouchY(int pointer);
    
    public Point getStartPoint(int pointer);
    
    public Point getCurrentPoint(int pointer);
    
    public List<TouchEvent> getTouchEvents();
}