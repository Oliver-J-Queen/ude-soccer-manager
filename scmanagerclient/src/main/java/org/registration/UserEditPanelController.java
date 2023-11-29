package org.registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.clientconnection.RequestHelper;
import org.mainmenu.UserPanelController;

import java.io.File;
import java.io.IOException;

public class UserEditPanelController {

    private double x,y;

    @FXML
    private TextField txt_Email;

    @FXML
    private TextField txt_Username;

    @FXML
    private Button button_Back;

    @FXML
    private PasswordField txt_Password;

    @FXML
    private Button changeEmail;

    @FXML
    private Button changePassword;

    @FXML
    private Button changeUsername;

    @FXML
    private Label fail_message;

    @FXML
    void cancelPressed(ActionEvent event) throws Exception {
        changeEmail.getScene().getWindow().hide();
    }

    @FXML
    void editEmail(ActionEvent event) throws IOException, InterruptedException {
        RegistrationController tmp = new RegistrationController();
        if(txt_Email.getText().equals("")||txt_Email.getText().equals("Email")) {
            fail_message.setText("Please enter your new Email");
        }else if(tmp.checkEmailisVaild(txt_Email.getText())==false){
            fail_message.setText("Email is invalid");
        }else {
            fail_message.setText(RequestHelper.editUserEmail(LoginController.activeUsername, txt_Email.getText()));
        }
    }

    @FXML
    void editPassword(ActionEvent event) throws IOException, InterruptedException {
        RegistrationController tmp = new RegistrationController();
        if(txt_Password.getText().equals("")) {
            fail_message.setText("Please enter your new Password");
        }else if(tmp.containsSpecialCharacters(txt_Password.getText()) == true){
            fail_message.setText("Password: No spaces and special characters allowed");
        }else{
            if(txt_Password.getText().length() > 6) {
                fail_message.setText(RequestHelper.editUserPassword(LoginController.activeUsername, txt_Password.getText()));
            }else{
                fail_message.setText("Password to short");
            }
        }
    }

    @FXML
    void editUsername(ActionEvent event) throws IOException, InterruptedException {
        RegistrationController tp = new RegistrationController();
        if(txt_Username.getText().equals("")||txt_Username.getText().equals("Username")) {
            fail_message.setText("Please enter your new Username");
        }else if(tp.containsSpecialCharacters(txt_Username.getText()) == true){
            fail_message.setText("Username: No spaces and special characters allowed");
        }else {
            String tmp = RequestHelper.editUserName(LoginController.activeUsername, txt_Username.getText());
            if (tmp.equalsIgnoreCase("Username already exists")) {
                fail_message.setText("Username already exists");
            } else {
                fail_message.setText("Username changed succesfully");
                LoginController.activeUsername = tmp;
            }
        }
    }

    public void main(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(new File("src/main/resources/org/registration/UserEditPanel.fxml").toURI().toURL());
        Parent root = FXMLLoader.load(getClass().getResource("UserEditPanel.fxml"));
        primaryStage.setScene(new Scene(root));
       // primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("SCManager Client");
        primaryStage.show();
/*
        root.setOnMouseDragged(event -> {
            x = event.getScreenX();
            y = event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

 */


    }

}
