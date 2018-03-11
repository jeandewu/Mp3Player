package com.jeandewu.mp3player.controller;



import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MenuPaneController implements Initializable {

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private MenuItem fileMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem dirMenuItem;

    @FXML
    private Menu fileMenu;

    public MenuItem getFileMenuItem() {
        return fileMenuItem;
    }

    public MenuItem getDirMenuItem() {
        return dirMenuItem;
    }

    public MenuItem getCloseMenuItem() {
        return closeMenuItem;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        configureMenu();
    }

    private void configureMenu() {
        closeMenuItem.setOnAction(x -> Platform.exit());

        aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Parent parent = null;
                try {
                    parent = FXMLLoader.load(getClass().getResource("/pl/javastart/mp3player/view/AboutPane.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setTitle("Mp3Player v1.0 - about");
                stage.setScene(scene);
                stage.show();
            }
        });
    }
}