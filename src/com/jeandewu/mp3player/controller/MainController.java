package com.jeandewu.mp3player.controller;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.jeandewu.mp3player.mp3.Mp3Parser;
import com.jeandewu.mp3player.mp3.Mp3Player;
import com.jeandewu.mp3player.mp3.Mp3Song;

public class MainController implements Initializable {

    @FXML
    private ContentPaneController contentPaneController;
    @FXML
    private ControlPaneController controlPaneController;
    @FXML
    private MenuPaneController menuPaneController;

    private Mp3Player mp3Player;
    private Mp3Parser mp3Parser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mp3Player = new Mp3Player();
        mp3Parser = new Mp3Parser();
        configControlPaneAction();
        configureVolume();
        configureTable();
        configureMenu();
    }

    private void configureMenu() {
        MenuItem openFile = menuPaneController.getFileMenuItem();
        MenuItem openDir = menuPaneController.getDirMenuItem();

        openFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(new ExtensionFilter("Mp3", "*.mp3"));
                File file = fc.showOpenDialog(new Stage());
                mp3Player.getMp3Collection().clear();
                mp3Player.getMp3Collection().addSong(mp3Parser.createMp3Song(file));
            }
        });

        openDir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser dc = new DirectoryChooser();
                File dir = dc.showDialog(new Stage());
                mp3Player.getMp3Collection().clear();
                mp3Player.getMp3Collection().addSongs(mp3Parser.createMp3Songs(dir));
            }
        });
    }

    private void configureTable() {
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        contentTable.setItems(mp3Player.getMp3Collection().getSongList());
        contentTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    mp3Player.loadSong(contentTable.getSelectionModel().getSelectedIndex());
                    configureProgressBar();
                    controlPaneController.getPlayButton().setSelected(true);
                }
            }
        });
    }

    private void configureVolume() {
        Slider volSlider = controlPaneController.getVolumeSlider();
        final double minVolume = 0;
        final double maxVolume = 1;
        volSlider.setMin(minVolume);
        volSlider.setMax(maxVolume);
        volSlider.setValue(maxVolume);
        volSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mp3Player.setVolume(newValue.doubleValue());
            }
        });
    }

    private void configControlPaneAction() {
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        ToggleButton playButton = controlPaneController.getPlayButton();
        Button prevButton = controlPaneController.getPrevButton();
        Button nextButton = controlPaneController.getNextButton();

        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (playButton.isSelected()) {
                    mp3Player.play();
                } else {
                    mp3Player.stop();
                }
            }
        });

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() + 1);
                mp3Player.loadSong(contentTable.getSelectionModel().getSelectedIndex());
            }
        });

        prevButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() - 1);
                mp3Player.loadSong(contentTable.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void configureProgressBar() {
        Slider songSlider = controlPaneController.getSongSlider();
        mp3Player.getMediaPlayer().setOnReady(new Runnable() {
            @Override
            public void run() {
                songSlider.setMax(mp3Player.getLoadedSongLength());
            }
        });
        mp3Player.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> arg, Duration oldVal, Duration newVal) {
                songSlider.setValue(newVal.toSeconds());
            }
        });
        songSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(songSlider.isValueChanging()) {
                    mp3Player.getMediaPlayer().seek(Duration.seconds(newValue.doubleValue()));
                }

            }
        });
    }

}