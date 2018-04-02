package game;

import org.newdawn.slick.Color;

public class HitObject {
	final int x;
	final int y;
	final float radius;
	final float duration;
	volatile boolean clicked = false;

	final Color color = Color.green;

	public static final float BORDER_WIDTH = 3;

	public HitObject() {
		this.x = 0;
		this.y = 0;
		this.radius = 0;
		this.duration = 0;
	}

	public HitObject(int x, int y, float radius, float duration, boolean clicked) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.duration = duration;
		this.clicked = clicked;
	}
}
