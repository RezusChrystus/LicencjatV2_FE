package org.example.licencjatv2_fe.UserWindow;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import org.example.licencjatv2_fe.Api.ApiClient;
import org.example.licencjatv2_fe.Classes.User;
import org.example.licencjatv2_fe.Classes.Workspace;

import java.io.IOException;

public class UserController {

    @FXML
    private TilePane workspaceTilePane;

    @FXML
    private Button addWorkspaceButton;

    @FXML
    private TextField workspaceTagTextField;

    private User user;

    public void setUser(User user) {
        this.user = user;

        // Wypełnianie siatki nazwami workspaces
        for (Workspace workspace : user.getWorkspaceList()) {
            Button workspaceButton = new Button(workspace.getName());
            workspaceButton.setMinWidth(100);
            workspaceButton.setMinHeight(60);
            // można dodać tutaj akcję po kliknięciu
            workspaceTilePane.getChildren().add(workspaceButton);
        }
    }

    @FXML
    private void onAddWorkspaceButtonClick() {
        String tag = workspaceTagTextField.getText().trim();

        if (tag.isEmpty()) {
            showAlert("Error", "Tag field cannot be empty.", Alert.AlertType.ERROR);
            return;
        }

        boolean alreadyExists = user.getWorkspaceList().stream()
                .anyMatch(w -> w.getTag().equalsIgnoreCase(tag));

        if (alreadyExists) {
            showAlert("Info", "Workspace already added.", Alert.AlertType.INFORMATION);
            return;
        }

        // Operacja sieciowa w osobnym wątku
        new Thread(() -> {
            try {
                Workspace newWorkspace = ApiClient.addWorkspaceToUser(user.getLogin(), user.getPassword(), tag);

                if (newWorkspace != null) {
                    // Dodajemy nowy workspace do listy
                    user.getWorkspaceList().add(newWorkspace);

                    Platform.runLater(() -> {
                        // Tworzymy kafelek z nazwą workspace
                        Button workspaceButton = new Button(newWorkspace.getName());  // Używamy name z obiektu Workspace
                        workspaceButton.setMinWidth(100);
                        workspaceButton.setMinHeight(60);
                        workspaceTilePane.getChildren().add(workspaceButton);
                        showAlert("Succes", "Workspace added succesfuly!", Alert.AlertType.INFORMATION);
                        workspaceTagTextField.clear();
                    });

                } else {
                    Platform.runLater(() -> showAlert("Failure", "Workspace not found.", Alert.AlertType.ERROR));
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Error", "Could not connect with server.", Alert.AlertType.ERROR));
            }
        }).start();
    }



    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
