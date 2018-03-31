package game;

import java.util.LinkedList;

public class BeatmapParser {
	
	public LinkedList<Beat> parseBeatmap() {
	LinkedList<Beat> beatlist = new LinkedList<Beat>();
	beatlist.add(new Beat(250, 250, 1000));
	return beatlist;
	}
}
