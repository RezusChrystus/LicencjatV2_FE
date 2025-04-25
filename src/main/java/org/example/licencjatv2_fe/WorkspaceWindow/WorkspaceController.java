package org.example.licencjatv2_fe.WorkspaceWindow;

import javafx.fxml.FXML;
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

    public void initData(User user, Workspace workspace) {
        usernameLabel.setText("Logged in as: " + user.getLogin());
        workspaceNameLabel.setText("Workspace: " + workspace.getName());
        //todo: make different lists for each "State" enum value.
        List<Task> tasks = workspace.getTaskList(); // jeśli masz getter
        for (Task task : tasks) {
            taskListView.getItems().add(task.getContent()); // lub task.toString()
        }
    }
}
