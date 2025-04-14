package org.example.licencjatv2_fe.UserWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.licencjatv2_fe.Classes.User;
import org.example.licencjatv2_fe.EntryWindow.EntryAplication;

import java.io.IOException;
import java.net.URL;

public class UserAplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL resource = EntryAplication.class.getResource("/org/example/licencjatv2_fe/user-window-view.fxml");
        System.out.println("FXML path: " + resource); // to wypisze null jeśli pliku nie znajdzie
        FXMLLoader fxmlLoader = new FXMLLoader(resource);        Scene scene = new Scene(fxmlLoader.load(), 700 , 700);
        stage.setResizable(false);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    public static void startUserWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(UserAplication.class.getResource("/org/example/licencjatv2_fe/user-window-view.fxml"));
            Parent root = loader.load();

            UserController controller = loader.getController();
            controller.setUser(user); // Przekazujemy użytkownika

            Stage stage = new Stage();
            stage.setTitle("User Dashboard");
            stage.setScene(new Scene(root, 700, 700));
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to start UserWindow");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
