package com.mygdx.drop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.drop.Drop;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//added config stuff
        config.title = "Drop";
        config.width = 800;
        config.height = 480;
        new LwjglApplication(new Drop(), config);
	}
}
