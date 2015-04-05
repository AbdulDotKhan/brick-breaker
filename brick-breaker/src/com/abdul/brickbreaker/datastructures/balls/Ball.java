package com.abdul.brickbreaker.datastructures.balls;

import com.abdul.brickbreaker.datastructures.constants.Filters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Ball {
	public Body body;
	public Sprite sprite;
	public float radius;
	public float resultantVelocity;

	public Ball(World world, float x, float y, float radius, float pixelsPerMeter, Vector2 velocity, Vector2 margin) {		
		// basic field initializations
		this.radius = radius;
		this.resultantVelocity = (float) Math.sqrt((float)(velocity.x * velocity.x + velocity.y * velocity.y));
		
		// body definitions required to create a body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.awake = false;

		// adds a body to the world
		body = world.createBody(bodyDef);
		
		// making a shape for the body's fixture, which takes up space in the world
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);

		// the fixture definition used to make a fixture
		FixtureDef tmpFixtureDef = new FixtureDef();
		tmpFixtureDef.shape = circle;
		tmpFixtureDef.density = 1.0f;
		tmpFixtureDef.friction = 0f;
		tmpFixtureDef.restitution = 1f;
		
		// add the fixture to the body
		body.createFixture(tmpFixtureDef);
		
		// filter for collisions and ContactHandler
		Filter filter = new Filter();
		filter.categoryBits = Filters.BALL; // bit code for category
		
		// there is always only one fixture per body, so its always the first item in the list
		body.getFixtureList().get(0).setFilterData(filter);
		
		// wouldn't want it to shift
		body.setFixedRotation(false);
		
		// gotta dispose it
		circle.dispose();
		
		// TODO temp init velocity
		body.setLinearVelocity(velocity);
		
		// sprite and image operations
		Texture texture = new Texture(Gdx.files.internal("res/images/ball.png"));
		sprite = new Sprite(texture);
		sprite.setSize(texture.getWidth(), texture.getHeight());
		update();
	
	}
	
	public void update() {
		fixVelocityComponents(15f);
		
	}
	
	public void render(SpriteBatch spriteBatch, float pixelsPerMeter) {
		sprite.setPosition((body.getWorldCenter().x - radius) * pixelsPerMeter, (body.getWorldCenter().y - radius) * pixelsPerMeter);
		sprite.draw(spriteBatch);
	}
	
	private void fixVelocityComponents(float offset) {
		float a = getAngle(body.getLinearVelocity());
		//System.out.println(a);
		
		if (a >= 0 && a < offset) {
			//System.out.println("0-15");
			body.setLinearVelocity(body.getLinearVelocity().rotate((offset - a)));
			a = offset - a;
		}
		if (a >= 90 - offset && a < 90) {
			//System.out.println("75-90");
			body.setLinearVelocity(body.getLinearVelocity().rotate((90 - offset - a)));
			a = 90 - offset - a;
		}
		if (a >= 90 && a < 90 + offset) {
			//System.out.println("90-105");
			body.setLinearVelocity(body.getLinearVelocity().rotate((90 + offset - a)));
			a = 90 + offset - a;
		}
		if (a >= 180 - offset && a < 180) {
			//System.out.println("165-180");
			body.setLinearVelocity(body.getLinearVelocity().rotate((180 - offset - a)));
			a = 180 - offset - a;
		}
		if (a >= 180 && a < 180 + offset) {
			//System.out.println("180-195");
			body.setLinearVelocity(body.getLinearVelocity().rotate((180 + offset - a)));
			a = 180 + offset - a;
		}
		if (a >= 270 - offset && a < 270) {
			//System.out.println("255-270");
			body.setLinearVelocity(body.getLinearVelocity().rotate((270 - offset - a)));
			a = 270 - offset - a;
		}
		if (a >= 270 && a < 270 + offset) {
			//System.out.println("270-295");
			body.setLinearVelocity(body.getLinearVelocity().rotate((270 + offset - a)));
			a = 270 + offset - a;
		}
		if (a >= 360 - offset && a < 360) {
			//System.out.println("345-360");
			body.setLinearVelocity(body.getLinearVelocity().rotate((360 - offset - a)));
			a = 360 - offset - a;
		}
		
		// check for resultant consistency
		if (body.getLinearVelocity().len() < resultantVelocity) {
			System.out.println("fixing speed");
			body.setLinearVelocity(resultantVelocity * MathUtils.cosDeg(a), resultantVelocity * MathUtils.sinDeg(a));
		}
		//body.setLinearVelocity(20f, 20f);
//		// check for resultant consistency
//		if (Math.abs(body.getLinearVelocity().dot(body.getLinearVelocity()) - resultantVelocity * resultantVelocity) <) {
//			System.out.println("r is too large");
//			body.setLinearVelocity(resultantVelocity * MathUtils.cosDeg(a), resultantVelocity * MathUtils.sinDeg(a));
//		}
		
//		System.out.println(Math.sqrt((double)body.getLinearVelocity().dot(body.getLinearVelocity())));
//		System.out.println(resultantVelocity);
	}
	
	private void increaseResultant(float increment) {
		resultantVelocity += increment;
	}
	
	public void resize(float pixelsPerMeter) {
		sprite.setSize(2 * radius * pixelsPerMeter, 2 * radius * pixelsPerMeter);
	}
	
	private float getAngle(Vector2 vec) {
		float ra = (float) Math.abs(Math.atan((double)vec.y / (double)vec.x)) * MathUtils.radiansToDegrees;
		float a = 0;
		
		if (vec.x >= 0 && vec.y >= 0) {
			a = ra;
		}
		if (vec.x >= 0 && vec.y < 0) {
			a = 360 - ra;
		}
		if (vec.x < 0 && vec.y >= 0) {
			a = 180 - ra;
		}
		if (vec.x < 0 && vec.y < 0) {
			a = 180 + ra;
		}
		return a;
	}

}
