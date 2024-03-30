public abstract class AudioFile {
	private String pathname; // full path of the audio file
	private String filename; // name of the audio file (value after "/")
	protected String author; // author of that audio file
	protected String title; // title of the audio file
	
	// default constructor
	public AudioFile() {}
	
	// parameterized constructor
	public AudioFile(String path) {
		parsePathname(path);
		parseFilename(this.filename);
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
		
		if(filename.equals(" -")) {
			author = "";
			title = "";
		} else if(filename.equals("-")) {
			author = "";
			title = "-";
		} else if(filename.equals("") 
				|| filename.equals(" ") 
				|| filename.equals(" - ")) {
			title = "";
			author = "";
		} else {
			// Find the index of the last dot in the filename
			int lastIndexOfDot = filename.lastIndexOf(".");
			
			// Find the index of the last occurrence of " - " in the filename
			int lastIndexOfHyphen = filename.lastIndexOf(" - ");
			
			if (lastIndexOfHyphen != -1) {
				// If " - " is found, split the filename based on it
				author = filename.substring(0, lastIndexOfHyphen).trim();
				title = filename.substring(lastIndexOfHyphen + 3, lastIndexOfDot).trim();
			} else {
				// If " - " is not found, set author as empty and use the entire string before the last dot as title
				author = "";
				title = filename.substring(0, lastIndexOfDot).trim();
			}
			
			// Check if the title ends with a dot and remove it
			if (title.endsWith(".")) {
				title = title.substring(0, title.length() - 1).trim();
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
	
	// overwriting toString method
	public String toString() {
		if(author.isEmpty()) {
			return title.trim();
		} else {
			return author.trim() + " - " + title.trim();
		}
	}


	public abstract void play();
	
	public abstract void togglePause();
	
	public abstract void stop();
	
	public abstract String formatDuration();
	
	public abstract String formatPosition();
}
