package com.abdul.brickbreaker.game;

import com.abdul.brickbreaker.datastructures.levels.Level;
import com.abdul.brickbreaker.datastructures.levels.LevelData;
import com.abdul.brickbreaker.datastructures.levels.LevelState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

public class LevelScreen implements Screen {
	public BrickBreaker game;
	Level level;
	private World world;
	
	public LevelScreen(BrickBreaker game, LevelData levelData) {
		this.game = game;
		this.world = new World(new Vector2(0,0), false);
		this.level = new Level(levelData, LevelState.COUNTDOWN, 9, 16);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0.5f, 0.9f, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		level.update(delta);
		level.render();

	}

	@Override
	public void resize(int width, int height) {
		level.resize(width, height);
		System.out.println("resize called in screen");
	}

	@Override
	public void show() {
		 //level.ball.body.setLinearVelocity(5f, 5f);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
