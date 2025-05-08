package org.example.licencjatv2_fe.WorkspaceWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.licencjatv2_fe.Classes.State;
import org.example.licencjatv2_fe.Classes.Task;
import org.example.licencjatv2_fe.Classes.User;
import org.example.licencjatv2_fe.Classes.Workspace;
import org.example.licencjatv2_fe.TaskWindow.TaskApplication;
import org.example.licencjatv2_fe.TaskWindow.TaskController;

import java.io.IOException;
import java.time.LocalDate;
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
    Workspace workspace;

    public void initData(User user, Workspace workspace) {
        this.workspace = workspace;
        usernameLabel.setText("Logged in as: " + user.getLogin());
        workspaceNameLabel.setText("Workspace: " + workspace.getName());

        for (Task task : workspace.getTaskList()) {
            switch (task.getState()) {
                case PENDING -> pendingListView.getItems().add(task.getContent());
                case IN_PROGRESS -> inProgressListView.getItems().add(task.getContent());
                case COMPLETED -> completedListView.getItems().add(task.getContent());
            }
        }
        setupClickListeners();

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

 /*   private void openAddTaskWindow() {
        Stage addTaskStage = new Stage();
        addTaskStage.setTitle("Add New Task");

        // Pola do wpisywania danych
        TextField nameField = new TextField();
        nameField.setPromptText("Task name");

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due date");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("PENDING", "IN_PROGRESS", "DONE");
        statusBox.setValue("PENDING");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            LocalDate dueDate = dueDatePicker.getValue();
            String status = statusBox.getValue();

            // Tutaj można dodać logikę zapisu taska, np. wywołanie metody z ApiClient

            System.out.println("Task name: " + name);
            System.out.println("Due date: " + dueDate);
            System.out.println("Status: " + status);

            addTaskStage.close(); // zamknij okienko po zapisaniu
        });

        VBox layout = new VBox(10, nameField, dueDatePicker, statusBox, saveButton);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 300, 250);
        addTaskStage.setScene(scene);
        addTaskStage.initModality(Modality.APPLICATION_MODAL); // blokuje okno główne
        addTaskStage.showAndWait();
    }*/
}
