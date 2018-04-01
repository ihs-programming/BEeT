package utils;

import java.io.File;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;

public class MusicDecoder {
	public static void convertAudioType(String source, String dest)
			throws JavaLayerException {
		Converter c = new Converter();
		c.convert(source, dest);
	}

	public static void convertAllAudioToWav(String sourceDir) throws JavaLayerException {
		File folder = new File(sourceDir);
		if (folder.isFile()) {
			throw new IllegalArgumentException("source directory is not a directory");
		}
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				convertAllAudioToWav(file.getAbsolutePath());
			} else if (getExtension(file.getName()).equals(".mp3")) {
				String newFilename = changeFileExtension(file.getAbsolutePath(), ".wav");
				convertAudioType(file.getAbsolutePath(), newFilename);
			}
		}
	}

	private static String getExtension(String filename) {
		int ind = filename.lastIndexOf(".");
		return filename.substring(ind);
	}

	private static String changeFileExtension(String filename, String newExtension) {
		if (!newExtension.startsWith(".")) {
			throw new IllegalArgumentException("Extension must start with period");
		}
		int extInd = filename.lastIndexOf(".");
		return filename.substring(0, extInd) + newExtension;
	}
}
