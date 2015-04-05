package com.abdul.brickbreaker.datastructures.levels;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.abdul.brickbreaker.datastructures.bricks.Brick;
import com.abdul.brickbreaker.datastructures.bricks.BrickInfo;
import com.abdul.brickbreaker.datastructures.bricks.BrickType;
import com.abdul.brickbreaker.game.BrickBreaker;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

// level data is the bridge between the level editor and the brick breaker game
public class LevelData implements Serializable {
	private static final long serialVersionUID = 6086239248567221523L;
	public BrickInfo[][] bricks;
	public float numBricksAcross;
	public float numBricksDown;
	
	public LevelData(BrickInfo[][] bricks) {
		this.bricks = bricks;
		numBricksAcross = (float)bricks[0].length;
		numBricksDown = (float)bricks.length;
	
	}
	
	// will be used in the main game to get all the bricks ready for box2d!
	public ArrayList<Brick> getArrayListOfBricks(World world, float worldWidth, float worldHeight, float pixelsPerMeter) {
		ArrayList<Brick> brickObjects = new ArrayList<Brick>();
		for (int y = 0; y < bricks.length; y++) {
			for (int x = 0; x < bricks[0].length; x++) {
				if (bricks[y][x] != null) {
					brickObjects.add(new Brick(world, (float)x * BrickBreaker.BRICK_WIDTH - 4.2f, 
							(-(float)y * BrickBreaker.BRICK_HEIGHT + 7.8f), 
							BrickBreaker.BRICK_WIDTH, 
							BrickBreaker.BRICK_HEIGHT,
							pixelsPerMeter));
				}
			}
		}
		return brickObjects;
	}
	
	// used when "painting" the level, stamps a brick to a slot
	public void setBrickInfo(BrickType brickType, int x, int y){
		try {
			bricks[y][x] = new BrickInfo(x, y, 2, 1, brickType.type, brickType.imageDir);
		}
		catch (Exception a) {
			try {bricks[y][x] = null;}
			catch (Exception e) {}
		}
	}
	
	// saves the level
	public void save(String dir) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(dir);
		ObjectOutputStream saveStream = new ObjectOutputStream(fileOutputStream);
		saveStream.writeObject(this);
	}
}
