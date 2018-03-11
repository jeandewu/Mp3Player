package com.jeandewu.mp3player.controller;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mp3Player = new Mp3Player();
        configControlPaneAction();
        configureVolume();
        configureTable();
        testMp3Add();
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

    private void testMp3Add() {
        mp3Player.getMp3Collection().addSong(createMp3SongFromPath("test.mp3"));
    }

    private Mp3Song createMp3SongFromPath(String filePath) {
        File file = new File(filePath);
        Mp3Song result = new Mp3Song();
        try {
            MP3File mp3File = new MP3File(file);
            result.setFilePath(file.getAbsolutePath());
            result.setTitle(mp3File.getID3v2Tag().getSongTitle());
            result.setAuthor(mp3File.getID3v2Tag().getLeadArtist());
            result.setAlbum(mp3File.getID3v2Tag().getAlbumTitle());
        } catch (IOException | TagException e) {
            e.printStackTrace();
        }
        return result;
    }

}