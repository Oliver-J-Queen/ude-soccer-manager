package org.tournament;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.clientconnection.RequestHelper;
import org.clientconnection.TournamentHelper;
import org.registration.LoginController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TournamentCreationController implements Initializable {

    @FXML Button createTOURNAMENT;
    @FXML TextField NAMEFIELD;
    @FXML ChoiceBox<String> set_MAXPLAYERS, set_ENTRYFEE, set_GAMEMODE;
    @FXML Label creationStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        set_MAXPLAYERS.getItems().addAll("4","6","8");
        set_ENTRYFEE.getItems().addAll("200", "400","800","1000");
        set_GAMEMODE.getItems().addAll("League", "Knockout");


    }

    @FXML
    public void createTournament(ActionEvent event) throws IOException, InterruptedException {

        String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
        String id = ID[3];

        String ChosenName = NAMEFIELD.getText().replace(" ", "_");

        String created = TournamentHelper.createTournament(id,ChosenName, set_GAMEMODE.getSelectionModel().getSelectedItem(),set_ENTRYFEE.getSelectionModel().getSelectedItem(),set_MAXPLAYERS.getSelectionModel().getSelectedItem());

        System.out.println(created);
        if(created.equals("success")) {
            creationStatus.setText("Tournament created successfully!");
            System.out.println("Tournament created!");
        }else if(created.equals("fail")){
            creationStatus.setText("Creation Failed!");
        }else if(created.equals("teamerror")){
            creationStatus.setText("Please Setup your Team first!");
        }
    }
}
