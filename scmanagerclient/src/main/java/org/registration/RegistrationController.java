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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.clientconnection.RequestHelper;
import org.mainmenu.UserPanelController;
import org.registration.LoginController;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationController {

    @FXML
    private TextField txt_Email;
    @FXML
    private TextField txt_Username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField repeatpassword;
    @FXML
    private Button button_Back;
    @FXML
    private Button button_Register;
    @FXML
    private Button button_Exit;
    @FXML
    private Label fail_message;

    @FXML
    private AnchorPane registrationPanel;
    private double x, y;

    public void clearMail(){
        txt_Email.setText("");
    }
    public void clearUserName(){
        txt_Username.setText("");
    }
    public void clearPasswordRepeat(){
        repeatpassword.setText("");
    }
    public void clearPassword(){
        password.setText("");
    }

    public void mouseEnteredUserName(){
        txt_Username.setStyle("-fx-border-color: #198cff; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 3;");
    }

    public void mouseExitedUserName(){
        txt_Username.setStyle("-fx-border-color: #0066cc; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 2;");
    }

    public void mouseEnteredPassword(){
        password.setStyle("-fx-border-color: #198cff; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 3;");
    }

    public void mouseExitedPassword(){
        password.setStyle("-fx-border-color: #0066cc; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 2;");
    }

    public void mouseEnteredMail(){
        txt_Email.setStyle("-fx-border-color: #198cff; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 3;");
    }

    public void mouseExitedMail(){
        txt_Email.setStyle("-fx-border-color: #0066cc; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 2;");
    }

    public void mouseEnteredRepeatPassword(){
        repeatpassword.setStyle("-fx-border-color: #198cff; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 3;");
    }

    public void mouseExitedRepeatPassword(){
        repeatpassword.setStyle("-fx-border-color: #0066cc; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 2;");
    }

    public void backEntered(){
        button_Back.setStyle("-fx-background-color: #208af5;");
    }
    public void registerEntered(){
        button_Register.setStyle("-fx-background-color: #208af5;");
    }
    public void backExited(){
        button_Back.setStyle("-fx-background-color: #0066cc;");
    }
    public void registerExited(){
        button_Register.setStyle("-fx-background-color: #0066cc;");
    }

    public void mouseEnteredExit(){ button_Exit.setTextFill(Paint.valueOf("#ff0000"));}
    public void mouseExitedExit(){ button_Exit.setTextFill(Paint.valueOf("#ffffff"));}

    public void main(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(new File("src/main/resources/org/registration/RegistrationPanel.fxml").toURI().toURL());
        Parent root = FXMLLoader.load(getClass().getResource("RegistrationPanel.fxml"));
        stage.setScene(new Scene(root));
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("SCManager Client");
        stage.show();
/*
        root.setOnMouseDragged(event -> {
            x = event.getScreenX();
            y = event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

 */
    }

    public void openLogin(ActionEvent event) throws Exception {
        LoginController lc = new LoginController();
        button_Back.getScene().getWindow().hide();
        lc.main(new Stage());
    }

    public void registerUser(ActionEvent event) throws Exception {
        //checking if username contains spaces and special characters
        if(containsSpecialCharacters(txt_Username.getText()) == true || containsSpecialCharacters(password.getText())==true){
            fail_message.setText("Invalid characters");
        }
        else if(checkEmailisVaild(txt_Email.getText())==false){
            fail_message.setText("Invalid E-Mail");
        }
        else {
            //Password check
            if (!password.getText().equalsIgnoreCase(repeatpassword.getText())) {
                //inform user about failed registration
                fail_message.setText("Password not identical");
            }
            else if (password.getText().length() < 6) {
                //inform user about failed registration
                fail_message.setText("Password to short");
            } else{
                //checking if Username and Email Textfield aren't empty
                if (!txt_Username.getText().equals("") && !txt_Email.getText().equals("")) {
                    //calling request method to check registration data in database
                    String tmpString = RequestHelper.registration(txt_Username.getText(), txt_Email.getText(), password.getText());
                    System.out.println("Server Return: "+tmpString);
                    if (tmpString.equals("Username already taken")) {
                        //inform user about failed registration
                        fail_message.setText("Username already taken");
                    }
                    if(tmpString.equals("Email is not valid")){
                        //inform user about failed registration
                        fail_message.setText("Email is not valid");
                    }
                    if (tmpString.equals("Email already taken")) {
                        //inform user about failed registration
                        fail_message.setText("Email already in use");
                    }
                    if (tmpString.equals("Registration successful")) {
                        LoginController.activeUsername = txt_Username.getText();
                        UserPanelController uc = new UserPanelController();
                        uc.main(new Stage());
                        button_Register.getScene().getWindow().hide();
                    }
                } else {
                    fail_message.setText("Email or Username is empty");
                }
            }
        }
    }

    public boolean containsSpecialCharacters(String word){
        String[] specialcharacters = {" ","/","%","§","#","$","&","*","(",")","|","+",
                "-","=","<",">","_","?","{","}","~","!","?","'",",",".",";",":"};
        for(int i = 0; i < specialcharacters.length; i++){
            if(word.contains(specialcharacters[i])){
                return true;
            }
        }
        return false;
    }

    public boolean checkEmailisVaild(String email){
        //the regular expressions are from the Website: https://blog.mailtrap.io/java-email-validation/
        String emailSymbols = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        Pattern tmp = Pattern.compile(emailSymbols);
        return tmp.matcher(email).matches();
    }

    public void doExit() throws Exception {
        ExitPanelController ec = new ExitPanelController();
        Stage tmp = (Stage) button_Exit.getScene().getWindow();
        button_Exit.getScene().getWindow().hide();
        ec.main(new Stage(), new RegistrationController());
    }
}
