package com.jeandewu.mp3player.mp3;

import java.io.File;
import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Mp3Parser {
    public Mp3Song createMp3Song(File file) {
        Mp3Song result = new Mp3Song();
        MP3File mp3File = null;
        try {
            mp3File = new MP3File(file);
        } catch (IOException | TagException e) {
            e.printStackTrace();
        }
        result.setFilePath(file.getAbsolutePath());
        result.setTitle(mp3File.getID3v2Tag().getSongTitle());
        result.setAuthor(mp3File.getID3v2Tag().getLeadArtist());
        result.setAlbum(mp3File.getID3v2Tag().getAlbumTitle());

        return result;
    }

    public ObservableList<Mp3Song> createMp3Songs(File dir) {
        if(!dir.isDirectory()) {
            return null;
        }

        ObservableList<Mp3Song> result = FXCollections.observableArrayList();
        File[] files = dir.listFiles();
        for(File f: files) {
            String fileExtension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
            if(fileExtension.equals("mp3"))
                result.add(createMp3Song(f));
        }

        return result;
    }
}