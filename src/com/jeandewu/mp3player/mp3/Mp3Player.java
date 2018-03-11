package com.jeandewu.mp3player.mp3;



import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class Mp3Player {
    private Mp3Collection mp3Collection;

    private Media media;
    private MediaPlayer mediaPlayer;

    public Mp3Collection getMp3Collection() {
        return mp3Collection;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Mp3Player() {
        this.mp3Collection = new Mp3Collection();
    }

    public void play() {
        if(mediaPlayer != null && (mediaPlayer.getStatus() == Status.READY || mediaPlayer.getStatus() == Status.PAUSED)) {
            mediaPlayer.play();
        }
    }

    public void stop() {
        if(mediaPlayer != null && mediaPlayer.getStatus() == Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    public double getLoadedSongLength() {
        if(media != null) {
            return media.getDuration().toSeconds();
        } else {
            return 0;
        }
    }

    public void setVolume(double volume) {
        if(mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public void loadSong(int index) {
        if(mediaPlayer != null && mediaPlayer.getStatus() == Status.PLAYING) {
            mediaPlayer.stop();
        }
        Mp3Song mp3s = mp3Collection.getSongList().get(index);
        media = new Media(new File(mp3s.getFilePath()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
    }
}