package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile o1, AudioFile o2) {
    	if (o1 == null || o2 == null || o1.getTitle().isEmpty() || o2.getTitle().isEmpty()) {
            return -1;
        }
        
        if (o1.getTitle() == null && o2.getTitle() == null) {
            return 0;
        }
        if (o1.getTitle() == null) {
            return -1;
        }
        if (o2.getTitle() == null) {
            return 1;
        }
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
