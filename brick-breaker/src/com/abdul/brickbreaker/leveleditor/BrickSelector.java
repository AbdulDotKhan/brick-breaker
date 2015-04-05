package com.abdul.brickbreaker.leveleditor;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import com.abdul.brickbreaker.datastructures.bricks.BrickInfo;
import com.abdul.brickbreaker.datastructures.bricks.BrickType;

public class BrickSelector {
	public ArrayList<BrickType> brickTypes;
	private List list;
	private BrickType brickType;
	private LevelCanvas levelCanvas;
	private Text details;
	
	public BrickSelector(Composite parent, LevelCanvas levelCanvas, ArrayList<BrickType> brickTypes) {
		this.levelCanvas = levelCanvas;
		this.brickTypes = brickTypes;
		
		// TODO - make into a list of brickTypes
		list = new List(parent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		list.setItems(getBrickNames());
		System.out.println(list.getItem(0) + "\n" + list.getItem(1));
		
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.verticalSpan = 1;
		int listHeight = list.getItemHeight() * 12;
		Rectangle trim = list.computeTrim(0, 0, 0, listHeight);
		gridData.heightHint = trim.height;
		gridData.widthHint = 300;
		list.setLayoutData(gridData);
		
		
		
		// the details
		details = new Text(parent, SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP);
		details.setText(brickTypes.get(list.getFocusIndex()).description);
		gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.verticalSpan = 1;
		gridData.widthHint = 300;
		details.setLayoutData(gridData);
		details.pack();
		
		setUpSelectionListener();
	}
	
	private void setUpSelectionListener() {
		list.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				brickType = brickTypes.get(list.getFocusIndex());
				levelCanvas.brickType = brickType;
				details.setText(brickTypes.get(list.getFocusIndex()).description);
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				brickType = brickTypes.get(list.getFocusIndex());
				levelCanvas.brickType = brickType;
				details.setText(brickTypes.get(list.getFocusIndex()).description);
			}
		});
	}
	
	public String[] getBrickNames() {
		String[] brickNamesArray = new String[brickTypes.size()];
		for (int i = 0; i < brickNamesArray.length; i++) {
			brickNamesArray[i] = brickTypes.get(i).name;
		}
		
		return brickNamesArray;
	}

	public void setLevelCanvas(LevelCanvas levelCanvas) {
		this.levelCanvas = levelCanvas;
		
	}
	
//	private void updateLevelCanvasImage() {
//		levelCanvas.setImage(currentBrick.image);
//	}
}
