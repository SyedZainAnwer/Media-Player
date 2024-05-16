package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {

    @Override
    public int compare(AudioFile o1, AudioFile o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        }

        return Long.compare(o1.getDuration(), o2.getDuration());
    }
}
