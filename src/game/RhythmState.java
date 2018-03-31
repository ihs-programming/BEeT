package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class RhythmState extends DefaultGameState{

	public final int[] CLICK_KEYS = {Input.KEY_Z, Input.KEY_X};
	public final float CIRCLE_TIME = 400f; //time it takes for circles to shrink. the time the circles are on screen is double this, as the circles grow, then shrink.

	private Vector2f curpos = new Vector2f(); //position of the current circle (shrinking circle)
	private Vector2f nextCircle = new Vector2f(); //position of the next circle (growing circle)
	private float prevCircleSize = 0; //current circle size
	private float nextCircleSize = 0; //next circle size

	private float circleTime = -1f; //changing this doesn't do anything. must be negative, however
	private Image background; //background image
	private float timescale = .10f; //rate at which circles shrink. doesn't change the time circles are on screen, however
	private int points = 0; //total number of points
	private boolean clicked = false; //whether or not the current circle has been clicked
	private boolean nextcircleclicked = false; //whether or not the next circle has been clicked

	private float maxRadius = CIRCLE_TIME * timescale; //maximum radius of the circles

	private long starttime = System.currentTimeMillis(); //initial time

	private Input inp; // Get various information about input (e.g. mouse position)

	@Override
	public void enter(GameContainer  gc, StateBasedGame arg1) throws SlickException {
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
	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
		g.setColor(Color.cyan);
		g.setLineWidth(10f);
		g.drawLine(curpos.x, curpos.y, nextCircle.x, nextCircle.y);
		if (nextcircleclicked) {
			g.setColor(Color.green);
		} else {
			g.setColor(Color.cyan);
		}
		g.fill(new Circle(nextCircle.x, nextCircle.y, nextCircleSize));
		if (clicked) {
			g.setColor(Color.green);
		} else {
			g.setColor(Color.cyan);
		}
		g.fill(new Circle(curpos.x, curpos.y, prevCircleSize));
		g.setColor(Color.white);
		g.drawString("Points: " + points, gc.getWidth() - 200, 50);
		g.drawString("Points per second " + 1000f * points / (System.currentTimeMillis() - starttime), gc.getWidth() - 600, 70);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (circleTime < 0) {
			// interval from radius to width - radius
			maxRadius = CIRCLE_TIME * timescale;
			curpos.set(nextCircle);
			nextCircle = new Vector2f((float) randomRange(maxRadius, gc.getWidth() - maxRadius),
					(float) randomRange(maxRadius, gc.getHeight() - maxRadius));
			circleTime = CIRCLE_TIME * timescale;
			clicked = nextcircleclicked;
			nextcircleclicked = false;
		}
		else {
			circleTime -= delta * timescale;
		}
		prevCircleSize = circleTime;
		nextCircleSize = maxRadius - prevCircleSize;

	}

	private double randomRange(float lower, float upper) {
		return Math.random() * (upper - lower) + lower;
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	public void click() {
		int x = inp.getMouseX(), y= inp.getMouseY();
		if (!clicked&&new Vector2f(x, y).distance(curpos) < prevCircleSize) { //checks if current circle has already been clicked, then checks if click is within the circle
			clicked = true;
			points ++;
		} else if (!nextcircleclicked&&new Vector2f(x, y).distance(nextCircle) < nextCircleSize){ //checks if next circle has already been clicked, then checks if click is within the circle
			nextcircleclicked = true;
			points ++;
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
