package com.abdul.brickbreaker;

import java.io.FileReader;
import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;

import com.abdul.brickbreaker.game.BrickBreaker;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        AssetFileDescriptor descriptor = null;
        
        try {
			initialize(new BrickBreaker(getAssets().open("levels/level.dat")), cfg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}