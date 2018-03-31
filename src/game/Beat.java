package game;

public class Beat {
	int x; //x position of the beat circle
	int y; //y position of the beat circle
	int time; //time, in ms, of the beat from the beginning of the song
	//Clip hitSound; to be used later, when sounds are added
	
	public Beat() {
		this.x = 0;
		this.y = 0;
		this.time = 0;
	}
	
	public Beat(int x, int y, int time) {
		this.x = x;
		this.y = y;
		this.time = time;
	}
}
