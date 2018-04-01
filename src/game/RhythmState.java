package game;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class RhythmState extends DefaultGameState {

	public final int[] CLICK_KEYS = { Input.KEY_Z, Input.KEY_X };
	public final float CIRCLE_TIME = 1000f; // time it takes for approach circles to
											// shrink.
	public final float LENIENCE_TIME = 200f; // lenience, in ms, given to the player to
												// click the hit object

	private Image background; // background image
	private float timescale = .08f; // rate at which approach circles shrink. doesn't
									// change the time circles are on screen, however
	private int points = 0; // total number of points
	private int combo = 0; // current combo
	private int hitobjectscompleted = 0; // number of passed hitobjects, includes both hit
											// and missed
	private float hitpercent = 0; // percentage of hitobjects hit
	private float percentcompletion = 0; // percent of the song completed

	private float maxRadius = CIRCLE_TIME * timescale; // maximum radius of the approach
														// circles
	private float innerRadius = 20f; // radius of the inner hit circles which you click

	private long starttime = System.currentTimeMillis(); // initial time
	private long songtime = 0; // time since beginning of the song, in ms

	private CopyOnWriteArrayList<HitObject> hitobjects = new CopyOnWriteArrayList<>();
	private LinkedList<Beat> beatmap = new LinkedList<>();
	private int beatmapindex = 0;

	private Input inp; // Get various information about input (e.g. mouse position)

	@Override
	public void enter(GameContainer gc, StateBasedGame arg1) throws SlickException {
		inp = gc.getInput();
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
		background = new Image("res/background.jpg");
		background = background.getScaledCopy(gc.getWidth(), gc.getHeight());

		String beatmapzipfilename = "725875 Sanshuu Chuugaku Yuushabu - Hoshi to Hana.osz";
		String beatmaporigin = "res/sample_osu_beatmaps/";
		beatmap = BeatmapParser.parseOsuBeatmaps(
				Paths.get(beatmaporigin, beatmapzipfilename).toString())[3];
	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g)
			throws SlickException {
		g.drawImage(background, 0, 0);

		for (HitObject hitobject : hitobjects) { // this for loop draws all the hit
													// objects
			int index = hitobjects.indexOf(hitobject);
			if (index != hitobjects.size() - 1) { // check to make sure hitobject is not
													// the last in the list
				g.setLineWidth(5f);
				g.setColor(Color.white);
				g.draw(new Line(new Vector2f(hitobject.x, hitobject.y), new Vector2f(
						hitobjects.get(index + 1).x, hitobjects.get(index + 1).y))); // draws
																						// a
																						// line
																						// in
																						// between
																						// consecutive
																						// hit
																						// objects
			}
			if (hitobject.clicked) { // changes color based on the state of the circle
				g.setColor(Color.green);
			} else {
				g.setColor(Color.white);
			}
			g.setLineWidth(2f);
			g.draw(new Circle(hitobject.x, hitobject.y, hitobject.radius + innerRadius)); // draws
																							// approach
																							// circle
			g.fill(new Circle(hitobject.x, hitobject.y, innerRadius)); // draws hit circle
		}

		g.setColor(Color.white);
		g.drawString("Points: " + points, 10, 30);
		g.drawString("Current combo: " + combo,
				10, 50);
		g.drawString(
				"Points per second "
						+ 1000f * points / (System.currentTimeMillis() - starttime),
				10, 70);
		g.drawString("Percentage complete: " + percentcompletion + "%",
				gc.getWidth() - 250, 30);
		g.drawString("Hit rate: " + hitpercent + "%",
				10, 90);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		songtime += delta;

		percentcompletion = (float) Math.floor(10000 * songtime / beatmap.getLast().time)
				/ 100;

		if (beatmapindex <= beatmap.size() - 1
				&& beatmap.get(beatmapindex).time <= CIRCLE_TIME + songtime) { // puts
																				// objects
																				// in the
																				// beatmap
																				// into
																				// the
																				// hitobjects
																				// list
																				// when
																				// needed
			Beat currentbeat = beatmap.get(beatmapindex);
			HitObject hitobject = new HitObject(currentbeat.x, currentbeat.y, maxRadius,
					CIRCLE_TIME, false);
			hitobjects.add(hitobject);
			beatmapindex++;
		}

		if (!hitobjects.isEmpty()) {
			for (HitObject hitobject : hitobjects) {
				int index = hitobjects.indexOf(hitobject);
				hitobjects.set(index,
						new HitObject(hitobject.x, hitobject.y,
								maxRadius * (hitobject.duration / CIRCLE_TIME),
								hitobject.duration - delta, hitobject.clicked)); // shrinks
																					// approach
																					// circle
																					// and
																					// reduces
																					// remaining
																					// time
																					// on
																					// screen
				if (hitobject.duration <= -LENIENCE_TIME
						|| hitobject.clicked && hitobject.duration <= 0) { // removes hit
																			// circle if
																			// time left
																			// is less
																			// than or
																			// equal to
																			// the
																			// lenience
																			// time, or
																			// the circle
																			// has already
																			// been
																			// clicked and
																			// it's time
																			// left is
																			// less than
																			// zero
					if (!hitobject.clicked) {
						combo = 0;
					}
					hitobjects.remove(index);
					hitobjectscompleted++;
					hitpercent = (float) (Math.floor(10000 * points / hitobjectscompleted)
							/ 100);
				}
			}
		}

	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	public void click() {
		int x = inp.getMouseX(), y = inp.getMouseY();
		for (HitObject hitobject : hitobjects) {
			if (!hitobject.clicked && new Vector2f(x, y)
					.distance(new Vector2f(hitobject.x, hitobject.y)) < innerRadius) { // checks
																						// if
																						// current
																						// circle
																						// has
																						// already
																						// been
																						// clicked,
																						// then
																						// checks
																						// if
																						// click
																						// is
																						// within
																						// the
																						// circle
				hitobjects.set(hitobjects.indexOf(hitobject), new HitObject(hitobject.x,
						hitobject.y, hitobject.radius, hitobject.duration, true)); // changes
																					// hitobject
																					// click
																					// state
				points++; // increments points
				combo++; // increases combo
				break; // breaks out of for loop so that only one hit object is clicked at
						// once
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == Input.MOUSE_LEFT_BUTTON) {
			click();
		}
	}

	@Override
	public void keyPressed(int keycode, char c) {
		for (int element : CLICK_KEYS) {
			if (keycode == element) {
				click();
				break;
			}
		}
	}
}
