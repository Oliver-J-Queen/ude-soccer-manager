package org.shopfront;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecievedPlayersPopup{


    public void main(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("RecievedPlayerPopup.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
