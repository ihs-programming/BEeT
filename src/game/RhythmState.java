package game;

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

	public final float CIRCLE_TIME = 500f;
	
	private Vector2f curpos = new Vector2f();
	private Vector2f nextCircle = new Vector2f();
	private float circleTime =-1f;
	private Image background;
	private float timescale = .10f;
	private int points = 0;
	private boolean clicked = false;
	private float prevCircleSize = 0;
	private long starttime = System.currentTimeMillis();

	
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
	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
		if (clicked) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.white);
		}
		g.fill(new Circle(curpos.x, curpos.y, prevCircleSize));
		g.setColor(Color.cyan);
		g.setLineWidth(10f);
		g.drawLine(curpos.x, curpos.y, nextCircle.x, nextCircle.y);
		g.setColor(Color.white);
		g.drawString("Points: " + points, gc.getWidth() - 200, 50);
		g.drawString("Points per second " + (1000f * points / (System.currentTimeMillis() - starttime)), gc.getWidth() - 600, 70);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (circleTime < 0) {
			// interval from radius to width - radius
			float maxRadius = CIRCLE_TIME * timescale;
			curpos.set(nextCircle);
			nextCircle = new Vector2f((float) randomRange(maxRadius, gc.getWidth() - maxRadius),
					(float) randomRange(maxRadius, gc.getHeight() - maxRadius));
			circleTime = CIRCLE_TIME * timescale;
			clicked = false;
		}
		else {
			circleTime -= delta * timescale;
		}
		if (!clicked) {
			prevCircleSize = circleTime;
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
			if (new Vector2f(x, y).distance(curpos) < circleTime) {
				clicked = true;
				points ++;
			}
		}
	}

}
