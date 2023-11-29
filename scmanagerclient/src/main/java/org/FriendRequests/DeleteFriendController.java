package org.FriendRequests;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.clientconnection.RequestHelper;
import org.registration.LoginController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeleteFriendController implements Initializable {
    private double x,y;

    @FXML
    private Label label_Confirmation;

    @FXML
    private Button accept_Button;

    @FXML
    private Button decline_Button;

    @FXML
    private Label label_From;

    @FXML
    private Label label_name;

    @FXML
    void accept(ActionEvent event) throws Exception {
        RequestHelper.removeFriend(LoginController.activeUsername, FriendOverviewController.getChosenFriend);
        accept_Button.getScene().getWindow().hide();
        FriendOverviewController tp = new FriendOverviewController();
        tp.main(new Stage());
    }

    @FXML
    void decline(ActionEvent event) throws Exception {
        accept_Button.getScene().getWindow().hide();
        FriendOverviewController tp = new FriendOverviewController();
        tp.main(new Stage());
    }

    public void main(Stage primaryStage) throws Exception {
        //creating a new window
        Parent root = FXMLLoader.load(getClass().getResource("DeleteFriendView.fxml"));
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

    @Override
    public void initialize (URL location, ResourceBundle resources){
        label_name.setText(FriendOverviewController.getChosenFriend);
    }
}
