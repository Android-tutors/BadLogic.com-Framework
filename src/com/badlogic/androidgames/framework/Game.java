package com.badlogic.androidgames.framework;

import android.graphics.Point;
import android.graphics.PointF;

public interface Game {
	public Input getInput();

	public FileIO getFileIO();

	public Graphics getGraphics();

	public Audio getAudio();

	public void setScreen(Screen screen);

	public Screen getCurrentScreen();

	public Screen getStartScreen();
	
	public Point getScreenSize();
	
	public PointF getScreenScale();
}