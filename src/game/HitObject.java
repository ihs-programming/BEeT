package game;

public class HitObject {
	int x;
	int y;
	float radius;
	float duration;
	boolean clicked = false;
	
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
