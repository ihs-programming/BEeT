package game;

import java.util.ArrayList;
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

public class RhythmState extends DefaultGameState{

	public final float CIRCLE_TIME = 600f; //time it takes for circles to shrink. the time the circles are on screen is double this, as the circles grow, then shrink.
	
	private float circleTime = -1f; //changing this doesn't do anything. must be negative, however
	private Image background; //background image
	private float timescale = .10f; //rate at which circles shrink. doesn't change the time circles are on screen, however
	private int points = 0; //total number of points
	private boolean clicked = false; //whether or not the current circle has been clicked
	private boolean nextcircleclicked = false; //whether or not the next circle has been clicked

	private float maxRadius = CIRCLE_TIME * timescale; //maximum radius of the circles
	
	private long starttime = System.currentTimeMillis(); //initial time
	private long songtime = 0; //time since beginning of the song, in ms
	
	private CopyOnWriteArrayList<HitObject> hitobjects = new CopyOnWriteArrayList<>();
	private LinkedList<Beat> beatmap = new LinkedList<>();
	private int beatmapindex = 0;

	@Override
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		
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
		
		BeatmapParser beatmapparser = new BeatmapParser();
		beatmap = beatmapparser.parseBeatmap();
	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
		
		for (HitObject hitobject:hitobjects) {
			if (hitobject.clicked) {
				g.setColor(Color.green);
			} else {
				g.setColor(Color.white);
			}
			g.fill(new Circle(hitobject.x, hitobject.y, hitobject.radius));
		}
		
		g.setColor(Color.white);
		g.drawString("Points: " + points, gc.getWidth() - 200, 50);
		g.drawString("Points per second " + (1000f * points / (System.currentTimeMillis() - starttime)), gc.getWidth() - 600, 70);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		songtime += delta;
		
		if ((beatmapindex<=beatmap.size()-1)&&(beatmap.get(beatmapindex).time<=songtime)) {
			Beat currentbeat = beatmap.get(beatmapindex);
			HitObject hitobject = new HitObject(currentbeat.x, currentbeat.y, maxRadius, CIRCLE_TIME, false);
			hitobjects.add(hitobject);
			beatmapindex++;
		}
		
		if (!hitobjects.isEmpty()) {
			for (HitObject hitobject:hitobjects) {
				int index = hitobjects.indexOf(hitobject);
				hitobjects.set(index, new HitObject(hitobject.x, hitobject.y, maxRadius * (hitobject.duration / CIRCLE_TIME), hitobject.duration - delta, hitobject.clicked));
				if (hitobject.duration<=0) {
					hitobjects.remove(index);
				}
			}
		}
		
	}
	
	private double randomRange(float lower, float upper) {
		return Math.random() * (upper - lower) + lower;
	}
	
	@Override
	public boolean isAcceptingInput() {
		return true;
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == Input.MOUSE_LEFT_BUTTON) {
			for (HitObject hitobject:hitobjects) {
				if ((!hitobject.clicked)&&((new Vector2f(x, y).distance(new Vector2f(hitobject.x, hitobject.y))) < hitobject.radius)) { //checks if current circle has already been clicked, then checks if click is within the circle
					hitobjects.set(hitobjects.indexOf(hitobject), new HitObject(hitobject.x, hitobject.y, hitobject.radius, hitobject.duration, true));
					points++;
					break;
				}
			}
		}
	}

}
