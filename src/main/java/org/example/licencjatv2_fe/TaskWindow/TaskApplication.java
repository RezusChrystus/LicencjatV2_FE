package org.example.licencjatv2_fe.TaskWindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.licencjatv2_fe.Classes.Task;

import java.io.IOException;

public class TaskApplication {

    public static void launchTaskWindow(String workspaceTag, Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(TaskApplication.class.getResource("/org/example/licencjatv2_fe/TaskWindow/task-window-view.fxml"));
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
}
