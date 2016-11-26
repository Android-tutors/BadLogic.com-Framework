package com.badlogic.androidgames.framework.gl;

import android.util.Log;

public class FPS {
	
	float maxDeltaTime;
	float deltaTime = 0;
	int updates = 0;
	
	public FPS(float maxDeltaTime) {
		this.maxDeltaTime = maxDeltaTime;
	}
	
	public void update(float deltaTime) {
		updates++;
		this.deltaTime += deltaTime;
		if(this.deltaTime > maxDeltaTime) {
			float fps = updates / this.deltaTime;
			this.deltaTime = 0;
			updates = 0;
			Log.d("FPS", Float.toString(fps));
		}
	}
}
