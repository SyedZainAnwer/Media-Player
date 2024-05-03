import java.util.Map;
import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile{
	
	protected String album;
	
	public TaggedFile() {
		super();
	}
	
	public TaggedFile(String path) {
		super(path);
		readAndStoreTags();
	}
	
	
	public void readAndStoreTags() {
	    Map<String, Object> tagMap = TagReader.readTags(getPathname());
	    
	    author = (String) tagMap.getOrDefault("author", author);
	    title = (String) tagMap.getOrDefault("title", title);
	    album = (String) tagMap.getOrDefault("album", album);
	    duration = (long) tagMap.getOrDefault("duration", duration);
	}

	
	public String getAlbum() {
		return album.trim();
	}
	
	
	public String toString() {
	    String albumString = (album != null) ? album.trim() : "";
	    if(albumString.isEmpty()) {
	        return super.toString() + " - " + formatDuration();
	    } else {
	        return super.toString() + " - " + albumString + " - " + formatDuration();
	    }
	}

}
