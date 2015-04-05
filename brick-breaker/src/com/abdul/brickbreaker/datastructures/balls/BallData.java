package com.abdul.brickbreaker.datastructures.balls;

public class BallData {
	private Ball parentBall;
	private boolean launched;
	private boolean hitBrick;
	
	public BallData(Ball parentBall, boolean launched) {
		this.parentBall = parentBall;
		this.launched = launched;
		this.hitBrick = false;
	}
	
	public void setLaunched(boolean launched) {
		this.launched = launched;
	}
	
	public boolean isLaunched() {
		return launched;
	}
	
	public Ball getParentBall() {
		return parentBall;
	}

}
