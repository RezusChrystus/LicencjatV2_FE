package org.example.licencjatv2_fe.UserWindow;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import org.example.licencjatv2_fe.Classes.User;
import org.example.licencjatv2_fe.Classes.Workspace;

public class UserController {

    @FXML
    private TilePane workspaceTilePane;
    @FXML
    protected Button addWorkspaceButton;
    @FXML
    TextField workspaceTagTextField;

    private User user;

    public void setUser(User user) {
        this.user = user;

        // Wypełnianie siatki nazwami workspaces
        for (Workspace workspace : user.getWorkspaceList()) {
            Button workspaceButton = new Button(workspace.getName());
            workspaceButton.setMinWidth(100);
            workspaceButton.setMinHeight(60);

            // później tu dodasz event kliknięcia
            workspaceTilePane.getChildren().add(workspaceButton);
        }
    }
    @FXML
    private void onAddWorkspaceButtonClick(){
        workspaceTagTextField.getText();

        System.out.println("add workspace clicked!");
    }
}