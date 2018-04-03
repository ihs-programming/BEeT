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
		// add padding on the sides
		newx += 64 / 2;
		newy += 48 / 2;
		float scalefactor = getScaleFactor(gc);
		newx = newx * scalefactor;
		newy = newy * scalefactor;
		if ((double) gc.getWidth() / gc.getHeight() >= 640.0 / 480.0) {
			newx += (gc.getWidth() - scalefactor * gc.getHeight()) / 2;
		} else {
			newy += (gc.getHeight() - gc.getWidth() / scalefactor) / 2;
		}
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
	public Vector2f XYtoOsuPixel(GameContainer gc, Vector2f XYpixels) {
		float scalefactor = 1 / getScaleFactor(gc);
		System.out.println(scalefactor + "of y");
		float newx = XYpixels.x;
		float newy = XYpixels.y;
		if ((double) gc.getWidth() / gc.getHeight() >= 640.0 / 480.0) {
			newx -= (gc.getWidth() - scalefactor * gc.getHeight()) / 2;
		} else {
			newy -= (gc.getHeight() - gc.getWidth() / scalefactor) / 2;
		}
		newx = newx * scalefactor;
		newy = newy * scalefactor;
		newx -= 64 / 2;
		newy -= 48 / 2;
		Vector2f convertedvector = new Vector2f(newx, newy);
		return convertedvector;
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
