package org.MatchRequest;

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
import org.mainmenu.UserPanelController;
import org.registration.LoginController;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmationPanelController implements Initializable {

    private double x,y;
    public static String matchResult;

    @FXML
    private Label label_Confirmation;

    @FXML
    private Button accept_Button;

    @FXML
    private Button decline_Button;

    @FXML
    private Label label_Text;

    @FXML
    void accept(ActionEvent event) throws Exception {
        if (MatchRequestController.activeTable.equals("requests")) {
            String tmp = RequestHelper.answerMatchRequest(LoginController.activeUsername, MatchRequestController.getChosenRequest, "angenommen");
            if(tmp.equals("User1")){
                matchResult = "Last Match: Won";
            }else if(tmp.equals("User2")){
                matchResult = "Last Match: Lost";
            }else if(tmp.equals("Draw")){
                matchResult = "Last Match: Draw";
            }else if(tmp.equals("Null")){
                matchResult = "You need to choose your formation, setup and reserve!";
            }else if(tmp.equals("Opponent")){
                matchResult = "Your opponent need to choose his formation, setup and reserve!";
            }
            accept_Button.getScene().getWindow().hide();
        }else if(MatchRequestController.activeTable.equals("friends")){
            RequestHelper.sendMatchRequest(LoginController.activeUsername, MatchRequestController.getChosenRequest);
            System.out.println(MatchRequestController.getChosenRequest);
            accept_Button.getScene().getWindow().hide();
        }
        UserPanelController tp = new UserPanelController();
        tp.main(new Stage());
    }

    @FXML
    void decline(ActionEvent event) throws Exception {
        if (MatchRequestController.activeTable.equals("requests")) {
            RequestHelper.answerMatchRequest(LoginController.activeUsername, MatchRequestController.getChosenRequest, "abgelehnt");
            accept_Button.getScene().getWindow().hide();
        }else if(MatchRequestController.activeTable.equals("friends")){
            accept_Button.getScene().getWindow().hide();
        }
        UserPanelController tp = new UserPanelController();
        tp.main(new Stage());
    }

    public void main(Stage primaryStage) throws Exception {
        //creating a new window
        Parent root = FXMLLoader.load(getClass().getResource("ConfirmationPanel.fxml"));
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
    public void initialize (URL location, ResourceBundle resources) {
        if (MatchRequestController.activeTable.equals("requests")) {
            label_Text.setText("Do you want to play against " + MatchRequestController.getChosenRequest + "?");
        }else if(MatchRequestController.activeTable.equals("friends")){
            label_Text.setText("Do you want to send "+ MatchRequestController.getChosenRequest + " a match request?");
        }
        matchResult = "Last Match:";
    }
}
