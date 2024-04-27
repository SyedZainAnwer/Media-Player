import java.util.LinkedList;
//import java.util.List;

public class PlayList{
	
	private LinkedList<AudioFile> audioFilesList = new LinkedList<>();

	public PlayList(){
		
	}
	
	public PlayList(String m3uPathname) {
		
	}
	
	public void add(AudioFile file) {
		getList().add(file);
	}
	
	public void remove(AudioFile file) {
		getList().remove(file);
	}
	
	public int size() {
		return getList().size();
	}
	
	public AudioFile currentAudioFile() {
		return getList().curr
	}
	
	public LinkedList<AudioFile> getList() {
		return audioFilesList;
	}

}
