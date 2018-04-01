package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
	public static final String GAME_NAME = "BeET";

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Game());
			app.setTargetFrameRate(144);
			app.setIcons(new String[] { "res/icons/iconsmall.png",
					"res/icons/iconlarge.png" });
			app.start();
		} catch (SlickException e) {

		}
	}
}
