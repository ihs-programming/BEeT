package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

	public Game() {
		super(Main.GAME_NAME);
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		for (GameStateTypes gst : GameStateTypes.values()) {
			addState(gst.createInstance());
		}
	}

}
