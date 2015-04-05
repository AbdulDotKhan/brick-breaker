package com.abdul.brickbreaker.datastructures.levels;

import java.util.ArrayList;
import java.util.Iterator;

import com.abdul.brickbreaker.datastructures.balls.Ball;
import com.abdul.brickbreaker.datastructures.bricks.Brick;
import com.abdul.brickbreaker.datastructures.bricks.BrickData;
import com.abdul.brickbreaker.datastructures.constants.Filters;
import com.abdul.brickbreaker.datastructures.paddles.Paddle;
import com.abdul.brickbreaker.game.ContactHandler;
import com.abdul.brickbreaker.game.InputHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Level {
	// level maintenance variables
	private LevelState state; // enum for the state of the level
	private int secondsRemainingForCountdown; // countdown counter
	private int  numBricksDestroyed = 0; // destroyed bricks counter
	private InputHandler inputHandler; // input handler for the level
	private World world; // level's world
	private Box2DDebugRenderer debugRenderer; // renders bounds of box2d bodies for visual aid
	private ArrayList<Brick> bricks; // bricks of the level
	private Vector2 margin; // margin, in pixels, used for aspect ratios other than 16:9
	// note margin.x is for each side of the screen, and margin.y is for the bottom (not divided by two)
	private Paddle paddle; // the paddle
	private float worldWidth, worldHeight; // in meters
	private float pixelsPerMeter; // converion factor from box2d meters to pixels
	private Matrix4 combinedSpriteMatrix, combinedDebugMatrix; // projection matrices to draw the sprites and debugRenderer
	private SpriteBatch spriteBatch; // SpriteBatch to draw sprites
	
	public Ball ball; // the ball
	
	// main constructor
	public Level(LevelData levelData, LevelState state, float width, float height) {
		// initalization of fields
		
		// world set-up
		this.world = new World(new Vector2(), false);
		this.world.setContactListener(new ContactHandler());
		
		this.state = state;
		this.debugRenderer = new Box2DDebugRenderer();
		
		
		this.spriteBatch = new SpriteBatch();
		this.worldWidth = width; // in meters
		this.worldHeight = height; // in meters
		
		// taking care of proportions
		System.out.println("resize called in level");
		int newScreenWidthInPixels = Gdx.graphics.getWidth();
		int newScreenHeightInPixels = Gdx.graphics.getHeight();
		margin = new Vector2();
		
		// if the aspect ratio is smaller than the desired ratio
		if ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth() < worldHeight / worldWidth) {
			System.out.println("aspect ratio too small");
			// this probably means that the screen is not tall enough to fit the screen --> vertical letterbox
			newScreenHeightInPixels = Gdx.graphics.getHeight(); // height is the limiting factor, so we will base
																  // the screen's dimensions off of the screen's pixel
																  // height.
			newScreenWidthInPixels = Math.round((float) Gdx.graphics.getHeight() * worldWidth / worldHeight);
			System.out.println("newScreenWidthInPixels: " + newScreenWidthInPixels);
			

			margin.x = (Gdx.graphics.getWidth() - newScreenWidthInPixels) / 2; // puts the viewport right in the middle
			System.out.println(margin.x);
			margin.y = 0;
			pixelsPerMeter = (float) newScreenHeightInPixels / worldHeight;
		}
		
		// if the aspect ratio is larger than the desired ratio
		else if ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth() >= worldHeight / worldWidth) {
			System.out.println("aspect ratio too large");
			// this probably means that the screen is not tall enough to fit the screen --> TODO extra space will be made under the paddle
			newScreenWidthInPixels = Gdx.graphics.getWidth(); // width is the limiting factor, so we will base
			  										  		  // the screen's dimensions off of the screen's pixel
			  												  // width.
			newScreenHeightInPixels = Math.round((float) Gdx.graphics.getWidth() * worldHeight / worldWidth);
			
			margin.x = newScreenWidthInPixels;
			margin.y = Gdx.graphics.getHeight() - newScreenHeightInPixels; // puts the viewport as far up as possible, to leave space to control the paddle
			pixelsPerMeter = (float) newScreenWidthInPixels / worldWidth;			
		}
		
		else {System.out.println("There was an error");}
		
		System.out.println("Pixels per meter: " + pixelsPerMeter);
		
		System.out.println(margin.x);
		Vector2 spriteScaleFactor = new Vector2();
		spriteScaleFactor.x = (Gdx.graphics.getWidth() - 2 * margin.x) / Gdx.graphics.getWidth();
		spriteScaleFactor.y = (Gdx.graphics.getHeight() - margin.y) / Gdx.graphics.getHeight();
				
		OrthographicCamera spriteCamera = new OrthographicCamera(newScreenWidthInPixels, newScreenHeightInPixels);
		OrthographicCamera debugCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		combinedSpriteMatrix = spriteCamera.combined.scale(spriteScaleFactor.x, spriteScaleFactor.y, 0);
		combinedDebugMatrix = debugCamera.combined.scale(pixelsPerMeter, pixelsPerMeter,0);
		spriteBatch.setProjectionMatrix(combinedSpriteMatrix);		
		
		// set-up the input mechanisms
		this.inputHandler = new InputHandler(new Vector2(0,0), this.pixelsPerMeter);
		Gdx.input.setInputProcessor(this.inputHandler);
		
		// box2d initializations
		constructWalls(width, height);
		this.ball = new Ball(world, 3, -6, 0.2f, pixelsPerMeter, new Vector2(5f,5f), margin);
		// set-up the paddle with arbitrary x, y, width and height meter values
				this.paddle = new Paddle(world, -3, -5.2f, 2, 0.6f, pixelsPerMeter,
						new TextureRegion(new Texture(Gdx.files.internal("res/images/paddle.png")), 
								0, 0, width * pixelsPerMeter, height * pixelsPerMeter));
		// get the bricks from the levelData
		this.bricks = levelData.getArrayListOfBricks(world, width, height, pixelsPerMeter);
		
	}
	
	// begins the countdown to the launch of the ball
	public void startCountdown(int secondsToStart) {
		state = LevelState.COUNTDOWN; // set state
		secondsRemainingForCountdown = secondsToStart; // start at the inital time remaining
		
	}
	
	public void update(float delta) {
		if (state == LevelState.COUNTDOWN) {
			updateCountdown(); // run the timer
		}
		
		if (state == LevelState.STARTED) {
			//world.step(1f/60f, 3, 3);
			world.step(Gdx.graphics.getDeltaTime(), 5, 5);
			//System.out.println(Gdx.graphics.getDeltaTime());
			updateBricks();
		}
		
		paddle.update(inputHandler, worldWidth, pixelsPerMeter);
		ball.update();	
		inputHandler.refresh();
		//System.out.println(ball.resultantVelocity);
		//System.out.println(ball.body.getLinearVelocity().len());
		//System.out.println(" " + Math.sqrt(ball.body.getLinearVelocity().x * ball.body.getLinearVelocity().x + ball.body.getLinearVelocity().y * ball.body.getLinearVelocity().y));
	}
	
	public void render() {
		spriteBatch.begin();
		ball.render(this.spriteBatch, pixelsPerMeter);
		paddle.render(spriteBatch);
		int nullBodyCounter = 0;
		for (int i = 0; i < bricks.size(); i++) {
			if (bricks.get(i).body.getUserData() == null) {
				nullBodyCounter += 1;
			} else {
				bricks.get(i).render(spriteBatch);
			}
			
		}
		//System.out.println(nullBodyCounter);
		//System.out.println(bricks.size());
		spriteBatch.end();
		//debugRenderer.render(world, combinedDebugMatrix);
	}
	
	public void resize(int screenWidthInPixels, int screenHeightInPixels) {
		System.out.println("resize called in level");
		int newScreenWidthInPixels = screenWidthInPixels;
		int newScreenHeightInPixels = screenHeightInPixels;
		margin = new Vector2();
		
		// if the aspect ratio is smaller than the desired ratio
		if ((float) screenHeightInPixels / (float) screenWidthInPixels < worldHeight / worldWidth) {
			System.out.println("aspect ratio too small");
			// this probably means that the screen is not tall enough to fit the screen --> vertical letterbox
			newScreenHeightInPixels = screenHeightInPixels; // height is the limiting factor, so we will base
																  // the screen's dimensions off of the screen's pixel
																  // height.
			newScreenWidthInPixels = Math.round((float) screenHeightInPixels * worldWidth / worldHeight);
			System.out.println("newScreenWidthInPixels: " + newScreenWidthInPixels);
			

			margin.x = (screenWidthInPixels - newScreenWidthInPixels) / 2; // puts the viewport right in the middle
			System.out.println(margin.x);
			margin.y = 0;
			pixelsPerMeter = (float) newScreenHeightInPixels / worldHeight;
			this.inputHandler.pixelsPerMeter = this.pixelsPerMeter;
		}
		
		// if the aspect ratio is smaller than the desired ratio
		
		// if the aspect ratio is larger than the desired ratio
		else if ((float) screenHeightInPixels / (float) screenWidthInPixels >= worldHeight / worldWidth) {
			System.out.println("aspect ratio too large");
			// this probably means that the screen is not tall enough to fit the screen --> extra space will be made under the paddle
			newScreenWidthInPixels = screenWidthInPixels; // width is the limiting factor, so we will base
			  										  		  // the screen's dimensions off of the screen's pixel
			  												  // width.
			newScreenHeightInPixels = Math.round((float) screenWidthInPixels * worldHeight / worldWidth);
			
			margin.x = newScreenWidthInPixels;
			margin.y = screenHeightInPixels - newScreenHeightInPixels; // puts the viewport as far up as possible, to leave space to control the paddle
			pixelsPerMeter = (float) newScreenWidthInPixels / worldWidth;
			this.inputHandler.pixelsPerMeter = this.pixelsPerMeter;
			
		}
		
		else {System.out.println("There was an error");}
		
		System.out.println("Pixels per meter: " + pixelsPerMeter);
		
		System.out.println(margin.x);
		Vector2 spriteScaleFactor = new Vector2();
		spriteScaleFactor.x = (screenWidthInPixels - 2 * margin.x) / screenWidthInPixels;
		spriteScaleFactor.y = (screenHeightInPixels - margin.y) / screenHeightInPixels;
				
		OrthographicCamera spriteCamera = new OrthographicCamera(newScreenWidthInPixels, newScreenHeightInPixels);
		OrthographicCamera debugCamera = new OrthographicCamera(screenWidthInPixels, screenHeightInPixels);
		System.out.println("Margins: " + margin.x + " " + margin.y);
		System.out.println("Screen dimensions with margins: " + newScreenWidthInPixels + " " + newScreenHeightInPixels);
		System.out.println("Gdx screen dimensions: " + Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
		System.out.println("Sprite scale factors: " + spriteScaleFactor.x + " " + spriteScaleFactor.y);

		this.combinedSpriteMatrix = spriteCamera.combined.scale(spriteScaleFactor.x, spriteScaleFactor.y, 0);
		this.combinedDebugMatrix = debugCamera.combined.scale(pixelsPerMeter, pixelsPerMeter,0);
		spriteBatch.setProjectionMatrix(combinedSpriteMatrix);
		this.ball.resize(pixelsPerMeter);
		this.paddle.resize(pixelsPerMeter);
		for (int i = 0; i < bricks.size(); i++) {
			bricks.get(i).resize(pixelsPerMeter);
		}
	}
	
	private void constructWalls(float width, float height) {
		/*making the wall bodies*/
		// top wall
			
		// bodyDef required to make body
		BodyDef topWallBodyDef = new BodyDef();
		topWallBodyDef.type = BodyDef.BodyType.StaticBody;
		topWallBodyDef.position.set(0, height / 2);
		topWallBodyDef.awake = true;
		
		// add a body to the world
		Body topWallBody = world.createBody(topWallBodyDef);
		
		// shape for the paddle
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, 0); // going with a box shape
		
		// fixture definition created with the shape to make a fixture on the body
		FixtureDef tmpFixtureDef = new FixtureDef();
		tmpFixtureDef.shape = shape;
		tmpFixtureDef.density = 1.0f;
		tmpFixtureDef.friction = 0f;
		tmpFixtureDef.restitution = 0f;

		// add the fixture to the screen
		topWallBody.createFixture(tmpFixtureDef);
		
		// filter for collisions and ContactHandler
		Filter filter = new Filter();
		filter.categoryBits = Filters.WALL; // bit code for category
		
		// there is always only one fixture per body, so its always the first item in the list
		topWallBody.getFixtureList().get(0).setFilterData(filter);
		
		// dont let it rotate
		topWallBody.setFixedRotation(true);
		
		// garbage collect
		shape.dispose();
		
		// bottom wall
			
		// bodyDef required to make body
		BodyDef bottomWallBodyDef = new BodyDef();
		bottomWallBodyDef.type = BodyDef.BodyType.StaticBody;
		bottomWallBodyDef.position.set(0, - height / 2);
		bottomWallBodyDef.awake = false;
		
		// add a body to the world
		Body bottomWallBody = world.createBody(bottomWallBodyDef);
		
		// shape for the paddle
		shape = new PolygonShape();
		shape.setAsBox(width / 2, 0); // going with a box shape
		
		// fixture definition created with the shape to make a fixture on the body
		tmpFixtureDef = new FixtureDef();
		tmpFixtureDef.shape = shape;
		tmpFixtureDef.density = 1.0f;
		tmpFixtureDef.friction = 0f;
		tmpFixtureDef.restitution = 0f;
		
		// add the fixture to the screen
		bottomWallBody.createFixture(tmpFixtureDef);
		
		// filter for collisions and ContactHandler
		filter = new Filter();
		filter.categoryBits = Filters.WALL; // bit code for category
				
		// there is always only one fixture per body, so its always the first item in the list
		topWallBody.getFixtureList().get(0).setFilterData(filter);
		
		// dont let it rotate
		bottomWallBody.setFixedRotation(true);
		
		// garbage collect
		shape.dispose();
		
		// left wall
		// the paddle's sprite
			
		// bodyDef required to make body
		BodyDef leftWallBodyDef = new BodyDef();
		leftWallBodyDef.type = BodyDef.BodyType.KinematicBody;
		leftWallBodyDef.position.set(-width / 2, 0);
		leftWallBodyDef.awake = false;
		
		// add a body to the world
		Body leftWallBody = world.createBody(leftWallBodyDef);
		
		// shape for the paddle
		shape = new PolygonShape();
		shape.setAsBox(0, height / 2); // going with a box shape
		
		// fixture definition created with the shape to make a fixture on the body
		tmpFixtureDef = new FixtureDef();
		tmpFixtureDef.shape = shape;
		tmpFixtureDef.density = 1.0f;
		tmpFixtureDef.friction = 0f;
		tmpFixtureDef.restitution = 0f;
		
		// add the fixture to the screen
		leftWallBody.createFixture(tmpFixtureDef);

		// filter for collisions and ContactHandler
		filter = new Filter();
		filter.categoryBits = Filters.WALL; // bit code for category
				
		// there is always only one fixture per body, so its always the first item in the list
		topWallBody.getFixtureList().get(0).setFilterData(filter);
		
		// dont let it rotate
		leftWallBody.setFixedRotation(true);
		
		// garbage collect
		shape.dispose();
		
		
		// right wall
		// the paddle's sprite
			
		// bodyDef required to make body
		BodyDef rightWallBodyDef = new BodyDef();
		rightWallBodyDef.type = BodyDef.BodyType.KinematicBody;
		rightWallBodyDef.position.set(width / 2, 0);
		rightWallBodyDef.awake = false;
		
		// add a body to the world
		Body rightWallBody = world.createBody(rightWallBodyDef);
		
		// shape for the paddle
		shape = new PolygonShape();
		shape.setAsBox(0, height / 2); // going with a box shape
		
		// fixture definition created with the shape to make a fixture on the body
		tmpFixtureDef = new FixtureDef();
		tmpFixtureDef.shape = shape;
		tmpFixtureDef.density = 1.0f;
		tmpFixtureDef.friction = 0f;
		tmpFixtureDef.restitution = 0f;
		
		// add the fixture to the screen
		rightWallBody.createFixture(tmpFixtureDef);
		
		// filter for collisions and ContactHandler
		filter = new Filter();
		filter.categoryBits = Filters.WALL; // bit code for category
				
		// there is always only one fixture per body, so its always the first item in the list
		topWallBody.getFixtureList().get(0).setFilterData(filter);
		
		// dont let it rotate
		rightWallBody.setFixedRotation(true);
		
		// garbage collect
		shape.dispose();
	}
	
	private void updateCountdown() {
		secondsRemainingForCountdown -= Gdx.graphics.getDeltaTime(); // count down
		if (secondsRemainingForCountdown <= 0) {
			state = LevelState.STARTED;
			secondsRemainingForCountdown = 0;
		}
	}
	
	private void updateBricks() {
		// check bricks
		int nullCounter = 0;
		for (int i = 0; i < bricks.size(); i++) {
			if (bricks.get(i).body.getUserData() != null) {
				BrickData brickData = (BrickData)bricks.get(i).body.getUserData();
				if (brickData.shouldBeDestroyed()) {
					world.destroyBody(bricks.get(i).body);
					nullCounter += 1;
					numBricksDestroyed += 1;
				}
				else if (bricks.get(i).body == null) {
					nullCounter += 1;
				}
			}
			else {
				nullCounter += 1;
			}
		}
		
		// if all bodies are null
		if (nullCounter == bricks.size()) {
			System.out.println(numBricksDestroyed);
			state = LevelState.WON;
			System.out.println(world.getBodyCount());
			
			Iterator<Body> bi = world.getBodies();
	        
			while (bi.hasNext()){
			    Body b = bi.next();
			    if (b != null) {
			        System.out.println(b.getFixtureList().get(0).getFilterData().categoryBits);
			    }
			}
			
		}
		
		// clean up the list
		ArrayList<Brick> newBricks = new ArrayList<Brick>();
		for (int i = 0; i < bricks.size(); i++) {
			if (bricks.get(i).body != null) {
				newBricks.add(bricks.get(i));
			}
		}
		bricks = newBricks;
	}
}
