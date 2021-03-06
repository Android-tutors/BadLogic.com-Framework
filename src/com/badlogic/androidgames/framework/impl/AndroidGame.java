package com.badlogic.androidgames.framework.impl;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.FileIO;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;

public abstract class AndroidGame extends Activity implements Game {
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	// размер экрана
	Point screenSize = new Point();
	// отношение размера экрана к размеру буфера
	PointF screenScale;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// определяем размер экрана
		getWindowManager().getDefaultDisplay().getSize(screenSize);

		// ориентация экрана
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

		// задаем размер картинки рендера
		int frameBufferWidth = isLandscape ? 480 : 320;
		int frameBufferHeight = isLandscape ? 320 : 480;

		// коэффициент размера экарана на долю картинки
		screenScale = new PointF(1f * frameBufferWidth / screenSize.x, 1f * frameBufferHeight / screenSize.y);

		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);

		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new FileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView);
		screen = getStartScreen();
		setContentView(renderView);
	}

	@Override
	public void onResume() {
		super.onResume();
		screen.resume();
		renderView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		renderView.pause();
		screen.pause();

		if (isFinishing()) {
			screen.dispose();
		}
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.getInput().reset();
		
		this.screen.pause();
		this.screen.dispose();
		
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	public Screen getCurrentScreen() {
		return screen;
	}
	
	public Point getScreenSize() {
		return screenSize;
	}
	
	public PointF getScreenScale() {
		return screenScale;
	}
}