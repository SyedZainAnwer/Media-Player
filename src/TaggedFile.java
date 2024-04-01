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
		for (String tag : tagMap.keySet()) {
		Object value = tagMap.get(tag);
			switch(tag) {
			case "author":
				author = (String) value;
				break;
			case "title":
				title = (String) value;
				break;
			case "album":
				album = (String) value;
				break;
			case "duration":
				duration = (long) value;
			}
		}
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
