package org.example.licencjatv2_fe.EntryWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import org.example.licencjatv2_fe.Api.ApiClient;
import org.example.licencjatv2_fe.DTO.DTOService;

import java.io.IOException;
import java.util.Arrays;

public class EntryController {
    DTOService dtoService = new DTOService();
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField confirmPasswordTextField = new TextField();
    @FXML
    private Button loginButton;
    @FXML
    private Label alertLabel;
    @FXML
    private Label confirmPasswordLabel = new Label();
    @FXML
    private MenuItem loginMenuItem = new MenuItem();
    @FXML
    private MenuItem registerMenuItem = new MenuItem();
    @FXML
    private MenuItem closeMenuItem = new MenuItem();
    @FXML
    private MenuItem helpMenuItem = new MenuItem();

    @FXML
    protected void onLoginMenuItemClicked(){
        System.out.println("Login Menu Item Clicked!");
        loginButton.setText("Login");
        confirmPasswordLabel.setVisible(false);
        confirmPasswordTextField.setVisible(false);
    }
    @FXML
    protected void onRegisterMenuItemClicked(){
        System.out.println("Register Menu Item Clicked!");
        loginButton.setText("Register");
        confirmPasswordLabel.setVisible(true);
        confirmPasswordTextField.setVisible(true);

    }
    @FXML
    protected void onHelpMenuItemClicked(){
        System.out.println("Help Menu Item Clicked!");
    }
    @FXML
    protected void onCloseMenuItemClicked(){
        System.out.println("Close Menu Item Clicked!");
    }

    private String[] onLoginButtonClick() {
        String login;
        String password;
        login = loginTextField.getText();
        password = passwordTextField.getText();
        try {

            String response = ApiClient.login(login, password);
            if (response == null) {
                alertLabel.setText("Wrong credentials");
            } else {
//                System.out.println(response);
                alertLabel.setText("");
                System.out.println(dtoService.userMapping(response));


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new String[]{login, password};
    }

    private String []onRegisterButtonClicked(){
        String login;
        String password;
        String confirmedPassword;
        login = loginTextField.getText();
        password = passwordTextField.getText();
        confirmedPassword = confirmPasswordTextField.getText();
        if(!password.equals(confirmedPassword)){
            alertLabel.setText("Passwords does not match!");
        }else{
            try {
                String response = ApiClient.register(login,password);
                if (response == null || response.isEmpty()) {
                    alertLabel.setText("Something went Wrong!");
                }else{
                    alertLabel.setText("User "+login+" created! C:");
                    System.out.println(response);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return new String[]{login,password};
    }

    @FXML
    protected void choseAcctionOnButtonClick(){
        if(loginButton.getText().equals("Login")){
            System.out.println(Arrays.toString(onLoginButtonClick()));
        }else {
            System.out.println(Arrays.toString(onRegisterButtonClicked()));
        }
    }
}