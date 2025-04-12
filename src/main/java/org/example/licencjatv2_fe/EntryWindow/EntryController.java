package org.example.licencjatv2_fe.EntryWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.licencjatv2_fe.Api.ApiClient;

import java.io.IOException;

public class EntryController {
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;

    @FXML
    private Label alertLabel;

    @FXML
    protected String[] onLoginButtonClick() {
        String login;
        String password;
        login = loginTextField.getText();
        password = passwordTextField.getText();
        try {

            String response = ApiClient.login(login, password);
            if(response==null){
             alertLabel.setText("Wrong credentials");
            }else {
                //newUser1
                //newUser1Password
                System.out.println(response);
                alertLabel.setText("");

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new String[]{login, password};
    }
}