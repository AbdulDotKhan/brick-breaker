package com.abdul.brickbreaker.leveleditor;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MenuBar {
	public Menu menu, fileMenu,editMenu; // different menus and submenus
	public Shell parentShell; // the parent shell
	public LevelCanvas levelCanvas; // the canvas on which he level is drawn

	public MenuBar(Shell parentShell, LevelCanvas levelCanvas) {
		this.parentShell = parentShell;
		this.levelCanvas = levelCanvas;
		this.menu  = new Menu(parentShell, SWT.BAR);
		configureItems();
	}
	
	public void configureItems() {
		// file and edit menus
		MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
		fileItem.setText("File");
		MenuItem editItem = new MenuItem(menu, SWT.CASCADE);
		editItem.setText("Edit");

		// file menu
		Menu fileMenu = new Menu(menu);
		fileItem.setMenu(fileMenu);
		MenuItem newItem = new MenuItem(fileMenu, SWT.NONE);
		newItem.setText("New");
		MenuItem openItem = new MenuItem(fileMenu, SWT.NONE);
		openItem.setText("Open...");
		
		// open listener - serializes the levelCanvas' current data to a file
		openItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String fileName = new FileDialog(parentShell).open();
				if (fileName != null) {
					try {
						levelCanvas.openLevel(fileName);
					}
					catch (Exception e) {
						MessageBox errorReadingFileMessageBox = new MessageBox(parentShell);
						errorReadingFileMessageBox.setText("There was an error reading your file type.");
						errorReadingFileMessageBox.setMessage(errorReadingFileMessageBox.getText());
						errorReadingFileMessageBox.open();
					}
				}
				
			}
		});
		
		// save and save as...
		// TODO - make 'save' overwrite changes
		MenuItem saveItem = new MenuItem(fileMenu, SWT.NONE);
		saveItem.setText("Save");
		MenuItem saveAsItem = new MenuItem(fileMenu, SWT.NONE);
		saveAsItem.setText("Save As...");
		
		saveAsItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				String fileName = new FileDialog(parentShell, SWT.SAVE).open();
				try {
					levelCanvas.levelData.save(fileName);
				} catch (IOException e1) {
					// just in case.......
					e1.printStackTrace();
				}
				
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = new FileDialog(parentShell, SWT.SAVE).open();
				try {
					levelCanvas.levelData.save(fileName);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		// some more menu items
		// the only useful one is exit, which extis the program
		new MenuItem(fileMenu, SWT.SEPARATOR);
		MenuItem pageSetupItem = new MenuItem(fileMenu, SWT.NONE);
		pageSetupItem.setText("Page Setup...");
		MenuItem printItem = new MenuItem(fileMenu, SWT.NONE);
		printItem.setText("Print...");
		new MenuItem(fileMenu, SWT.SEPARATOR);
		MenuItem exitItem = new MenuItem(fileMenu, SWT.NONE);
		exitItem.setText("Exit");

		// cut and paste items ... TODO - make them do something when productivity is needed
		Menu editMenu = new Menu(menu);
		editItem.setMenu(editMenu);
		MenuItem cutItem = new MenuItem(editMenu, SWT.NONE);
		cutItem.setText("Cut");
		MenuItem pasteItem = new MenuItem(editMenu, SWT.NONE);
		pasteItem.setText("Paste");

		// for exiting
		exitItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				if (((MenuItem) event.widget).getText().equals("Exit")) {
					parentShell.close();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});

		parentShell.setMenuBar(menu);
	}


}
