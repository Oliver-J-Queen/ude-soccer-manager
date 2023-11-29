package org.registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.clientconnection.RequestHelper;
import org.mainmenu.UserPanelController;


public class LoginController {

    private double x,y;
    public static String activeUsername;

    @FXML
    private TextField txt_Username, serverAddressText;
    @FXML
    private Button button_Register, saveButton;
    @FXML
    private Button button_Login;
    @FXML
    private PasswordField password;
    @FXML
    private Button button_Exit;
    @FXML
    private Label fail_message;
    @FXML
    private static String serverAddress = "localhost";


    public void doLogin(ActionEvent event) throws Exception {
        //checking if username contains spaces
        if(txt_Username.getText().contains(" ")){
            fail_message.setText("Username contains space");
        }
        //checking if password contains spaces
        else if(password.getText().contains(" ")){
            fail_message.setText("Password contains space");
        }
        else {
            //calling request method to check login data in database
            String tmp = RequestHelper.login(txt_Username.getText(), password.getText());
            if (tmp.equals("Login Successful")) {
                /*accessing the RegistrationPanel and the current username
                will be saved to change the Userdata in UserEditController*/
                activeUsername = txt_Username.getText();
                UserPanelController uc = new UserPanelController();
                uc.main(new Stage());
                button_Exit.getScene().getWindow().hide();
            }
            if (tmp.equals("Passwort falsch")) {
                //inform user about failed login attend
                fail_message.setText("Wrong Password");
            }
            if (tmp.equals("User nicht registriert")) {
                //inform user about failed login attend
                fail_message.setText("User is not registered");
            }

            //EXEMPLARISCH, BITTE GGF EUREN VORSTELLUNGEN ANPASSEN
            else{
                fail_message.setText(tmp);
            }


        }
    }

    //author: Yelle Lieder + related UI
    public void changeServerAddress(ActionEvent event){
        if(serverAddressText.isVisible()){
            serverAddressText.setVisible(false);
            saveButton.setVisible(false);
        }
        else{
            serverAddressText.setVisible(true);
            saveButton.setVisible(true);
        }
    }

    //author: Yelle Lieder + related UI
    public void saveServerAddress(ActionEvent event){
        if(validate(serverAddressText.getText())) {
            serverAddress = serverAddressText.getText();
            serverAddressText.setText(serverAddress + " saved as server address!");
        }
        else{
            serverAddressText.setText("no valid server address");
        }

    }

    public void doRegister(ActionEvent event) throws Exception {
        //accessing the RegistrationPanel
        RegistrationController rc = new RegistrationController();
        button_Login.getScene().getWindow().hide();
        rc.main(new Stage());
    }


    //clears txt_field onMouseClicked
    public void clearUserName(){ txt_Username.setText("");}
    public void clearPassword(){ password.setText(""); }

    //changes exit_button_color onMouseEntered
    public void mouseEnteredExit(){ button_Exit.setTextFill(Paint.valueOf("#ff0000"));}
    public void mouseExitedExit(){ button_Exit.setTextFill(Paint.valueOf("#ffffff"));}

    //changes txt_field_border-color onMouseEntered
    public void mouseEnteredUserName(){ txt_Username.setStyle("-fx-border-color: #198cff; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 3;"); }
    public void mouseExitedUserName(){ txt_Username.setStyle("-fx-border-color: #0066cc; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 2;"); }
    public void mouseEnteredPassword(){ password.setStyle("-fx-border-color: #198cff; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 3;"); }
    public void mouseExitedPassword(){ password.setStyle("-fx-border-color: #0066cc; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 2;"); }

    //changes button_color onMouseEntered
    public void loginEntered(){ button_Login.setStyle("-fx-background-color: #208af5;"); }
    public void registerEntered(){ button_Register.setStyle("-fx-background-color: #208af5;"); }
    public void loginExited(){ button_Login.setStyle("-fx-background-color: #0066cc;"); }
    public void registerExited(){ button_Register.setStyle("-fx-background-color: #0066cc;"); }

    //accessing the ExitPanel
    public void doExit() throws Exception {
        ExitPanelController ec = new ExitPanelController();
        button_Exit.getScene().getWindow().hide();
        ec.main(new Stage(), new LoginController());
    }


    public void main(Stage primaryStage) throws Exception {
        //creating a new window
        //Parent root = FXMLLoader.load(new File("src/main/resources/org/registration/LoginPanel.fxml").toURI().toURL());
        Parent root = FXMLLoader.load(getClass().getResource("LoginPanel.fxml"));
        primaryStage.setScene(new Scene(root));
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("SCManager Client");
        primaryStage.show();

        //window is draggable
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
    //author: Yelle Lieder
    //copied from https://stackoverflow.com/questions/5667371/validate-ipv4-address-in-java
    public static boolean validate(String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(PATTERN);
    }

    //author: Yelle Lieder
    public static String getServerAddress() {
        return serverAddress;
    }
}

