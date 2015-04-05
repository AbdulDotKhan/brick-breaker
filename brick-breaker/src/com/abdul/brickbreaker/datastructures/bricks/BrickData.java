package com.abdul.brickbreaker.datastructures.bricks;

public class BrickData {
	private Brick parentBrick;
	private int timesHit, numberOfHitsToDestroy;
	
	
	public BrickData(Brick parentBrick, int timesHit, int numberOfHitsToDestroy) {
		// the brick which contains the body, which has this object attached to it
		this.parentBrick = parentBrick;
		this.timesHit = timesHit;
		this.numberOfHitsToDestroy = numberOfHitsToDestroy;
	}
	
	public boolean shouldBeDestroyed() {
		// destroy brick if its been hit more times than its capacity
		return timesHit >= numberOfHitsToDestroy;
	}
	
	public void addHit() {
		// adds a hit
		timesHit += 1;
	}
	
	public Brick getParentBrick() {
		// get the parentBrick, aliasing is quite helpful with changing values
		return parentBrick;
	}

}
