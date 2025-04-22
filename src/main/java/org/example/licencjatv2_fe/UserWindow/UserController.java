package org.example.licencjatv2_fe.UserWindow;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
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
    @FXML
    private TextField workspaceNameTextField;
    @FXML
    private Button createWorkspaceButton;
    @FXML
    private Button logoutButton;

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
    private void onLogoutButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/licencjatv2_fe/entry-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Błąd", "Nie można przeładować ekranu logowania.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onCreateWorkspaceButtonClick() {
        String workspaceName = workspaceNameTextField.getText().trim();

        if (workspaceName.isEmpty()) {
            showAlert("Błąd", "Nazwa workspace nie może być pusta.", Alert.AlertType.ERROR);
            return;
        }

        new Thread(() -> {
            try {
                Workspace newWorkspace = ApiClient.createAndAddWorkspaceToUser(
                        user.getLogin(), user.getPassword(), workspaceName
                );

                if (newWorkspace != null) {
                    user.getWorkspaceList().add(newWorkspace);

                    Platform.runLater(() -> {
                        Button workspaceButton = new Button(newWorkspace.getName());
                        workspaceButton.setMinWidth(100);
                        workspaceButton.setMinHeight(60);
                        workspaceTilePane.getChildren().add(workspaceButton);
                        showAlert("Sukces", "Workspace utworzony i dodany!", Alert.AlertType.INFORMATION);
                        workspaceNameTextField.clear();
                    });

                } else {
                    Platform.runLater(() -> showAlert("Błąd", "Nie udało się utworzyć workspace.", Alert.AlertType.ERROR));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Błąd", "Wystąpił problem z połączeniem z serwerem.", Alert.AlertType.ERROR));
            }
        }).start();
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
