package org.example.licencjatv2_fe.EntryWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class EntryAplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL resource = EntryAplication.class.getResource("/org/example/licencjatv2_fe/entry-view.fxml");
        System.out.println("FXML path: " + resource); // to wypisze null jeśli pliku nie znajdzie
        FXMLLoader fxmlLoader = new FXMLLoader(resource);        Scene scene = new Scene(fxmlLoader.load(), 400 , 300);
        stage.setResizable(false);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}