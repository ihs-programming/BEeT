package game;

import org.newdawn.slick.Color;

/**
 * We assume that all particles can be rendered as a circle + color.
 *
 * @author s-chenrob
 *
 */
public class Particle extends HitObject {
	protected int tick;

	public Particle(int x, int y, float radius, float duration, boolean clicked) {
		super(x, y, radius, duration, clicked);

		tick = 0;
	}

	public void update(int delta) {
		tick += delta / 2;
	}

	public int getRadius() {
		return tick / 2;
	}

	public Color getColor() {
		return new Color(0, 255, 0, 155 - tick);
	}

	/**
	 * Return true if this particle should be disposed.
	 *
	 * @return
	 */
	public boolean isDead() {
		return tick > 150;
	}
}
