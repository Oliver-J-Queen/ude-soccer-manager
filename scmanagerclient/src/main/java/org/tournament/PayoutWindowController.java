package org.tournament;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.clientconnection.RequestHelper;
import org.clientconnection.TournamentHelper;
import org.registration.LoginController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PayoutWindowController extends TournamentController implements Initializable  {

    @FXML
    private Button Accept;
    @FXML
    private Label PLACEMENT, WINNINGS;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        


    }
     public void showPlacementPayout(String tournamentID, int entryFee, int playerCount) throws IOException, InterruptedException {
         String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
         String id = ID[3];

        int[] temp = TournamentHelper.getPlacements(tournamentID);

        String firstPlace = Integer.toString(temp[0]);
        int earningsFirstPlace = ((entryFee * playerCount) * 2) / 6;
        String secondPlace = Integer.toString(temp[1]);
        String thirdPlace = Integer.toString(temp[2]);

        if (id.equalsIgnoreCase(firstPlace)) {
            PLACEMENT.setText("Congratulations! You took first place.");
            WINNINGS.setText(Integer.toString(earningsFirstPlace * 3));
        } else if (id.equalsIgnoreCase(secondPlace)) {
            PLACEMENT.setText("Congratulations! You took second place.");
            WINNINGS.setText(Integer.toString(earningsFirstPlace * 2));
        } else if (id.equalsIgnoreCase(thirdPlace)) {
            PLACEMENT.setText("Congratulations! You took third place.");
            WINNINGS.setText(Integer.toString(earningsFirstPlace * 1));
        } else {
            PLACEMENT.setText("Sorry but you didnt make it to the top three :(");
        }

     }

     public void close(ActionEvent event){

        Stage stage = (Stage) Accept.getScene().getWindow();
        stage.close();

     }
}
