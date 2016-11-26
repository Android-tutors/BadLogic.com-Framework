package com.badlogic.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnKeyListener;

import com.badlogic.androidgames.framework.Input.KeyEvent;
import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.Pool.PoolObjectFactory;

public class KeyboardHandler implements OnKeyListener {
	
	// ====================================================
	//	Fields
	// ====================================================

    boolean[] pressedKeys = new boolean[128];
    Pool<KeyEvent> keyEventPool;
    List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>();    
    List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
    
    // список кнопок, которые будут игнорироваться при опросе клавиш
    private List<Integer> ignoredKeys = new ArrayList<Integer>();

    // ====================================================
    //	Construct methods
    // ====================================================
    
    public KeyboardHandler(View view) {
        PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
            @Override
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        };
        keyEventPool = new Pool<KeyEvent>(factory, 100);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
    
    // ====================================================
 	//	Parental methods
 	// ====================================================

    public void reset() {
    	keyEvents.clear();
    }
    
    @Override
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
            return false;

        synchronized (this) {
            KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = true;
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = false;
            }
            keyEventsBuffer.add(keyEvent);
        }
        
        return (checkIgnoredKey(keyCode));
    }
    
    // ====================================================
  	//	Public methods
  	// ====================================================

    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode > 127)
            return false;
        return pressedKeys[keyCode];
    }

    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();
            for (int i = 0; i < len; i++)
                keyEventPool.free(keyEvents.get(i));
            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
        }
    }
    
    /**
     * Метод добавляет код клавиши в список игнора 
     * @param keyCode - Код клавиши android.view.KeyEvent.KEY_CODE***
     */
    public void addIgnoredKey(int keyCode) {
    	int id = ignoredKeys.indexOf(keyCode);
    	if(id < 0)
    		ignoredKeys.add(keyCode);
    }

    /**
     * Метод удаляет код клавиши из списка игнора 
     * @param keyCode - Код клавиши android.view.KeyEvent.KEY_CODE***
     */
	public void removeIgnoredKey(int keyCode) {
		int id = ignoredKeys.indexOf(keyCode);
    	if(id >= 0)
    		ignoredKeys.remove(id);
	}

	/**
     * Метод проверяет наличие клавиши в списке игнора 
     * @param keyCode - Код клавиши android.view.KeyEvent.KEY_CODE***
     */
	public boolean checkIgnoredKey(int keyCode) {
		int id = ignoredKeys.indexOf(keyCode);
    	return (id >= 0);
	}
}