package game;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
	public static final String GAME_NAME = "BeET";

	public static void main(String[] args) {
		GraphicsDevice gs = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		DisplayMode dm = gs.getDisplayMode();
		int refreshRate = dm.getRefreshRate();
		try {
			AppGameContainer app = new AppGameContainer(new Game());
			app.setTargetFrameRate(refreshRate);
			app.setIcons(new String[] { "res/icons/iconsmall.png",
					"res/icons/iconlarge.png" });
			app.setDisplayMode(dm.getWidth() * 3 / 4, dm.getHeight() * 3 / 4, false);
			app.start();
		} catch (SlickException e) {

		}
	}
}
