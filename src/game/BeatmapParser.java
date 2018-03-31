package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

import utils.UnzipUtility;

public class BeatmapParser {

	public static final String DEFAULT_BEATMAP_FOLDER = "res/beatmaps/";

	/**
	 * Parses an all of the beatmaps out of an osz file
	 *
	 * @param oszfilename
	 * @return
	 */
	public static LinkedList<Beat>[] parseOsuBeatmaps(String oszfilename) {
		String osufilename = Paths.get(oszfilename).getFileName().toString();
		String beatmapFolder = Paths.get(DEFAULT_BEATMAP_FOLDER, osufilename).toString();
		extractOsuZip(oszfilename, beatmapFolder);
		File[] osufiles = new File(beatmapFolder)
				.listFiles((FilenameFilter) (dir, name) -> name.endsWith(".osu"));
		LinkedList<Beat>[] beatmaps = new LinkedList[osufiles.length];
		for (int i = 0; i < osufiles.length; i++) {
			beatmaps[i] = parseBeatmap(osufiles[i]);
		}
		return beatmaps;
	}

	/**
	 * Takes the file with filepath beatmapfilepath and extracts it to the default
	 * beatmap folder (res/beatmaps/)
	 *
	 * @param beatmapfilepath
	 */
	private static void extractOsuZip(String beatmapfilepath, String beatmapFolder) {
		UnzipUtility beatmapunzipper = new UnzipUtility();
		try {
			beatmapunzipper.unzip(beatmapfilepath, beatmapFolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Takes a beatmap file (.osu) and converts it to a LinkedList comprised of the
	 * beats
	 *
	 * @param osufile
	 * @return
	 */
	private static LinkedList<Beat> parseBeatmap(File osufile) {
		String hitobjectmarker = "[HitObjects]";
		ArrayList<String> osufileparsed = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(osufile));
			String line;
			while ((line = br.readLine()) != null) {
				osufileparsed.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		int beatindex = osufileparsed.indexOf(hitobjectmarker) + 1;
		int osulinecount = osufileparsed.size();

		LinkedList<Beat> beatlist = new LinkedList<>();

		while (beatindex <= osulinecount - 1) {
			String beat[] = osufileparsed.get(beatindex).split(",");
			beatlist.add(new Beat(Integer.valueOf(beat[0]), Integer.valueOf(beat[1]),
					Integer.valueOf(beat[2])));
			beatindex++;
		}

		return beatlist;
	}
}
