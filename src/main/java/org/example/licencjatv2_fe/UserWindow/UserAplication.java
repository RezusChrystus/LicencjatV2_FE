package org.example.licencjatv2_fe.UserWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.licencjatv2_fe.EntryWindow.EntryAplication;

import java.net.URL;

public class UserAplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL resource = EntryAplication.class.getResource("/org/example/licencjatv2_fe/user-window-view.fxml");
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
