package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
	public static final String GAME_NAME = "BeET";
	
	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new Game());
			app.setTargetFrameRate(60);
			app.start();
		}
		catch (SlickException e) {
			
		}
	}
}
