package org.tournament;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.clientconnection.RequestHelper;
import org.clientconnection.TournamentHelper;
import org.registration.LoginController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

//inspiration from https://rb.gy/ddau2r when passing values between Controllers
//inspiration from https://rb.gy/osxqod adding a button row to tableview

public class TournamentController implements Initializable {

    @FXML private Button set_TOURNAMENTNAME, refresh, open_TOURNAMENTCREATION;
    @FXML private Label JoinStatus;

    @FXML private TableView<Tournament> table;
    @FXML private TableColumn<Tournament, String> col_name;
    @FXML private TableColumn<Tournament, Integer> col_fee;
    @FXML private TableColumn<Tournament, String> col_mode;
    @FXML private TableColumn<Tournament, String> col_currentplayers;
    @FXML private TableColumn<Tournament, Void> colBtn = new TableColumn("Button Column");


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        col_name.setCellValueFactory(new PropertyValueFactory<Tournament, String>("name"));
        col_fee.setCellValueFactory(new PropertyValueFactory<Tournament, Integer>("entryFee"));
        col_mode.setCellValueFactory(new PropertyValueFactory<Tournament, String>("mode"));
        col_currentplayers.setCellValueFactory(new PropertyValueFactory<Tournament, String>("playercount"));

        Callback<TableColumn<Tournament, Void>, TableCell<Tournament, Void>> cellFactory = new Callback<TableColumn<Tournament, Void>, TableCell<Tournament, Void>>() {
            @Override
            public TableCell<Tournament, Void> call(final TableColumn<Tournament, Void> param) {
                final TableCell<Tournament, Void> cell = new TableCell<Tournament, Void>() {

                    private final Button btn = new Button("Join");

                    {
                        btn.setOnAction((ActionEvent event) -> {

                            try {
                                String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                                String id = ID[3];
                                String tournamentID = Integer.toString(getTableView().getItems().get(getIndex()).getID());
                                int tournamentFee = getTableView().getItems().get(getIndex()).getEntryFee();
                                int playerCount = getTableView().getItems().get(getIndex()).getMaxAnzahl();
                                String join = TournamentHelper.joinTournament(id, tournamentID);
                                System.out.println(join);

                                if(join.equalsIgnoreCase("success")){
                                    JoinStatus.setText("You've successfully joined the tournament!");
                                }
                                if(join.equalsIgnoreCase("duplicate")){
                                    JoinStatus.setText("You're already part of this Tournament!");
                                    System.out.println("set");
                                }
                                if(join.equalsIgnoreCase("failure")){
                                    JoinStatus.setText("Insufficient Funds!");
                                }if(join.equalsIgnoreCase("full")){
                                    FXMLLoader Loader = new FXMLLoader();
                                    Loader.setLocation(getClass().getResource("PlacementPayoutWindow.fxml"));
                                    try{
                                        Loader.load();
                                    } catch (IOException ex) {
                                        Logger.getLogger(TournamentController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    PayoutWindowController payoutPlacement = Loader.getController();
                                    payoutPlacement.showPlacementPayout(tournamentID,tournamentFee,playerCount);

                                    Parent root = Loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root));
                                    stage.show();
                                }if(join.equalsIgnoreCase("teamerror")){
                                    JoinStatus.setText("Setup your Team before joining a Tournament!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        try {
            if (TournamentHelper.getTournaments() != null) {
                ArrayList<Tournament> tmp = TournamentHelper.getTournaments();
                ObservableList<Tournament> observableTournaments = FXCollections.observableArrayList(tmp);

                table.setItems(observableTournaments);
            }
        }
        catch(InterruptedException | IOException e){
                e.printStackTrace();
        }
    }

    public void refresh(ActionEvent event){

        try{
            ArrayList<Tournament> tmp = TournamentHelper.getTournaments();
            ObservableList<Tournament> observableList = FXCollections.observableArrayList(tmp);
            table.setItems(observableList);
        } catch (InterruptedException | IOException throwables){
            throwables.printStackTrace();
        }

        table.refresh();
    }

    @FXML
    public void openTournamentCreation(ActionEvent event) throws Exception {

        TournamentCreationWindow tw = new TournamentCreationWindow();
        tw.start(new Stage());
    }


}
