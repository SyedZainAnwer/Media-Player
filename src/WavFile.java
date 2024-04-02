import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile{
	
	public WavFile() {
		super();
	}
	
	public WavFile(String path) {
		super(path);
		readAndSetDurationFromFile();
	}
	
	public void readAndSetDurationFromFile() {
		WavParamReader.readParams(getPathname());
		float frameRate = WavParamReader.getFrameRate();
		long numberOfFrames = WavParamReader.getNumberOfFrames();
		
		duration = computeDuration(numberOfFrames, frameRate);
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
