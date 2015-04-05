package com.abdul.brickbreaker.game;

import com.abdul.brickbreaker.datastructures.bricks.BrickData;
import com.abdul.brickbreaker.datastructures.constants.Filters;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactHandler implements ContactListener {
	@Override
	public void beginContact(Contact contact) {
		//System.out.println("contact");
		 switch (contact.getFixtureA().getFilterData().categoryBits) {
		 	case Filters.BALL: // BALL
		 		switch (contact.getFixtureB().getFilterData().categoryBits) {
		 			case Filters.BALL:
		 				break;
		 			case Filters.PADDLE:
		 				break;
		 			case Filters.BRICK:
		 				BrickData tempBrickData = (BrickData)contact.getFixtureB().getBody().getUserData();
			 			tempBrickData.addHit();
			 			contact.getFixtureB().getBody().setUserData(tempBrickData);
		 				break;
		 			case Filters.WALL:
		 				System.out.println("touched wall");
		 			default:
		 				break;
		 		}
		 		break;
		 	case Filters.PADDLE: // PADDLE
		 		switch (contact.getFixtureB().getFilterData().categoryBits) {
		 			case Filters.BALL:
		 				break;
		 			case Filters.PADDLE:
		 				break;
		 			case Filters.BRICK:
		 				break;
		 			case Filters.WALL:
		 				break;
		 			default:
		 				break;
		 		}
		 		break;
		 	case Filters.BRICK: // BRICK
		 		switch (contact.getFixtureB().getFilterData().categoryBits) {
		 			case Filters.BALL:
		 				BrickData tempBrickData = (BrickData)contact.getFixtureA().getBody().getUserData();
			 			tempBrickData.addHit();
			 			contact.getFixtureA().getBody().setUserData(tempBrickData);
		 				break;
		 			case Filters.PADDLE:
		 				break;
		 			case Filters.BRICK:
		 				break;
		 			case Filters.WALL:
		 				break;
		 			default:
		 				break;
		 		}
		 		break;
		 	case Filters.WALL: // WALL
		 		switch (contact.getFixtureB().getFilterData().categoryBits) {
		 			case Filters.BALL:
		 				break;
		 			case Filters.PADDLE:
		 				break;
		 			case Filters.BRICK:
		 				break;
		 			case Filters.WALL:
		 				break;
		 			default:
		 				break;
		 		}
		 		break;
		 	default:
		 		System.out.println("Unknown body type");
		 		break;
		 }

	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
	
	

}
