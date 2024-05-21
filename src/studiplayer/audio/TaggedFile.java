package studiplayer.audio;
import java.io.File;
import java.util.Map;
import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile{
	
	protected String album;
	
	public TaggedFile() {
		super();
	}
	
	public TaggedFile(String path) throws NotPlayableException{
		super(path);
		File f = new File(getPathname());
		
		if(f.canRead() == false) {
			throw new NotPlayableException(getPathname(), "File not readable!");
		}
		
		readAndStoreTags();
	}
	
	
	public void readAndStoreTags() throws NotPlayableException {
	    try {
	    	Map<String, Object> tagMap = TagReader.readTags(getPathname());

	        if (tagMap.containsKey("author")) {
	            author = (String) tagMap.get("author");
	        }

	        if (tagMap.containsKey("title")) {
	            title = (String) tagMap.get("title");
	        }
	        if (tagMap.containsKey("album")) {
	            album = (String) tagMap.get("album");
	        }
	        if (tagMap.containsKey("duration")) {
	            duration = (long) tagMap.get("duration");
	        }
	    } catch (Exception e) {
	        throw new NotPlayableException(this.getPathname(), e);
	    }
	}

	
	public String getAlbum() {
		return album == null ? "" : album.trim();
	}
	
	
	public String toString() {
	    String albumString = (album != null) ? album.trim() : "";
	    System.out.println(albumString + " sep " + super.toString());
	    if(albumString.isEmpty()) {
	        return super.toString() + " - " + formatDuration();
	    } else {
	        return super.toString() + " - " + albumString + " - " + formatDuration();
	    }
	}

}
