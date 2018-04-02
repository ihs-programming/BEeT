package game;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
	public static final String GAME_NAME = "BeET";

	public static void main(String[] args) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		int refreshRate = 0;
		for (GraphicsDevice element : gs) {
			DisplayMode dm = element.getDisplayMode();
			if (dm.getRefreshRate() > refreshRate) {
				refreshRate = dm.getRefreshRate();
			}
		}
		try {
			AppGameContainer app = new AppGameContainer(new Game());
			app.setTargetFrameRate(refreshRate);
			app.setIcons(new String[] { "res/icons/iconsmall.png",
					"res/icons/iconlarge.png" });
			app.start();
		} catch (SlickException e) {

		}
	}
}
