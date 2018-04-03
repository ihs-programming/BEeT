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
	public Vector2f osuPixeltoXY(GameContainer gc, Vector2f osupixels) {
		float newx = osupixels.x;
		float newy = osupixels.y;
		float scalefactor = getScaleFactor(gc);
		// add padding on the sides
		newx += 64.0;
		newy += 48.0;
		Vector2f convertedvector = new Vector2f(newx * scalefactor, newy * scalefactor);
		if ((double) gc.getWidth() / gc.getHeight() >= 640.0 / 480.0) {
			convertedvector = new Vector2f(
					convertedvector.x + (gc.getWidth() - 640 * scalefactor) / 2,
					convertedvector.y);
		} else {
			convertedvector = new Vector2f(
					convertedvector.x,
					convertedvector.y + (gc.getHeight() - 480 * scalefactor) / 2);
		}
		return convertedvector;
	}

	/**
	 * Converts display coordinates to osupixels
	 *
	 * @param gc
	 * @param XYpixels
	 * @return
	 */
	public Vector2f XYtoOsuPixel(GameContainer gc, Vector2f convertedvector) {
		float scalefactor = (float) (1.0 / getScaleFactor(gc));
		if ((double) gc.getWidth() / gc.getHeight() >= 640.0 / 480.0) {
			convertedvector = new Vector2f(
					convertedvector.x - (gc.getWidth() - 640 / scalefactor) / 2,
					convertedvector.y);
		} else {
			convertedvector = new Vector2f(
					convertedvector.x,
					convertedvector.y - (gc.getHeight() - 480 / scalefactor) / 2);
		}
		float newx = convertedvector.x * scalefactor;
		float newy = convertedvector.y * scalefactor;

		// add padding on the sides
		newx -= 64.0;
		newy -= 48.0;
		Vector2f vector = new Vector2f(newx, newy);
		return vector;
	}

	protected float getScaleFactor(GameContainer gc) {
		int screenheight = gc.getHeight();
		int screenwidth = gc.getWidth();
		float scalefactor;
		if ((double) screenwidth / screenheight >= 640.0 / 480.0) {
			scalefactor = screenheight / 480f;
		} else {
			scalefactor = screenwidth / 640f;
		}
		return scalefactor;
	}
}
