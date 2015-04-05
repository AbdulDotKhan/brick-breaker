package com.abdul.brickbreaker.leveleditor;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.abdul.brickbreaker.datastructures.bricks.BrickType;

public class LevelEditor {
	// swt objects
	private Shell shell;
	private Display display;
	
	// level editor objects
	private LevelCanvas levelCanvas;
	private BrickSelector brickSelector;
	private MenuBar menuBar;


	public void run() throws IOException {
		// SWT stuff to create the main window
		display = new Display();
		shell = new Shell(display);
		shell.setSize(1024, 720);
		shell.setText("Level Editor");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);
		
		// adding brick types
		ArrayList<BrickType> brickTypes = new ArrayList<BrickType>();
		brickTypes.add(new BrickType("single-hit", "yellow-brick", "basic brick\nyellow\none hit", "res/images/brick1.png", display));
		brickTypes.add(new BrickType("single-hit", "blue-brick", "basic brick\nblue\none hit", "res/images/brick2.png", display));
		brickTypes.add(new BrickType("single-hit", "green-brick", "basic brick\ngreen\none hit", "res/images/brick3.png", display));
		brickTypes.add(new BrickType("3-hit", "gray-brick", "basic brick\nblue\nonethree hits", "res/images/brick4.png", display));
		brickTypes.add(new BrickType("unbreakable", "black-brick", "special brick\nblue\ninfinite hit", "res/images/unbreakable_brick.png", display));
		brickTypes.add(new BrickType("exploding", "orange-brick", "special brick\norange\none hit", "res/images/exploding_brick.png", display));
		

		levelCanvas = new LevelCanvas(shell, display, null, 360, 640, brickTypes, brickTypes.get(0));
		brickSelector = new BrickSelector(shell, levelCanvas, brickTypes);
		menuBar = new MenuBar(shell, levelCanvas);

		
		// open the window
		shell.open();
		
		// Create and check the event loop
		while (!shell.isDisposed()) {
			// System.out.println(shell.getSize().x + " " + shell.getSize().y);
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	public static void main(String[] args) {
		try {
			new LevelEditor().run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}