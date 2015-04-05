package com.abdul.brickbreaker.datastructures.bricks;

import com.abdul.brickbreaker.datastructures.constants.Filters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Brick {
	public Body body;
	public String data;
	public Sprite sprite;
	public float x, y, width, height;
	
	public Brick(World world, float x, float y, float width, float height, float pixelsPerMeter) {
		this.x = x - width / 2;
		this.y = y - height / 2;
		this.width = width;
		this.height = height;
		sprite = new Sprite(new Texture(Gdx.files.internal("res/images/bricks/unbreakable_brick.png")));
		sprite = new Sprite(new Texture(Gdx.files.internal("res/images/bricks/brick3.png")));
		sprite.setX((x - width / 2) * pixelsPerMeter);
		sprite.setY((y - height / 2) * pixelsPerMeter);
		sprite.setSize(width * pixelsPerMeter, height * pixelsPerMeter);
		
		// body definitions required to create a body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.position.set(x, y);
		bodyDef.awake = false;
		
		// adds a body to the world
		body = world.createBody(bodyDef);
		
		// making a shape for the body's fixture, which takes up space in the world
		PolygonShape box = new PolygonShape();
		box.setAsBox(width / 2, height / 2);

		// the fixture definition used to make a fixture
		FixtureDef tmpFixtureDef = new FixtureDef();
		tmpFixtureDef.shape = box;
		tmpFixtureDef.density = 1.0f;
		tmpFixtureDef.friction = 0f;
		
		// add the fixture to the body
		body.createFixture(tmpFixtureDef);
		
		// filter for collisions and ContactHandler
		Filter filter = new Filter();
		filter.categoryBits = Filters.BRICK; // bit code for category
		
		// there is always only one fixture per body, so its always the first item in the list
		body.getFixtureList().get(0).setFilterData(filter);
		
		// wouldn't want it to shift
		body.setFixedRotation(false);
		
		// gotta dispose it
		box.dispose();
		
		// attaching a BrickData, which allows for accessing the parent brick from the body.getUserData() method.
		body.setUserData(new BrickData(this, 0, 1));
		
		System.out.println("x: " + body.getWorldCenter().x + "\ny: " + body.getWorldCenter().y + "\n" + "width: " + width + "\nheight: " + height + "\n");
	}

	public Body getBody() {
		return body;
	}
	
	public void render(SpriteBatch spriteBatch) {
		sprite.draw(spriteBatch);
	}
	
	public void resize(float pixelsPerMeter) {
		System.out.println(pixelsPerMeter);
		sprite.setSize(width * pixelsPerMeter, height * pixelsPerMeter);
		sprite.setX(x * pixelsPerMeter);
		sprite.setY(y * pixelsPerMeter);
	}


}
