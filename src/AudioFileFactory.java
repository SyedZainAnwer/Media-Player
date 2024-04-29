import java.io.File;

public class AudioFileFactory {
    
    public static AudioFile createAudioFile(String path) throws RuntimeException {
        File file = new File(path);
        String filename = file.getName().toLowerCase();

        if (filename.endsWith(".wav")) {
            return new WavFile(path);
        } else if (filename.endsWith(".ogg") || filename.endsWith(".mp3")) {
            return new TaggedFile(path);
        } else {
            throw new RuntimeException("Unknown suffix for AudioFile \"" + path + "\"");
        }
    }
}
