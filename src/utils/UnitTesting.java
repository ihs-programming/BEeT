package utils;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import game.OsuPixels;

/**
 *
 * @author William Ball
 *
 */
public class UnitTesting {
	/**
	 * Tests if OsuPixels returns the right vectors.
	 * <p>
	 * Goes through all possible osupixel coordinate values, and converts them to
	 * display coordinates and back.
	 *
	 * @param gc
	 *            The game container, used for osupixel conversion.
	 * @return Returns whether or not the test was successful.
	 */
	public boolean testOsuPixels(GameContainer gc) {
		OsuPixels osupixelconverter = new OsuPixels();
		for (int x = 0; x <= 512; x++) {
			for (int y = 0; y <= 384; y++) {
				Vector2f test = new Vector2f(x, y);
				test = osupixelconverter.osuPixeltoXY(gc, test);
				test = osupixelconverter.XYtoOsuPixel(gc, test);
				if ((int) test.x != x || (int) test.y != y) {
					return false;
				}
			}
		}
		return true;
	}
}
