package org.example.licencjatv2_fe.WorkspaceWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.licencjatv2_fe.Classes.Task;
import org.example.licencjatv2_fe.Classes.User;
import org.example.licencjatv2_fe.Classes.Workspace;

import java.util.List;

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

    public void initData(User user, Workspace workspace) {
        usernameLabel.setText("Logged in as: " + user.getLogin());
        workspaceNameLabel.setText("Workspace: " + workspace.getName());

        for (Task task : workspace.getTaskList()) {
            switch (task.getState()) {
                case PENDING -> pendingListView.getItems().add(task.getContent());
                case IN_PROGRESS -> inProgressListView.getItems().add(task.getContent());
                case COMPLETED -> completedListView.getItems().add(task.getContent());
            }
        }
    }

    @FXML
    protected void onAddTaskButtonClick(){
        System.out.println("I was clicked!");
    }
}
