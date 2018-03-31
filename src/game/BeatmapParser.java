package game;

import java.util.LinkedList;

public class BeatmapParser {
	
	public LinkedList<Beat> parseBeatmap() {
	LinkedList<Beat> beatlist = new LinkedList<Beat>();
	beatlist.add(new Beat(100,78,854));
	beatlist.add(new Beat(42,276,3132));
	beatlist.add(new Beat(235,324,3891));
	beatlist.add(new Beat(465,104,5410));
	beatlist.add(new Beat(256,192,5790));
	beatlist.add(new Beat(263,259,9398));
	beatlist.add(new Beat(476,172,10347));
	beatlist.add(new Beat(359,44,11106));
	beatlist.add(new Beat(9,68,12436));
	beatlist.add(new Beat(41,308,14144));
	beatlist.add(new Beat(367,364,15474));
	beatlist.add(new Beat(384,114,16423));
	beatlist.add(new Beat(312,43,16803));
	beatlist.add(new Beat(225,92,17182));
	beatlist.add(new Beat(67,248,18701));
	beatlist.add(new Beat(141,314,19081));
	return beatlist;
	}
}
