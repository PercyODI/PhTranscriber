/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//https://blog.idrsolutions.com/2015/04/javafx-mp3-music-player-embedding-sound-in-your-application/
package phtranscriber;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author pah9qd
 */
public class TranscriberScreenController implements Initializable {
    
    Stage stage;
    Transcription transcription;
    
    MediaPlayer mediaPlayer;
    
    @FXML Text titleText;
    @FXML Slider timeSlider;
    
//    @FXML Button playBtn;
//    @FXML Button pauseBtn;
//    @FXML Button stopBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void ready(Stage stage) {
        this.stage = stage;
    }
    
    private String CallMP3FileChooser() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File file = fc.showOpenDialog(null);
        
        String path = file.getAbsolutePath();
        path = path.replace("\\", "/");
        
        return path;
    }
    
//    private Media CreateMediaPlayer(String path) {
//        Media media = new Media(new File(path).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//    }
    
    private void SelectMP3() {
        String path = CallMP3FileChooser();
        transcription.setAudioPath(path);
    }
    
    private void NewTranscription() {
        transcription = new Transcription();
        SelectMP3();
        OpenTranscription();
    }
    
    private void OpenTranscription() {
        // Set Media Player
        Media media = new Media(new File(transcription.getAudioPath()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        titleText.setText(transcription.getAudioPath());
        
        //Set slider listeners
        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if(timeSlider.isValueChanging()) {
                    mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(timeSlider.getValue() / 100.0));
                    updateMediaPlayerValues();
                }
            }
        });
        
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                updateMediaPlayerValues();
            }
        });
    }
    
    private void updateMediaPlayerValues() {
        Duration currentTime = mediaPlayer.getCurrentTime();
        timeSlider.setValue(currentTime.divide(mediaPlayer.getMedia().getDuration().toMillis()).toMillis() * 100.0);
    }
    
    @FXML
    private void HandleSelectMP3(ActionEvent event) {
        SelectMP3();
    }
    
    @FXML
    private void HandleNewTranscription(ActionEvent event) {
        NewTranscription();
    }
    
    @FXML
    private void HandlePlayBtn(ActionEvent event) {
        mediaPlayer.play();
    }
    
    @FXML
    private void HandlePauseBtn(ActionEvent event) {
        mediaPlayer.pause();
    }
    
    @FXML
    private void HandleStopBtn(ActionEvent event) {
        mediaPlayer.stop();
    }
    
}
