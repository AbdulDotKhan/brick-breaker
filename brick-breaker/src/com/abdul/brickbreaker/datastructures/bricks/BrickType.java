package com.abdul.brickbreaker.datastructures.bricks;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class BrickType {
	// kinda self-explanatory
	public String type, name, description, imageDir;
	public Image image;
	
	public BrickType(String type, 
			String name, 
			String description, 
			String imageDir, 
			Display display) {
		
		this.type = type;
		this.name = name;
		this.description = description;
		this.image = new Image(display, new ImageData(imageDir).scaledTo(24, 15));
		this.imageDir = imageDir;
	}
	
	// gets a copy to avoid aliasing
	public BrickType getCopy(){
		return this;
	}
}
