package com.abdul.brickbreaker.datastructures.bricks;

import java.io.Serializable;

// NOTE: only used in the level editor
public class BrickInfo implements Serializable {
	private static final long serialVersionUID = 1202463162107485952L;
	public float x, y, width, height; // in meters, generally ... width/height = 2
	public String type, imageDir; // type is the brick type
	// x, y from the top left when created in the level editor

	public BrickInfo(float x, float y, float width, float height, String type, String imageDir) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		this.imageDir = imageDir;
	}
	
	
}
