package org.example.licencjatv2_fe.WorkspaceWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.licencjatv2_fe.Api.ApiClient;
import org.example.licencjatv2_fe.Classes.State;
import org.example.licencjatv2_fe.Classes.Task;
import org.example.licencjatv2_fe.Classes.User;
import org.example.licencjatv2_fe.Classes.Workspace;
import org.example.licencjatv2_fe.DTO.DTOService;
import org.example.licencjatv2_fe.TaskWindow.TaskController;

import java.io.IOException;

public class WorkspaceController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label workspaceNameLabel;
    @FXML
    private ListView<String> taskListView;

    @FXML
    private ListView<String> pendingListView;
    @FXML
    private ListView<String> inProgressListView;
    @FXML
    private ListView<String> completedListView;
    @FXML
    private Button addTaskButton;
    Workspace workspace;
    User user;

    public void initData(User user, Workspace workspace) {
        this.user = user;
        this.workspace = workspace;
        usernameLabel.setText("Logged in as: " + user.getLogin());
        workspaceNameLabel.setText("Workspace: " + workspace.getName());

        refreshTaskLists();
        setupClickListeners();
    }

    private void refreshTaskLists() {
        pendingListView.getItems().clear();
        inProgressListView.getItems().clear();
        completedListView.getItems().clear();

        for (Task task : workspace.getTaskList()) {
            switch (task.getState()) {
                case PENDING -> pendingListView.getItems().add(task.getContent());
                case IN_PROGRESS -> inProgressListView.getItems().add(task.getContent());
                case COMPLETED -> completedListView.getItems().add(task.getContent());
            }
        }
    }

    private void reloadWorkspaceFromBackend() {
        try {
            Workspace updatedWorkspace = ApiClient.findByTag(workspace.getTag());
            workspace.setTaskList(updatedWorkspace.getTaskList());
            refreshTaskLists();
            reloadUserWorkspaces();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadUserWorkspaces() {
        DTOService dtoService =    new DTOService();
        try {
            User updatedUser = dtoService.userMapping(ApiClient.login(user.getLogin(),user.getPassword()));
            user.setWorkspaceList(updatedUser.getWorkspaceList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddTaskButtonClick() {
        openTaskWindow(workspace.getTag(), null);
    }

    private void openTaskWindow(String workspaceTag, Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/licencjatv2_fe/task-window-view.fxml"));
            Parent root = loader.load();

            TaskController controller = loader.getController();
            controller.setData(workspaceTag, task);

            Stage stage = new Stage();
            stage.setTitle(task == null ? "Dodaj zadanie" : "Edytuj zadanie");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(event -> reloadWorkspaceFromBackend());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        pendingListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = pendingListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    Task task = findTaskByContentAndState(selected, State.PENDING);
                    openTaskWindow(workspace.getTag(), task);
                }
            }
        });

        inProgressListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = inProgressListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    Task task = findTaskByContentAndState(selected, State.IN_PROGRESS);
                    openTaskWindow(workspace.getTag(), task);
                }
            }
        });

        completedListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selected = completedListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    Task task = findTaskByContentAndState(selected, State.COMPLETED);
                    openTaskWindow(workspace.getTag(), task);
                }
            }
        });
    }

    private Task findTaskByContentAndState(String content, State state) {
        return workspace.getTaskList().stream()
                .filter(task -> task.getContent().equals(content) && task.getState() == state)
                .findFirst()
                .orElse(null);
    }
}
