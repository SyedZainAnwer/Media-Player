package studiplayer.audio;
import java.io.File;
import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
	
	protected long duration;
	
	// default constructor
	SampledFile() {
		
	}
	
	// parameterized constructor
	SampledFile(String path) throws NotPlayableException{
		super(path);
		File file = new File(getPathname());
		
		if(!file.canRead()) {
			throw new NotPlayableException(path, "File not readable!");
		}
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void play() throws NotPlayableException {
		try {			
			BasicPlayer.play(getPathname());
		} catch (Exception e) {
			throw new NotPlayableException(getPathname() ,e);
		}
	}
	
	public void stop() {
		BasicPlayer.stop();
	}
	
	public void togglePause() {
		BasicPlayer.togglePause();
	}
	
	public String formatPosition() {
		return timeFormatter(BasicPlayer.getPosition());
	}
	
	public String formatDuration() {
		return timeFormatter(getDuration());
	}
	
	public static String timeFormatter(long timeInMicroSeconds) {
		if(timeInMicroSeconds < 0) {
			throw new RuntimeException("Invalid audio time");
		}
		
		long seconds = timeInMicroSeconds / 1000000; // there are 1000000microsec in a second
		long minutes = seconds / 60;
		long remainingSeconds = seconds % 60;
		
		if(minutes > 99 || remainingSeconds >= 60) {
			throw new RuntimeException("Duration cannot be displayed!");
		}
		
		return String.format("%02d:%02d", minutes, remainingSeconds);
	}
}