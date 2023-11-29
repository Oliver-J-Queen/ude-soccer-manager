package org.serverlauncher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Yelle Lieder
 */

public class MainServer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //
        Parent root = FXMLLoader.load(getClass().getResource("ServerView.fxml"));
        primaryStage.setTitle("SCManager Server");
        primaryStage.setScene(new Scene(root, 600, 375));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
