package game;

import java.lang.reflect.InvocationTargetException;

import org.newdawn.slick.state.GameState;

/**
 * Contains id information for every state
 */
public enum GameStateTypes {
	/*
	 * Note that the first state listed is the state that the game starts in
	 */
	// @formatter:off
	SONG_SELECT(1, SongSelectState.class),
	RHYTHM_GAME(2, RhythmState.class);
	// @formatter:on

	private int id;
	private Class<? extends DefaultGameState> state;

	private GameStateTypes(int stateID, Class<? extends DefaultGameState> state) {
		this.id = stateID;
		this.state = state;
	}

	public int getID() {
		return this.id;
	}

	public GameState createInstance() {
		try {
			return state.getDeclaredConstructor(int.class).newInstance(id);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
