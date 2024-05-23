package studiplayer.audio;
import java.io.File;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile{
	
	public WavFile() {
		super();
	}
	
	public WavFile(String path) throws NotPlayableException {
		super(path);
		
		File f = new File(getPathname());
		
		if(f.canRead() == false) {
			throw new NotPlayableException(path, "File not readable!");
		} else {			
			readAndSetDurationFromFile();
		}
	}
	
	public void readAndSetDurationFromFile() throws NotPlayableException {
	    try {           
	        WavParamReader.readParams(getPathname());
	        float frameRate = WavParamReader.getFrameRate();
	        long numberOfFrames = WavParamReader.getNumberOfFrames();
	        duration = computeDuration(numberOfFrames, frameRate);
	        
	        // Remove file extension from the title
	        String titleWithoutExtension = getTitle().replaceFirst("[.][^.]+$", "");
	        title = titleWithoutExtension;
	    } catch (Exception e) {
	        throw new NotPlayableException(getPathname(), e);
	    }
	}


	
	public static long computeDuration(long numberOfFrames, float frameRate) {
		return Math.round((numberOfFrames / frameRate) * 1000000L);
	}
	
	public String toString() {
		String formattedString;
		if(getAuthor().isEmpty()) {
			return formattedString = getTitle();
		} else {
			formattedString = getAuthor() + " - " + getTitle();
		}
		
		formattedString = formattedString + " - " + formatDuration();
		
		return formattedString;
	}

	public static void main(String[] args) {
		
	}

}
