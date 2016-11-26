package com.badlogic.androidgames.framework.impl;

import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.View;

import com.badlogic.androidgames.framework.Input;

public class AndroidInput implements Input {
	
	// ====================================================
	//	Fields
	// ====================================================
	
	// менеджеры контроллеров ввода
    private AccelerometerHandler accelHandler;
    private KeyboardHandler keyHandler;
    private TouchHandler touchHandler;
    
    // ====================================================
    //	Construct methods
    // ====================================================

	public AndroidInput(Context context, View view) {
        accelHandler = new AccelerometerHandler(context);
        keyHandler = new KeyboardHandler(view);      
        // SDK версии ниже 5 не поддерживает мультитач
        if(VERSION.SDK_INT < 5) 
            touchHandler = new SingleTouchHandler(view);
        else
            touchHandler = new MultiTouchHandler(view);
    }
	
	// ====================================================
	//	Parental methods
	// ====================================================

	/**
	 * Сбрасывает состояния кнопок и тачей
	 */
	@Override
	public void reset() {
		keyHandler.reset();
		touchHandler.reset();
	}
	
    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return accelHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return accelHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return accelHandler.getAccelZ();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }
    
    @Override
    public void addIgnoredKey(int keyCode) {
    	keyHandler.addIgnoredKey(keyCode);
    }

	@Override
	public void removeIgnoredKey(int keyCode) {
		keyHandler.removeIgnoredKey(keyCode);
	}

	@Override
	public boolean checkIgnoredKey(int keyCode) {
		return keyHandler.checkIgnoredKey(keyCode);
	}

	@Override
	public Point getStartPoint(int pointer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point getCurrentPoint(int pointer) {
		// TODO Auto-generated method stub
		return null;
	}
}