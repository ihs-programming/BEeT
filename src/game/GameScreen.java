package game;

import org.newdawn.slick.GameContainer;

public class GameScreen implements Screen {
	private GameContainer gc;

	public GameScreen(GameContainer gc) {
		this.gc = gc;
	}

	@Override
	public int getWidth() {
		return gc.getWidth();
	}

	@Override
	public int getHeight() {
		return gc.getHeight();
	}

}
