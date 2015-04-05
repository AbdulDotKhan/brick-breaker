package com.abdul.brickbreaker.leveleditor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;

import com.abdul.brickbreaker.datastructures.bricks.BrickInfo;
import com.abdul.brickbreaker.datastructures.bricks.BrickType;
import com.abdul.brickbreaker.datastructures.levels.LevelData;

public class LevelCanvas {
	public LevelData levelData;
	public Group container;
	public GC canvasController;
	public Canvas canvas;
	public Point pixelsPerMeter;
	public int maxBricksAcross, maxBricksDown;
	public int width, height; // in pixels
	public int paddleSafeHeight; // safety zone where there can never be bricks, to make the game fair
	public float pixelsPerBrickX, pixelsPerBrickY;
	public int imageX, imageY;
	public BrickType brickType;
	public ArrayList<BrickType> brickTypes;
	public boolean leftClick = false;
	public boolean rightClick = false;
	
	// when a "new" level is created
	public LevelCanvas() {
		
	}
	
	// when a level is opened from a file
	public LevelCanvas(Composite parent, 
			Display display, 
			ObjectInputStream savedLevelData, 
			int widthInPixels, 
			int heightInPixels, 
			ArrayList<BrickType> brickTypes,
			BrickType initBrickType) {
		
		// fields from parameters
		this.brickTypes = brickTypes;
		this.brickType = initBrickType;
		this.width = widthInPixels;
		this.height = heightInPixels;
		
		// arbitrary fields ... TODO - change this system
		this.paddleSafeHeight = 265; // TODO - make int a percentage
		this.maxBricksAcross = 15;
		this.maxBricksDown = 25;
		this.pixelsPerBrickX = width / maxBricksAcross;
		this.pixelsPerBrickY = (height - paddleSafeHeight) / maxBricksDown;
		
		// open file, catch exceptions, if savedLevelData == null, use a blank levelData
		try {
			levelData = (LevelData) savedLevelData.readObject();
		} catch (ClassNotFoundException e) {
			MessageBox errorReadingFileMessageBox = new MessageBox(canvas.getShell());
			errorReadingFileMessageBox.setText("There was an error reading your file type.");
			errorReadingFileMessageBox.setMessage(errorReadingFileMessageBox.getText());
			errorReadingFileMessageBox.open();
			e.printStackTrace();
		} catch (NullPointerException e) {
			levelData = new LevelData(new BrickInfo[maxBricksDown][maxBricksAcross]);
		} catch (Exception e) {
			MessageBox unknownErrorMessageBox = new MessageBox(canvas.getShell());
			unknownErrorMessageBox.setText("There has been an unknown error.");
			unknownErrorMessageBox.setMessage(unknownErrorMessageBox.getText());
			unknownErrorMessageBox.open();
			e.printStackTrace();
		}
		
		
		
		// instantiating the canvas
		canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setSize(width, height);
		
		// setting up the canvas' formating within its composite
		GridData gridData = new GridData(GridData.CENTER, GridData.CENTER, true, true);
		gridData.widthHint = width;
		gridData.heightHint = height;
		gridData.verticalSpan = 2;
		canvas.setLayoutData(gridData);
		
		// setting up all of the EventListeners
		setUpPaintListener();
		setUpMouseMoveListener();
		setUpMouseListener();
		
		// pack and draw the canvas
		canvas.pack();
		canvas.redraw();
	}
	
	// change the image
	public void setBrickType(BrickType brickType) {
		this.brickType = brickType;
	}
	
	public void openLevel(String fileDir) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream savedLevelData = new ObjectInputStream(new FileInputStream(fileDir));
		levelData = (LevelData) savedLevelData.readObject();
	}
	
	// helper method
	private void setUpPaintListener() {
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				// set the background and clear the screen
				e.gc.setBackground(new Color(canvas.getDisplay(), 255, 216, 0));
				e.gc.setForeground(new Color(canvas.getDisplay(), 0, 0, 0));
				e.gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
				
				if (leftClick) {
					levelData.setBrickInfo(brickType.getCopy(), (int)(imageX / pixelsPerBrickX), (int)(imageY / pixelsPerBrickY));
				}
				
				if (rightClick) {
					levelData.setBrickInfo(null, (int)(imageX / pixelsPerBrickX), (int)(imageY / pixelsPerBrickY));
				}
				
				// TODO - cycle through the levelData's BrickInfo[][] and draw the brick when != null
				for (int y = 0; y < levelData.bricks.length; y++) {
					for (int x = 0; x < levelData.bricks[0].length; x++) {
						if (levelData.bricks[y][x] != null) {
							Image img = null;
							for (int i = 0; i < brickTypes.size(); i++) {
								if (brickTypes.get(i).imageDir.equals(levelData.bricks[y][x].imageDir)) {
									img = brickTypes.get(i).image;
								}
							}
							
							try {
								e.gc.drawImage(img, 
										(int)(levelData.bricks[y][x].x * pixelsPerBrickX), 
										(int)(levelData.bricks[y][x].y * pixelsPerBrickY));
							}
							
							catch(NullPointerException npe) {
								npe.printStackTrace();
							}
						}
					}
				}
				
				// draw the grid
				for (int y = 0; y <= maxBricksDown; y++) {
					e.gc.drawLine(0, Math.round(y * pixelsPerBrickY), canvas.getSize().x, Math.round(y * pixelsPerBrickY));
				}
				
				for (int x = 0; x <= maxBricksAcross; x++) {
					e.gc.drawLine(Math.round(x * pixelsPerBrickX), 0, Math.round(x * pixelsPerBrickX), canvas.getSize().y - paddleSafeHeight);
				}
				
				// scaling the image
				ImageData imageData = brickType.image.getImageData().scaledTo(Math.round(pixelsPerBrickX), Math.round(pixelsPerBrickY));
				
				// check bounds
				if (imageY + pixelsPerBrickY > canvas.getSize().y - paddleSafeHeight) {
					imageY = Math.round(pixelsPerBrickY * maxBricksDown) - imageData.height;
				}
				
				// make the cursor transparent
				e.gc.setAlpha(100);
				e.gc.drawImage(new Image(canvas.getDisplay(), imageData), imageX, imageY);
				
				// set alpha back to normal
				e.gc.setAlpha(0);

				
				
			}
		});
	}
	
	// helper method
	private void setUpMouseMoveListener() {
		// update the mouses position whenever it moves
		canvas.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				imageX = (int)((int)(e.x / pixelsPerBrickX) * pixelsPerBrickX);
				imageY = (int)((int)(e.y / pixelsPerBrickY) * pixelsPerBrickY);
				canvas.redraw();
			}
		});
	}
	
	// helper method
	private void setUpMouseListener() {
		// mainly for clicking
		canvas.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {				
			}
			@Override
			public void mouseDown(MouseEvent e) {
				imageX = (int)((int)(e.x / pixelsPerBrickX) * pixelsPerBrickX);
				imageY = (int)((int)(e.y / pixelsPerBrickY) * pixelsPerBrickY);
				if (e.button == 1) { // left click
					levelData.setBrickInfo(brickType.getCopy(), (int)(imageX / pixelsPerBrickX), (int)(imageY / pixelsPerBrickY));
					leftClick = true;
				}
				
				if (e.button == 3) { // right click
					levelData.setBrickInfo(null, (int)(imageX / pixelsPerBrickX), (int)(imageY / pixelsPerBrickY));
					rightClick = true;
					
				}
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					leftClick = false;
				}
				
				if (e.button == 3) {
					rightClick = false;
					
				}
			}
		});
	}

}
