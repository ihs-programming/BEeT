package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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

import javazoom.jl.decoder.JavaLayerException;
import utils.MusicDecoder;

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
	private boolean musicstarted = false;

	private CopyOnWriteArrayList<HitObject> hitobjects = new CopyOnWriteArrayList<>();
	private LinkedList<Beat> beatmap = new LinkedList<>();
	private int beatmapindex = 0;

	private Input inp; // Get various information about input (e.g. mouse position)

	private GameContainer gamecontainer;

	public RhythmState(int id) {
		super(id);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame arg1) throws SlickException {
		gamecontainer = gc;
		inp = gc.getInput();
		gc.setMouseCursor("res/cursor.png", 80, 80);

		String beatmapzipfilename = "725875 Sanshuu Chuugaku Yuushabu - Hoshi to Hana.osz";
		String beatmaporigin = "res/sample_osu_beatmaps/";

		beatmap = BeatmapParser.parseOsuBeatmaps(
				Paths.get(beatmaporigin, beatmapzipfilename).toString())[3];
		background = new Image(
				BeatmapParser.DEFAULT_BEATMAP_FOLDER.concat(beatmapzipfilename)
						.concat("/bg.jpg"));
		background = background.getScaledCopy(gc.getWidth(), gc.getHeight());
		try {
			String extractedSongFilename = Paths
					.get(BeatmapParser.DEFAULT_BEATMAP_FOLDER, beatmapzipfilename)
					.toString();
			MusicDecoder.convertAllAudioToWav(extractedSongFilename);
			Clip clip = AudioSystem.getClip();

			// load audio from file into stream
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(
					Paths.get(extractedSongFilename, "audio.wav").toFile());
			clip.open(audioStream);

			// adjust volume
			FloatControl gainControl = (FloatControl) clip
					.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-25f);
			clip.start();
			musicstarted = true;
		} catch (JavaLayerException | LineUnavailableException
				| UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g)
			throws SlickException {
		g.drawImage(background, 0, 0);

		gamecontainer = gc; // updates game container

		// this for loop draws all the hit objects
		OsuPixels osupixelconverter = new OsuPixels();
		float scalefactor = osupixelconverter.getScaleFactor(gc);
		for (int index = 0; index < hitobjects.size(); index++) {
			HitObject hitobject = hitobjects.get(index);
			Vector2f currentcirclepos = osupixelconverter.osuPixeltoXY(gc,
					new Vector2f(hitobject.x, hitobject.y)); // converts from osupixels to
																// real display position
			if (index != hitobjects.size() - 1) { // check to make sure hitobject is not
													// the last in the list
				g.setLineWidth(5f);
				g.setColor(Color.white);

				// draws a line in between consecutive hit objects
				Vector2f nextcirclepos = osupixelconverter.osuPixeltoXY(gc, new Vector2f(
						hitobjects.get(index + 1).x, hitobjects.get(index + 1).y)); // converts
																					// from
																					// osupixels
																					// to
																					// real
																					// display
																					// position
				g.setColor(new Color(255, 255, 255, 120));
				g.draw(new Line(currentcirclepos, nextcirclepos));
			}
			if (hitobject.clicked) { // changes color based on the state of the circle
				g.setColor(Color.transparent);
			} else {
				g.setColor(Color.white);
				g.fill(new Circle(currentcirclepos.x, currentcirclepos.y,
						innerRadius * scalefactor)); // Draw border
				g.setColor(hitobject.color);
			}
			g.setLineWidth(2f);

			// draws approach circle
			g.draw(new Circle(currentcirclepos.x, currentcirclepos.y,

					(hitobject.radius + innerRadius) * scalefactor));
			g.fill(new Circle(currentcirclepos.x, currentcirclepos.y,
					(innerRadius - HitObject.BORDER_WIDTH) * scalefactor)); // draws
			// hit
			// circle
		}

		g.setColor(Color.white);
		g.drawString("Points: " + points, 10, 30);
		g.drawString("Current combo: " + combo, 10, 50);
		g.drawString("Points per second "
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
		if (musicstarted) { // only update if the music has actually started
			songtime += delta;

			percentcompletion = (float) Math
					.floor(10000 * songtime / beatmap.getLast().time)
					/ 100;

			// puts objects in the beatmap into the hitobjects list when needed
			if (beatmapindex <= beatmap.size() - 1
					&& beatmap.get(beatmapindex).time <= CIRCLE_TIME + songtime) {
				Beat currentbeat = beatmap.get(beatmapindex);
				HitObject hitobject = new HitObject(currentbeat.x, currentbeat.y,
						maxRadius,
						CIRCLE_TIME, false);
				hitobjects.add(hitobject);
				beatmapindex++;
			}

			for (HitObject hitobject : hitobjects) {
				int index = hitobjects.indexOf(hitobject);

				// shrinks approach circle and reduces remaining time on screen
				hitobjects.set(index,
						new HitObject(hitobject.x, hitobject.y,
								maxRadius * (hitobject.duration / CIRCLE_TIME),
								hitobject.duration - delta, hitobject.clicked));

				// removes hit circle if time left is less than or equal to the lenience
				// time, or the circle has already been clicked and it's time left is less
				// than zero
				if (hitobject.duration <= -LENIENCE_TIME
						|| hitobject.clicked && hitobject.duration <= 0) {
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
		OsuPixels osupixelstoxy = new OsuPixels();
		float scalefactor = osupixelstoxy.getScaleFactor(gamecontainer);
		for (HitObject hitobject : hitobjects) {
			// checks if current circle has already been clicked, then checks if click is
			// within the circle
			if (!hitobject.clicked && new Vector2f(x, y)
					.distance(osupixelstoxy.osuPixeltoXY(gamecontainer,
							new Vector2f(hitobject.x, hitobject.y))) < innerRadius
									* scalefactor) {
				// changes hitobject click state
				hitobjects.set(hitobjects.indexOf(hitobject), new HitObject(hitobject.x,
						hitobject.y, hitobject.radius, hitobject.duration, true));
				points++; // increments points
				combo++; // increases combo
				break; // breaks out of loop so that only one hit object is clicked at
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
