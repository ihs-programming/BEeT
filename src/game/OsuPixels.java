package game;

import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author William Ball
 *
 */
public class OsuPixels {

	private final Screen gc;

	public OsuPixels(Screen gc) {
		this.gc = gc;
	}

	/**
	 * Converts a vector containing osupixel coordinates to a vector containing
	 * display coordinates.
	 *
	 * @param gc
	 *            The game container, used to find the dimensions of the game
	 *            window.
	 * @param osupixels
	 *            The vector, in osupixel coordinates, to be converted to display
	 *            coordinates.
	 * @return Returns a vector containing the converted display coordinates of the
	 *         osupixel coordinate vector.
	 */
	public Vector2f osuPixeltoXY(Vector2f osupixels) {
		float newx = osupixels.x;
		float newy = osupixels.y;
		float scalefactor = getScaleFactor();
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
	 * Converts a vector containing display coordinates to a vector containing
	 * osupixel coordinates.
	 *
	 * @param gc
	 *            The game container, used to find the dimensions of the game
	 *            window.
	 * @param XYpixels
	 *            The vector, in display coordinates, to be converted to osupixel
	 *            coordinates.
	 * @return Returns a vector containing the converted osupixel coordinates of the
	 *         display coordinate vector.
	 */
	public Vector2f XYtoOsuPixel(Vector2f convertedvector) {
		float scalefactor = (float) (1.0 / getScaleFactor());
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

	/**
	 * Determines the scale factor to use when converting osupixel coordinates to
	 * display coordinates.
	 *
	 * @param gc
	 *            The game container, used to find the dimensions of the game
	 *            window.
	 * @return If the screen width to screen height ratio is greater than that of a
	 *         640x480px display, returns the float ratio between the screen height
	 *         and the height of a 640x480px display. If the screen width to screen
	 *         height ratio is less than that of a 640x480px display, returns the
	 *         float ratio between the screen width and that of a 640x480px display.
	 *
	 */
	protected float getScaleFactor() {
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
