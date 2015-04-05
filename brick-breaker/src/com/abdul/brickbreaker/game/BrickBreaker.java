package com.abdul.brickbreaker.game;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import com.abdul.brickbreaker.datastructures.levels.LevelData;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;

public class BrickBreaker extends Game {
	public LevelScreen levelScreen;
	FPSLogger log = new FPSLogger();
	public float pixelsPerMeter;
	public float screenWidthInMeters, screenHeightInMeters;
	
	public static float BRICK_WIDTH = 0.6f;
	public static float BRICK_HEIGHT = 0.375f;
	
	public ObjectInputStream levelInputStream;
	
	// different constructors are used for different operating systems
	public BrickBreaker(String levelPath) throws FileNotFoundException, IOException {
		levelInputStream = new ObjectInputStream(new FileInputStream(levelPath));
	}
	
	public BrickBreaker(FileDescriptor fileDescriptor) throws IOException {
		levelInputStream = new ObjectInputStream(new FileInputStream(fileDescriptor));
	}
	
	public BrickBreaker(InputStream inputStream) throws IOException {
		System.out.println("InputStream constructor");
		
		levelInputStream = new ObjectInputStream(inputStream);
	}
	
	@Override
	public void create() {
		Gdx.graphics.setVSync(true);
		LevelData levelData = null;
		try {
			levelData = (LevelData) levelInputStream.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// if level can be found
		if (levelData != null) {
			screenWidthInMeters = 9.0f;
			screenHeightInMeters = 16.0f;
			try {
				resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());}
			catch (Exception e) {}
			
			levelScreen = new LevelScreen(this, levelData);
			setScreen(levelScreen);
		}
		
		else {
			System.out.println("The level could not be found.");
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
		log.log();
	}

	@Override
	public void resize(int screenWidthInPixels, int screenHeightInPixels) {
		super.resize(screenWidthInPixels, screenHeightInPixels);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
