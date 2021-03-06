package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
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
	private int overlayopacity = 120; // opacity of the overlay which covers the
										// background image

	private float timescale = .08f; // rate at which approach circles shrink. doesn't
									// change the time circles are on screen, however
	private int points = 0; // total number of points
	private int hits = 0; // total number of completed circles
	private float perfection = 0; // perfection points
	private int combo = 0; // current combo
	private int hitobjectscompleted = 0; // number of passed hitobjects, includes both hit
											// and missed
	private float hitpercent = 0; // percentage of hitobjects hit
	private float perfectionpercent = 0; // perfection point percentage
	private float percentcompletion = 0; // percent of the song completed

	private float maxRadius = CIRCLE_TIME * timescale; // maximum radius of the approach
														// circles
	private float innerRadius = 20f; // radius of the inner hit circles which you click

	private Clip clip;
	private long starttime = System.currentTimeMillis(); // initial time
	private long songtime = 0; // time since beginning of the song, in ms

	private HashSet<Particle> particles = new HashSet<>();
	private CopyOnWriteArrayList<HitObject> hitobjects = new CopyOnWriteArrayList<>();
	private LinkedList<Beat> beatmap = new LinkedList<>();
	private int beatmapindex = 0;

	private Input inp; // Get various information about input (e.g. mouse position)
	private Image cursor;
	private Vector2f cursorcenter;

	private GameContainer gamecontainer;

	public RhythmState(int id) {
		super(id);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame arg1) throws SlickException {
		gamecontainer = gc;
		inp = gc.getInput();

		cursor = new Image("res/cursor.png");
		cursorcenter = new Vector2f(cursor.getWidth() / 2, cursor.getHeight() / 2);
		Cursor emptyCursor;
		try {
			emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
			gc.setMouseCursor(emptyCursor, 0, 0);
		} catch (LWJGLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
			clip = AudioSystem.getClip();
			// load audio from file into stream
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(
					Paths.get(extractedSongFilename, "audio.wav").toFile());
			clip.open(audioStream);

			// adjust volume
			FloatControl gainControl = (FloatControl) clip
					.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-25f);
			clip.start();
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

		// draw a transparent black rectangle over the background image
		g.setColor(new Color(0, 0, 0, overlayopacity));
		g.fill(new Rectangle(0, 0, gc.getWidth(), gc.getHeight()));

		// this for loop draws all the particles
		OsuPixels osupixelconverter = new OsuPixels(new GameScreen(gc));
		for (Particle particle : particles) {
			Vector2f center = osupixelconverter.osuPixeltoXY(
					new Vector2f(particle.x, particle.y));

			g.setColor(particle.getColor());
			g.fill(new Circle(center.x, center.y, particle.getRadius()));
		}

		// this for loop draws all the hit objects
		float scalefactor = osupixelconverter.getScaleFactor();
		for (int index = 0; index < hitobjects.size(); index++) {
			HitObject hitobject = hitobjects.get(index);
			Vector2f currentcirclepos = osupixelconverter.osuPixeltoXY(
					new Vector2f(hitobject.x, hitobject.y)); // converts from osupixels to
																// real display position
			if (index != hitobjects.size() - 1) { // check to make sure hitobject is not
													// the last in the list
				g.setLineWidth(5f);
				g.setColor(Color.white);

				// draws a line in between consecutive hit objects
				Vector2f nextcirclepos = osupixelconverter.osuPixeltoXY(new Vector2f(
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
		g.drawString("Perfection: " + perfectionpercent + "%",
				10, 110);

		g.drawImage(cursor, inp.getMouseX() - cursorcenter.x,
				inp.getMouseY() - cursorcenter.y);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		songtime = clip.getMicrosecondPosition() / 1000;

		percentcompletion = (float) Math
				.floor(10000 * songtime / beatmap.getLast().time)
				/ 100;

		// puts objects in the beatmap into the hitobjects list when needed
		if (beatmapindex <= beatmap.size() - 1
				&& beatmap.get(beatmapindex).time <= CIRCLE_TIME + songtime) {
			Beat currentbeat = beatmap.get(beatmapindex);
			HitObject hitobject = new HitObject(currentbeat.x, currentbeat.y,
					maxRadius, beatmap.get(beatmapindex).time - songtime, false);
			hitobjects.add(hitobject);
			beatmapindex++;
		}

		HashSet<Particle> toDelete = new HashSet<>();
		for (Particle particle : particles) {
			particle.update(delta);
			if (particle.isDead()) {
				toDelete.add(particle);
			}
		}
		for (Particle p : toDelete) {
			particles.remove(p);
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
				particles.add(new Particle(hitobject.x, hitobject.y, hitobject.radius,
						hitobject.duration, hitobject.clicked));
				hitobjects.remove(index);
				hitobjectscompleted++;
				hitpercent = (float) (Math.floor(10000 * hits / hitobjectscompleted)
						/ 100);
				perfectionpercent = (float) (Math
						.floor(10000 * perfection / hitobjectscompleted)
						/ 100);
			}
		}
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	public void click() {
		int x = inp.getMouseX(), y = inp.getMouseY();
		OsuPixels osupixelstoxy = new OsuPixels(new GameScreen(gamecontainer));
		float scalefactor = osupixelstoxy.getScaleFactor();
		for (HitObject hitobject : hitobjects) {
			// checks if current circle has already been clicked, then checks if click is
			// within the circle
			if (!hitobject.clicked && new Vector2f(x, y)
					.distance(osupixelstoxy.osuPixeltoXY(
							new Vector2f(hitobject.x, hitobject.y))) < innerRadius
									* scalefactor) {
				// changes hitobject click state
				hitobjects.set(hitobjects.indexOf(hitobject), new HitObject(hitobject.x,
						hitobject.y, hitobject.radius, hitobject.duration, true));
				if (Math.abs(hitobject.duration) <= LENIENCE_TIME) {
					// Formula: Base score of a hit + Base score of a hit
					// * (Combo multiplier * Difficulty multiplier * Mod multiplier) / 25
					points += 50 + 50 * (combo * 0.5 * 1) / 25; // increments points
					hits++;
					perfection += 1.0 - Math.abs(hitobject.duration) / LENIENCE_TIME;
					combo++; // increases combo
				}
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
