package studiplayer.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

import java.io.File;
import java.net.URL;

public class Player extends Application {

    public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
    private static final String PLAYLIST_DIRECTORY = "playlists";
    private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
    private static final String NO_CURRENT_SONG = " - ";
    private PlayList playList = new PlayList();
    private boolean useCertPlayList = false;

    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private Button nextButton;
    
    private Label playListLabel;
    private Label playTimeLabel;
    private Label currentSongLabel;
    private Button filterButton;
    
    private ChoiceBox<SortCriterion> sortChoiceBox;
    private TextField searchTextField;
    private SongTable songTable = new SongTable(playList);

    private PlayerThread playerThread;
    private TimerThread timerThread;
    
    boolean paused = false;

    public Player() {
        playList = new PlayList();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("APA Player");

        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        TitledPane filterPane = createFilterPane();
        mainPane.setTop(filterPane);

        mainPane.setCenter(songTable);

        VBox bottomPane = createBottomPane();
        mainPane.setBottom(bottomPane);
        
        if (useCertPlayList) {
            loadPlayList(DEFAULT_PLAYLIST);
            playListLabel.setText(DEFAULT_PLAYLIST);
        } else {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                loadPlayList(selectedFile.getPath());
                playListLabel.setText(selectedFile.getPath());
            } else {
            	loadPlayList(null);
            	playListLabel.setText(DEFAULT_PLAYLIST);
            }
        }
        
        Scene scene = new Scene(mainPane, 600, 400);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void setUseCertPlayList(boolean value) {
        this.useCertPlayList = !this.useCertPlayList;
    }
    
    public void setPlayList(String pathname) {
            playList = new PlayList(pathname);
            songTable.refreshSongs(playList);
    }

    public void loadPlayList(String pathname) {
        if (pathname == null || pathname.isEmpty()) {
        	setPlayList(DEFAULT_PLAYLIST);
        } else {
            setPlayList(pathname);
        }
    }

    private TitledPane createFilterPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label sortLabel = new Label("Sort by:");
        sortChoiceBox = new ChoiceBox<>();
        sortChoiceBox.getSelectionModel().selectFirst();

        Label searchLabel = new Label("Search:");
        searchTextField = new TextField();

        filterButton = new Button("Filter");
        filterButton.setOnAction(e -> applyFilterAndSort());

        gridPane.add(sortLabel, 0, 0);
        gridPane.add(sortChoiceBox, 1, 0);
        gridPane.add(searchLabel, 0, 1);
        gridPane.add(searchTextField, 1, 1);
        gridPane.add(filterButton, 2, 1);

        TitledPane titledPane = new TitledPane("Filter", gridPane);
        return titledPane;
    }

    private void applyFilterAndSort() {
        String searchText = searchTextField.getText();
        SortCriterion sortCriterion = sortChoiceBox.getValue();

        playList.setSearch(searchText);

        playList.setSortCriterion(sortCriterion);
        songTable.refreshSongs(playList);
    }

    private VBox createBottomPane() {
        VBox bottomPane = new VBox(10);

        GridPane infoPane = new GridPane();
        infoPane.setHgap(10);
        infoPane.setVgap(10);

        playListLabel = new Label("Playlist:");
        playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
        currentSongLabel = new Label(NO_CURRENT_SONG);

        infoPane.add(new Label("Playlist:"), 0, 0);
        infoPane.add(playListLabel, 1, 0);
        infoPane.add(new Label("Play Time:"), 0, 1);
        infoPane.add(playTimeLabel, 1, 1);
        infoPane.add(new Label("Current Song:"), 0, 2);
        infoPane.add(currentSongLabel, 1, 2);

        HBox controlPane = new HBox(10);
        playButton = createButton("play.jpg");
        pauseButton = createButton("pause.jpg");
        stopButton = createButton("stop.jpg");
        nextButton = createButton("next.jpg");
        setButtonStates(false, true, true, false);
        playButton.setOnAction(e -> playCurrentSong());
        pauseButton.setOnAction(e -> pauseCurrentSong());
        stopButton.setOnAction(e -> stopCurrentSong());
        nextButton.setOnAction(e -> playNextSong());

        controlPane.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);

        bottomPane.getChildren().addAll(infoPane, controlPane);

        return bottomPane;
    }

    private void setButtonStates(boolean playButtonState, boolean pauseButtonState, boolean stopButtonState, boolean nextButtonState) {
    	 playButton.setDisable(playButtonState);
    	 pauseButton.setDisable(pauseButtonState);
    	 stopButton.setDisable(stopButtonState);
    	 nextButton.setDisable(nextButtonState);
    }

    private void updateSongInfo(AudioFile af) {
        Platform.runLater(() -> {
            if (af == null) {
                currentSongLabel.setText(NO_CURRENT_SONG);
                playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
            } else {
                currentSongLabel.setText(af.getTitle());
                playTimeLabel.setText(af.formatPosition());
            }
        });
    }

    private Button createButton(String iconfile) {
        Button button = null;
        try {
            URL url = getClass().getResource("/icons/" + iconfile);
            Image icon = new Image(url.toString());
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            button = new Button("", imageView);
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setStyle("-fx-background-color: #fff;");
        } catch (Exception e) {
            System.out.println("Image " + "icons/" + iconfile + " not found!");
            System.exit(-1);
        }
        return button;
    }


    private void playCurrentSong() {
        if (paused) {
            paused = false;
            startThreads(true);
        } else {
            startThreads(false);
            paused = false;
        }
        songTable.selectSong(playList.currentAudioFile());
        setButtonStates(true, false, false, false);
    }


    private void pauseCurrentSong() {
        if (paused) {
            paused = false;
            startThreads(true);
        } else {
            paused = true;
            terminateThreads(true);
        }
        playList.currentAudioFile().togglePause();
        setButtonStates(true, false, false, false);
    }


    private void stopCurrentSong() {
       terminateThreads(false);
       playList.currentAudioFile().stop();
       setButtonStates(false, true, true, false);
       updateSongInfo(null);
    }


    private void playNextSong() {
    	stopCurrentSong();
        playList.nextSong();
        setButtonStates(true, false, false, false);
        updateSongInfo(playList.currentAudioFile());
        playCurrentSong();
    }

    class PlayerThread extends Thread {
        public boolean stopped = false;

        public void run() {
        	while (!stopped) {
            	if(playList.currentAudioFile() == null) break;
            	
                try {
                    playList.currentAudioFile().play();
                    if (!stopped) {
                        playList.nextSong();
                        updateSongInfo(playList.currentAudioFile());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void terminate() {
            stopped = true;
        }
    }

    class TimerThread extends Thread {
        public boolean stopped = false;

        public void run() {
            while (!stopped) {
            	if(playList.currentAudioFile() == null) break;
            	
                updateSongInfo(playList.currentAudioFile());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void terminate() {
            stopped = true;
        }
    }

    private void startThreads(boolean onlyTimer) {
        if (timerThread == null || !timerThread.isAlive()) {
            timerThread = new TimerThread();
            timerThread.start();
        }

        if (!onlyTimer) {
            if (playerThread == null || !playerThread.isAlive()) {
                playerThread = new PlayerThread();
                playerThread.start();
            }
        }
    }
    
    private void terminateThreads(boolean onlyTimer) {
        if (!onlyTimer) {
            if (playerThread != null) {
                playerThread.terminate();
                playerThread = null;
            }
        }
        if (timerThread != null) {
            timerThread.terminate();
            timerThread = null;
        }
    }
}

