package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

public class OsuPixels {
	/**
	 * Converts osupixels to display coordinates
	 *
	 * @param gc
	 * @param osupixels
	 * @return
	 */
	protected Vector2f osuPixeltoXY(GameContainer gc, Vector2f osupixels) {
		float newx = osupixels.x;
		float newy = osupixels.y;
		newx += 64;
		newy += 48;
		float scalefactor = getScaleFactor(gc);
		newx = newx * scalefactor;
		newy = newy * scalefactor;
		Vector2f convertedvector = new Vector2f(newx, newy);
		return convertedvector;
	}

	/**
	 * Converts display coordinates to osupixels
	 *
	 * @param gc
	 * @param XYpixels
	 * @return
	 */
	protected Vector2f XYtoOsuPixel(GameContainer gc, Vector2f XYpixels) {
		float scalefactor = getScaleFactor(gc);
		float newx = XYpixels.x;
		float newy = XYpixels.y;
		newx = newx / scalefactor;
		newy = newx / scalefactor;
		newx -= 64;
		newy -= 48;
		Vector2f convertedvector = new Vector2f(newx, newy);
		// System.out.println(newx + " " + newy);
		return convertedvector;
	}

	protected float getScaleFactor(GameContainer gc) {
		int screenheight = gc.getHeight();
		float scalefactor = screenheight / 480f;
		return scalefactor;
	}
}
