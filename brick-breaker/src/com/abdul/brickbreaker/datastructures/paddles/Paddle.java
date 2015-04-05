package com.abdul.brickbreaker.datastructures.paddles;

import com.abdul.brickbreaker.game.InputHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Paddle {
	public Sprite sprite;
	public Body body;
	private float width, height;
	
	public Paddle(World world, float x, float y, float width, float height, float pixelsPerMeter, TextureRegion region) {
		// the paddle's sprite
		sprite = new Sprite(region);
		
		this.width = width;
		this.height = height;
		
		
		// bodyDef required to make body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.position.set(x, y);
		bodyDef.awake = false;
		
		// add a body to the world
		body = world.createBody(bodyDef);
		
		// shape for the paddle
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2); // going with a box shape
		
		// fixture definition created with the shape to make a fixture on the body
		FixtureDef tmpFixtureDef = new FixtureDef();
		tmpFixtureDef.shape = shape;
		tmpFixtureDef.density = 1.0f;
		tmpFixtureDef.friction = 0f;
		tmpFixtureDef.restitution = 0f;
		
		// add the fixture to the screen
		body.createFixture(tmpFixtureDef);
		
		// filter for collisions and contact handler
		Filter filter = new Filter();
		filter.categoryBits = 2; // category code for the paddle
		
		// only one fixture, therefore the first item in the list
		// set the fixture
		body.getFixtureList().get(0).setFilterData(filter);
		
		// dont let it rotate
		body.setFixedRotation(true);
		
		// garbage collect
		shape.dispose();

		// attach a paddleData object for later use
		body.setUserData(new PaddleData());
		
		// sprite and image operations
		sprite = new Sprite(new Texture(Gdx.files.internal("res/images/paddle.png")));
		sprite.setSize(width * pixelsPerMeter, height * pixelsPerMeter);
		
		
		
	}
	
	public void resize(float pixelsPerMeter) {
		sprite.setSize(width * pixelsPerMeter, height * pixelsPerMeter);
	}
	
	public void update(InputHandler inputHandler, float screenWidthInMeters, float pixelsPerMeter) {
		// move the paddle according to touchDrag movements
		body.setTransform(new Vector2(body.getWorldCenter().x + inputHandler.dragDisplacement.x, 
				body.getWorldCenter().y), 
				body.getAngle());
		
		
		
		// check the boundaries
		checkIfWithinBoundariesOfWalls(screenWidthInMeters);
		sprite.setPosition((body.getWorldCenter().x - width / 2) * pixelsPerMeter, (body.getWorldCenter().y - height / 2) * pixelsPerMeter);
	}
	
	public void render(SpriteBatch spriteBatch) {
		sprite.draw(spriteBatch);
	}
	
	private void checkIfWithinBoundariesOfWalls(float screenWidthInMeters) {
		// past left
		if (body.getWorldCenter().x - width / 2 <= - screenWidthInMeters / 2) {
			//System.out.println("too far left");
			body.setTransform(new Vector2(- screenWidthInMeters / 2 + width / 2, body.getWorldCenter().y), body.getAngle());
		}
		
		// past right
		if (body.getWorldCenter().x + width / 2 >= screenWidthInMeters / 2) {
			//System.out.println("too far right");
			body.setTransform(new Vector2(screenWidthInMeters / 2 - width / 2, body.getWorldCenter().y), body.getAngle());
		}
	}
}
