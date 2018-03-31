package game;

import java.util.LinkedList;

public class BeatmapParser {
	
	public LinkedList<Beat> parseBeatmap() {
	LinkedList<Beat> beatlist = new LinkedList<Beat>();
	beatlist.add(new Beat(250, 250, 1000));
	beatlist.add(new Beat(300, 300, 1250));
	beatlist.add(new Beat(150, 350, 1500));
	return beatlist;
	}
}
