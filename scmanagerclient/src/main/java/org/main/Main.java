package org.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.registration.LoginController;


public class Main extends Application {
    LoginController lg = new LoginController();
    public void start(Stage primaryStage) throws Exception {
        lg.main(primaryStage);
        primaryStage.setTitle("SCManager Client");
    }

    public static void main(String[] args){
        launch(args);
    }

}