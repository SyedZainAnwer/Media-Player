import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class PlayList extends AudioFile{
	
	private LinkedList<AudioFile> audioFilesList = new LinkedList<>();
	private String search;
	private int current = 0;

	public PlayList(){
		
	}
	
	public PlayList(String m3uPathname) {
		loadFromM3U(m3uPathname);
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
		if(current >= 0 && current < audioFilesList.size()) {
			return audioFilesList.get(current);
		} else {
			return null;
		}
	}
	
	
	public void nextSong() {
	    current++;
	    if (current >= audioFilesList.size()) {
	        current = 0;
	    }
	}
	
	
	public void loadFromM3U(String path) {
        getList().clear();
        current = 0;

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.startsWith("#") && !line.isEmpty()) {
                    AudioFile audioFile = AudioFileFactory.createAudioFile(line);
                    getList().add(audioFile);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + path);
        } finally {
            if (scanner != null) {
                scanner.close();
             }
        }
    }
	
	public void saveAsM3U(String pathname) {
		FileWriter writer = null;
		String linesep = System.getProperty("line.separator");

		try {
			writer = new FileWriter(pathname);
			for(AudioFile file : getList()) {
				writer.write(file.getPathname() + linesep);
			}
		}
		catch(Exception e) {
			throw new RuntimeException("Cannot write to file " + pathname + ":" + e.getMessage());
		}
		finally {
			try {
				writer.close();
			}
			catch(Exception e) {
				throw new RuntimeException("Cannot close the file " + pathname + ":" + e.getMessage());
			}
		}
	}
	
	public LinkedList<AudioFile> getList() {
		return audioFilesList;
	}
	
	public int getCurrent() {
		return current;
	}
	
	public void setCurrent(int current) {
		this.current = current;
	}
	
	public void setSearch(String value) {
		this.search = value;
	}

	@Override
	public void play() {
	}

	@Override
	public void togglePause() {
	}

	@Override
	public void stop() {
	}

	@Override
	public String formatDuration() {
		return null;
	}

	@Override
	public String formatPosition() {
		return null;
	}

}
