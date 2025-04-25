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
import org.example.licencjatv2_fe.WorkspaceWindow.WorkspaceController;

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

        // Dodaj wszystkie workspace'y jako przyciski w TilePane
        for (Workspace workspace : user.getWorkspaceList()) {
            workspaceTilePane.getChildren().add(createWorkspaceButton(workspace));
        }
    }

    private Button createWorkspaceButton(Workspace workspace) {
        Button button = new Button(workspace.getName());
        button.setMinWidth(100);
        button.setMinHeight(60);
        button.setOnAction(e -> openWorkspaceWindow(user, workspace));
        return button;
    }

    private void openWorkspaceWindow(User user, Workspace workspace) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/licencjatv2_fe/workspace-window-view.fxml"));
            Parent root = loader.load();

            WorkspaceController controller = loader.getController();
            controller.initData(user, workspace);

            Stage stage = new Stage();
            stage.setTitle("Workspace: " + workspace.getName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
                Workspace newWorkspace = ApiClient.createAndAddWorkspaceToUser(user.getLogin(), user.getPassword(), workspaceName);
                if (newWorkspace != null) {
                    user.getWorkspaceList().add(newWorkspace);
                    Platform.runLater(() -> {
                        workspaceTilePane.getChildren().add(createWorkspaceButton(newWorkspace));
                        workspaceNameTextField.clear();
                        showAlert("Sukces", "Workspace utworzony i dodany!", Alert.AlertType.INFORMATION);
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
            showAlert("Błąd", "Pole tag nie może być puste.", Alert.AlertType.ERROR);
            return;
        }

        boolean alreadyExists = user.getWorkspaceList().stream()
                .anyMatch(w -> w.getTag().equalsIgnoreCase(tag));

        if (alreadyExists) {
            showAlert("Info", "Workspace już dodany.", Alert.AlertType.INFORMATION);
            return;
        }

        new Thread(() -> {
            try {
                Workspace newWorkspace = ApiClient.addWorkspaceToUser(user.getLogin(), user.getPassword(), tag);
                if (newWorkspace != null) {
                    user.getWorkspaceList().add(newWorkspace);
                    Platform.runLater(() -> {
                        workspaceTilePane.getChildren().add(createWorkspaceButton(newWorkspace));
                        workspaceTagTextField.clear();
                        showAlert("Sukces", "Workspace dodany pomyślnie!", Alert.AlertType.INFORMATION);
                    });
                } else {
                    Platform.runLater(() -> showAlert("Błąd", "Nie znaleziono workspace o podanym tagu.", Alert.AlertType.ERROR));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Błąd", "Brak połączenia z serwerem.", Alert.AlertType.ERROR));
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
