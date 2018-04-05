package test;

import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import game.OsuPixels;
import junit.framework.Assert;

public class OsuPixelsTest {

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
	@Test
	public void testOsuPixels() {
		OsuPixels osupixelconverter = new OsuPixels(new MockScreen(1024, 568));
		for (int x = 0; x <= 512; x++) {
			for (int y = 0; y <= 384; y++) {
				Vector2f test = new Vector2f(x, y);
				test = osupixelconverter.osuPixeltoXY(test);
				test = osupixelconverter.XYtoOsuPixel(test);
				if ((int) test.x != x || (int) (test.y + .5) != y) {
					Assert.fail(String.format("calculated: %f %f instead of %d %d",
							test.x, test.y, x, y));
				}
			}
		}
	}

}
