package studiplayer.audio;

import java.io.File;

public abstract class AudioFile {
	private String pathname; // full path of the audio file
	private String filename; // name of the audio file (value after "/")
	protected String author; // author of that audio file
	protected String title; // title of the audio file
	
	// default constructor
	public AudioFile() {}
	
	// parameterized constructor
	public AudioFile(String path) throws NotPlayableException {
		parsePathname(path);
		parseFilename(this.filename);
		
		File f = new File(getPathname());
		
		if(f.canRead() == false) {
			throw new NotPlayableException(pathname, "File not readable!");
		}
		
	}
		
	public void parsePathname(String path) {
	    String osName = System.getProperty("os.name").toLowerCase();

	    for (int currIndex = 0; currIndex < path.length(); currIndex++) {
	        int nextIndex = currIndex + 1;
	        char charAtCurrIndex = path.charAt(currIndex);
	        char charAtNextIndex = (nextIndex < path.length()) ? path.charAt(nextIndex) : '\0';

	        if ((charAtCurrIndex == '\\' || charAtCurrIndex == '/') && (charAtNextIndex == '\\' || charAtNextIndex == '/')) {
	            int temp = currIndex;
	            while (temp + 1 < path.length() && (path.charAt(temp + 1) == '\\' || path.charAt(temp + 1) == '/')) {
	                temp++;
	            }
	            String replaceSlash = path.substring(currIndex, temp + 1);
	            if (osName.contains("windows")) {
	                path = path.replace(replaceSlash, "\\");
	            } else {
	                path = path.replace(replaceSlash, "/");
	            }
	        }
	    }

	    if (osName.contains("windows")) {
	        path = path.replaceAll("[/]+", "\\\\");
	    } else {
	        path = path.replaceAll("[\\\\]+", "/");
	    }

	    pathname = path.trim();

	    String[] pathArray = pathname.split("[\\\\/]");
	    String lastIndexValue = pathArray[pathArray.length - 1];
	    if (lastIndexValue.equals(" -")) {
	        lastIndexValue = "-";
	    }
	    filename = lastIndexValue;

	    // Check if the pathname ends with a backslash or forward slash
	    if (pathname.endsWith("\\") || pathname.endsWith("/")) {
	        filename = "";
	    }
	}


	public void parseFilename(String filename) {
	    filename = filename.trim();
	    
	    if (filename.isEmpty() || filename.equals("-")) {
	        author = "";
	        title = "";
	    } else if (filename.equals(" -")) {
	        author = "";
	        title = "-";
	    } else {
	        int lastIndexOfHyphen = filename.lastIndexOf(" - ");
	        
	        if (lastIndexOfHyphen != -1) {
	            author = filename.substring(0, lastIndexOfHyphen).trim();
	            title = filename.substring(lastIndexOfHyphen + 3).trim();
	        } else {
	            author = "";
	            title = filename;
	        }
	        
	        // Check for file extensions and remove them if present
	        int lastIndexOfDot = title.lastIndexOf(".");
	        if (lastIndexOfDot != -1) {
	            title = title.substring(0, lastIndexOfDot).trim();
	        }
	    }
	}



	
	// get methods to access private variable
	public String getPathname() {
		return pathname;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getAuthor() {
		return author.trim();
	}
	
	public String getTitle() {
		return title.trim();
	}
	
	public long getDuration(){
		return 0l;
	}
	
	public String getAlbum() {
		return null;
	}
	
	
	// overwriting toString method
	 @Override
	    public String toString() {
	        String authorStr = (author != null && !author.isEmpty()) ? author.trim() : "";
	        String titleStr = (title != null && !title.isEmpty()) ? title.trim() : "";
	        if (authorStr.isEmpty()) {
	            return titleStr;
	        } else {
	            return authorStr + " - " + titleStr;
	        }
	    }


	public abstract void play() throws NotPlayableException;
	
	public abstract void togglePause();
	
	public abstract void stop();
	
	public abstract String formatDuration();
	
	public abstract String formatPosition();
}
