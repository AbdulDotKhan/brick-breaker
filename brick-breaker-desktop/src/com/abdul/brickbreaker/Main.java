package com.abdul.brickbreaker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.abdul.brickbreaker.game.BrickBreaker;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "brick-breaker";
		cfg.useGL20 = true;
		cfg.width = 240;
		cfg.height = 400;
		
		new LwjglApplication(new BrickBreaker(new FileInputStream("res/levels/level.dat")), cfg);
	}
}