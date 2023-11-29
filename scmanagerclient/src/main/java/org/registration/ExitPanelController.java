package org.registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.eclipse.jetty.server.Authentication;
import org.mainmenu.UserPanelController;

import java.io.File;

public class ExitPanelController<T> {

    private double x,y;
    private Object window;

    @FXML
    private Button button_Yes;

    @FXML
    private Button button_No;

    @FXML
    public void buttonNoPressed(ActionEvent event) throws Exception {
        button_No.getScene().getWindow().hide();
        if(window instanceof LoginController){
            LoginController lc = new LoginController();
            lc.main(new Stage());
        }
        else if(window instanceof RegistrationController){
            RegistrationController rc = new RegistrationController();
            rc.main(new Stage());
        }
        else {
            UserPanelController uc = new UserPanelController();
            uc.main(new Stage());
        }
    }

    @FXML
    public void buttonYesPressed(ActionEvent event) {
        button_No.getScene().getWindow().hide();
    }

    @FXML
    public void YesEntered(){
        button_Yes.setStyle("-fx-background-color: #208af5;");
    }
    public void NoEntered(){
        button_No.setStyle("-fx-background-color: #208af5;");
    }
    public void YesExited(){
        button_Yes.setStyle("-fx-background-color: #0066cc;");
    }
    public void NoExited(){
        button_No.setStyle("-fx-background-color: #0066cc;");
    }

    public void main(Stage primaryStage, Object input) throws Exception {
        //creates a new window
        //Parent root = FXMLLoader.load(new File("src/main/resources/org/registration/ExitPanel.fxml").toURI().toURL());
        Parent root = FXMLLoader.load(getClass().getResource("ExitPanel.fxml"));
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
        window = input;

         */
    }
}