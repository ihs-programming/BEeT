package utils;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import game.OsuPixels;

public class UnitTesting {
	/**
	 * Using the GameContainer, tests if OsuPixels returns the right vectors.
	 * 
	 * @param gc
	 * @return
	 */
	public boolean testOsuPixels(GameContainer gc) {
		OsuPixels osupixelconverter = new OsuPixels();
		for (int i = 0; i < 100; i++) {
			int x = (int) (Math.random() * 512);
			int y = (int) (Math.random() * 384);
			Vector2f test = new Vector2f(x, y);
			test = osupixelconverter.osuPixeltoXY(gc, test);
			test = osupixelconverter.XYtoOsuPixel(gc, test);
			if ((int) test.x != x || (int) test.y != y) {
				return false;
			}
		}
		return true;
	}
}
