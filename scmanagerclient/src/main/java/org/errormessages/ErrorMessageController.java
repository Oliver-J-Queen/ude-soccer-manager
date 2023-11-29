package org.errormessages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ErrorMessageController {
    @FXML
    Button close;

    double x,y;

    public void main(Stage primaryStage) throws Exception {
        //creating a new window
        Parent root = FXMLLoader.load(getClass().getResource("InsufficientSEP.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        //window is draggable
        root.setOnMouseDragged(event -> {
            x = event.getScreenX();
            y = event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });
    }
    public void close(){
        close.getScene().getWindow().hide();
    }
}
