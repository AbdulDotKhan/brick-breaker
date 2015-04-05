package com.abdul.brickbreaker.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class InputHandler implements InputProcessor {
	public Vector2 pointerLocation; // in meters
	public float pixelsPerMeter; // in meters
	public Vector2 dragDisplacement; // in meters
	public boolean touchedDown = false;
	public boolean up, down, left, right;
	
	public InputHandler(Vector2 pointerLocationInMeters, float pixelsPerMeter) {
		this.pointerLocation = new Vector2(pointerLocationInMeters);
		this.dragDisplacement = new Vector2(0,0);
		this.pixelsPerMeter = pixelsPerMeter;
		up = down = left = right = false;
	}
	
	public void refresh() {
		// gotta reset the displacement
		dragDisplacement.set(0,0);
		touchedDown = false;
		up = down = left = right = false;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.DPAD_UP:
				up = true;
			case Input.Keys.DPAD_DOWN:
				down = true;
			case Input.Keys.DPAD_LEFT:
				left = true;
			case Input.Keys.DPAD_RIGHT:
				right = true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// prob not needed
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// prob not needed
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pointer==0) {
			// prob not needed for touch, maybe pc
			// TODO - add better PC controls
			touchedDown = true;
			pointerLocation.set((float)screenX / pixelsPerMeter, (float)(Gdx.graphics.getHeight() - screenY) / pixelsPerMeter);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer==0) {
			// prob not needed for touch, maybe pc
			// TODO - add better PC controls
			touchedDown = false;
			pointerLocation.set((float)screenX / pixelsPerMeter, (float)(Gdx.graphics.getHeight() - screenY) / pixelsPerMeter);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer==0) {
			//System.out.println("dragging");
			//System.out.println(screenX + " " + screenY);
			// the displacement is the distance dragged, --> (x2 - x1, y2 - y1)
			// first gotta convert where the origin is at the bottom left instead of top left, then put into meters
			updateDrag(screenX, screenY);
			pointerLocation.set((float)screenX / pixelsPerMeter, (float)(Gdx.graphics.getHeight() - screenY) / pixelsPerMeter);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// prob not needed for touch, maybe pc
		// TODO - add better PC controls
		return false;
	}
	
	private void updateDrag(int screenX, int screenY) {
		if (touchedDown!=true) {
			dragDisplacement = new Vector2(1.5f * ((float)(screenX) / pixelsPerMeter - pointerLocation.x), 
					(float)(Gdx.graphics.getHeight() - screenY) / pixelsPerMeter - pointerLocation.y);
		}
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
