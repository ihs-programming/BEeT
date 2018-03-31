package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import utils.UnzipUtility;

public class BeatmapParser {
	/**
	 * Takes the file named beatmapfilename in folder beatmaporigin
	 * and extracts it to a folder named beatmapfilename in beatmappath
	 * @param beatmapfilename
	 * @param beatmaporigin
	 * @param beatmappath
	 */
	public void extractOsuZip(String beatmapfilename, String beatmaporigin, String beatmappath) { 
		beatmaporigin = beatmaporigin.concat(beatmapfilename);		
		beatmappath = beatmappath.concat(beatmapfilename);
		UnzipUtility beatmapunzipper = new UnzipUtility();
		try {
			beatmapunzipper.unzip(beatmaporigin, beatmappath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes the file named beatmapfilename in folder beatmaporigin
	 * and extracts it to the default beatmap folder (res/beatmaps/)
	 * @param beatmapfilename
	 * @param beatmaporigin
	 */
	public void extractOsuZip(String beatmapfilename, String beatmaporigin) {
		beatmaporigin = beatmaporigin.concat(beatmapfilename);
		String beatmappath = "res/beatmaps/";
		UnzipUtility beatmapunzipper = new UnzipUtility();
		try {
			beatmapunzipper.unzip(beatmaporigin, beatmappath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes the file with filepath beatmapfilepath
	 * and extracts it to the default beatmap folder (res/beatmaps/)
	 * @param beatmapfilepath
	 */
	public void extractOsuZip(String beatmapfilepath) {
		String beatmappath = "res/beatmaps/";
		UnzipUtility beatmapunzipper = new UnzipUtility();
		try {
			beatmapunzipper.unzip(beatmapfilepath, beatmappath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes a beatmap file (.osu) and converts it to a LinkedList comprised of the beats
	 * @param osufile
	 * @return
	 */
	public LinkedList<Beat> parseBeatmap(File osufile) {
		String hitobjectmarker = "[HitObjects]";
		ArrayList<String> osufileparsed = new ArrayList<>();
		BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(osufile));
            String line;
            while ((line = br.readLine()) != null) {
                osufileparsed.add(line);
                System.out.println(line);
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
        
		LinkedList<Beat> beatlist = new LinkedList<Beat>();
		
		while (beatindex <= osulinecount - 1) {
			String beat[] = osufileparsed.get(beatindex).split(",");
			beatlist.add(new Beat(Integer.valueOf(beat[0]), Integer.valueOf(beat[1]), Integer.valueOf(beat[2])));
			beatindex++;
		}
		
		return beatlist;
	}
}
