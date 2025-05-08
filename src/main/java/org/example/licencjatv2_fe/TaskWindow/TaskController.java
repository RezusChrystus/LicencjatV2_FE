package org.example.licencjatv2_fe.TaskWindow;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.licencjatv2_fe.Api.ApiClient;
import org.example.licencjatv2_fe.Classes.State;
import org.example.licencjatv2_fe.Classes.Task;

import java.time.LocalDate;

public class TaskController {

    @FXML private TextField titleField;
    @FXML private DatePicker dueDateField;
    @FXML private ChoiceBox<String> stateChoiceBox;

    private String workspaceTag;
    private Task editingTask;

    public void initialize() {
        stateChoiceBox.getItems().addAll("PENDING", "IN_PROGRESS", "COMPLETED");

    }

    public void setData(String workspaceTag, Task task) {
        this.workspaceTag = workspaceTag;
        this.editingTask = task;

        if (task != null) {
            titleField.setText(task.getContent());
            dueDateField.setValue(task.getDeadline());
            stateChoiceBox.setValue(task.getState().toString());
        } else {
            stateChoiceBox.setValue("PENDING");
        }
        Task t = new Task(1L,"asd", State.COMPLETED,null,null);
        System.out.println(t);
    }

    @FXML
    private void handleSave() {
        String title = titleField.getText();
        LocalDate dueDate = dueDateField.getValue(); // zakładam, że pole nazywa się dueDatePicker
        String state = stateChoiceBox.getValue();

        if (title.isBlank() || dueDate == null || state == null) {
            System.out.println("Uzupełnij wszystkie pola");
            return;
        }

        if (editingTask == null) {
            System.out.println("Dodawanie zadania do workspace: " + workspaceTag);
            try {
                String response = ApiClient.addTaskToWorkspace(workspaceTag, title, dueDate.toString(), state);
                System.out.println("Odpowiedź z API: " + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Edytowanie zadania ID: " + editingTask.getId());

            // Zaktualizuj dane taska lokalnie
            editingTask.setContent(title);
            editingTask.setDeadline(dueDate);
            editingTask.setState(State.valueOf(state));

            // Wywołaj metodę aktualizacji
            ApiClient.updateTask(editingTask);
        }

        ((Stage) titleField.getScene().getWindow()).close();
    }


}
