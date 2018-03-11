package com.jeandewu.mp3player.mp3;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Mp3Collection {
    private ObservableList<Mp3Song> songList;

    public ObservableList<Mp3Song> getSongList() {
        return songList;
    }

    public void addSong(Mp3Song song) {
        songList.add(song);
    }

    public void addSongs(ObservableList<Mp3Song> mp3Songs) {
        songList.addAll(mp3Songs);
    }

    public void clear() {
        songList.clear();
    }

    public Mp3Collection() {
        this.songList = FXCollections.observableArrayList();
    }
}