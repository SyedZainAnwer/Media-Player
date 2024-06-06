package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Iterator;

public class PlayList implements Iterable<AudioFile> {
    private LinkedList<AudioFile> audioFilesList = new LinkedList<>();
    private String search;
    private SortCriterion sortCriterion = SortCriterion.DEFAULT;
    private ControllablePlayListIterator iterator;

    public PlayList() {
        updateIterator();
    }

    public PlayList(String m3uPathname) {
        this();
        try {
            loadFromM3U(m3uPathname);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void add(AudioFile file) {
        audioFilesList.add(file);
        updateIterator();
    }

    public void remove(AudioFile file) {
        audioFilesList.remove(file);
        updateIterator();
    }

    public int size() {
        return audioFilesList.size();
    }

    public AudioFile currentAudioFile() {
        return iterator.getCurrentAudioFile();
    }

    public void nextSong() {
        if (iterator == null || !iterator.hasNext()) {
            updateIterator();
        } else {
            iterator.next();
        }
    }

    private void updateIterator() {
        if (sortCriterion == null) {
            sortCriterion = SortCriterion.DEFAULT;
        }
        iterator = new ControllablePlayListIterator(audioFilesList, search, sortCriterion);
    }

    public void loadFromM3U(String path) throws NotPlayableException {
        audioFilesList.clear();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.startsWith("#") && !line.isEmpty()) {
                    try {
                        AudioFile audioFile = AudioFileFactory.createAudioFile(line);
                        audioFilesList.add(audioFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            throw new NotPlayableException("Error reading file: " + path, e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        updateIterator();
    }

    public void saveAsM3U(String pathname) {
        try (FileWriter writer = new FileWriter(pathname)) {
            String linesep = System.getProperty("line.separator");
            for (AudioFile file : audioFilesList) {
                writer.write(file.getPathname() + linesep);
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot write to file " + pathname + ":" + e.getMessage());
        }
    }

    public LinkedList<AudioFile> getList() {
        return new LinkedList<>(audioFilesList);
    }

    @Override
    public Iterator<AudioFile> iterator() {
        return new ControllablePlayListIterator(audioFilesList, search, sortCriterion);
    }

    public SortCriterion getSortCriterion() {
        return sortCriterion;
    }

    public void setSortCriterion(SortCriterion sortCriterion) {
        this.sortCriterion = sortCriterion;
        updateIterator();
    }

    public void setSearch(String value) {
        this.search = value;
        updateIterator();
    }

    public String getSearch() {
        return search;
    }

    public AudioFile jumpToAudioFile(AudioFile file) {
        int index = audioFilesList.indexOf(file);
        if (index != -1) {
            iterator.jumpToAudioFile(file);
            return file;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.audioFilesList.toString();
    }
}
