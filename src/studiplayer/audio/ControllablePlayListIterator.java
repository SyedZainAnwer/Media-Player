package studiplayer.audio;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
    protected List<AudioFile> audioFiles;
    protected int currentIndex;

    public ControllablePlayListIterator(List<AudioFile> list) {
        this.audioFiles = list;
    }

    public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sortCriterion) {
        currentIndex = 0;
        List<AudioFile> filteredAudioFiles = new LinkedList<>(list);

        if (search != null && !search.isEmpty()) {
            filteredAudioFiles.removeIf(file ->
                    !(file.getAuthor().contains(search) ||
                            file.getTitle().contains(search) ||
                            (file instanceof TaggedFile && ((TaggedFile) file).getAlbum().contains(search))));
        }

        switch (sortCriterion) {
            case AUTHOR:
                filteredAudioFiles.sort(new AuthorComparator());
                break;
            case TITLE:
                filteredAudioFiles.sort(new TitleComparator());
                break;
            case ALBUM:
                filteredAudioFiles.sort(new AlbumComparator());
                break;
            case DURATION:
                filteredAudioFiles.sort(new DurationComparator());
                break;
            default:
                // No sorting needed for DEFAULT
                break;
        }

        audioFiles = filteredAudioFiles;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < audioFiles.size();
    }

    @Override
    public AudioFile next() {
        if (!hasNext()) {
            currentIndex = 0;
        }

        AudioFile nextFile = audioFiles.get(currentIndex);
        currentIndex++;
        
        System.out.println(currentIndex + "curr i");
        return nextFile;
    }

    public AudioFile jumpToAudioFile(AudioFile file) {
        int jumpIndexCount = audioFiles.indexOf(file) - currentIndex;
        if(jumpIndexCount >= 0) {
        	for(int i = 0; i < jumpIndexCount; i++) {
        		next();
        	}
        }
        
        if(jumpIndexCount < 0) {
        	currentIndex = 0;
        	for(int i = 0; i <= audioFiles.indexOf(file); i++) {
        		next();
        	}
        }

        return audioFiles.get(audioFiles.indexOf(file));
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public AudioFile getCurrentAudioFile() {
        if (audioFiles.isEmpty()) {
            return null;
        } else if (currentIndex >= audioFiles.size()) {
            currentIndex = 0;
        }

        return audioFiles.get(currentIndex);
    }
}
