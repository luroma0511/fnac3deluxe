package com.fnac3.deluxe.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.fnac3.deluxe.core.FNaC3Deluxe;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.useVsync(false);
		config.setWindowedMode(1024, 768);
		config.setResizable(false);
		config.setWindowIcon(Files.FileType.Local, "assets/customnight.png");
		config.setTitle("Five Nights at Candy's 3 Deluxe");
		new Lwjgl3Application(new FNaC3Deluxe(), config);
	}
}
