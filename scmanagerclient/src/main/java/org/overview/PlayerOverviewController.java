package org.overview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mainmenu.UserPanelController;
import org.overview.Player;
import org.registration.LoginController;
import org.registration.UserEditPanelController;

import org.clientconnection.RequestHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PlayerOverviewController implements Initializable{

    private double x,y;

    @FXML private TextField searchfield;

    @FXML private TableView<Player> table;
    @FXML private TableColumn<Player, String> col_name;
    @FXML private TableColumn<Player, String> col_club;
    @FXML private TableColumn<Player, String> col_pos;
    @FXML private TableColumn<Player, Integer> col_age;
    @FXML private TableColumn<Player, String> col_nat;
    @FXML private TableColumn<Player, Double> col_strength;

    public void main(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(new File("src/main/resources/org/overview/PlayerOverview.fxml").toURI().toURL());
        Parent root = FXMLLoader.load(getClass().getResource("PlayerOverview.fxml"));
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        root.setOnMouseDragged(event -> {
            x = event.getScreenX();
            y = event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col_name.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        col_club.setCellValueFactory(new PropertyValueFactory<Player, String>("club"));
        col_pos.setCellValueFactory(new PropertyValueFactory<Player, String>("position"));
        col_nat.setCellValueFactory(new PropertyValueFactory<Player, String>("nationality"));
        col_age.setCellValueFactory(new PropertyValueFactory<Player, Integer>("age"));
        col_strength.setCellValueFactory(new PropertyValueFactory<Player, Double>("strength"));


        try {
            ArrayList<Player> players = RequestHelper.viewPlayer();
            ObservableList<Player>nplayers = FXCollections.observableArrayList(players);
            FilteredList<Player> filteredPlayers = new FilteredList<>(nplayers, b ->true);

            searchfield.textProperty().addListener((observable, oldValue, newValue ) ->{
                filteredPlayers.setPredicate(player -> {
                    if(newValue==null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();

                    if(player.getName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(player.getClub().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(player.getNationality().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(player.getPosition().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if(player.getAge().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                    return false;
                });
            });

            SortedList<Player>sortedPlayers = new SortedList<>(filteredPlayers);
            sortedPlayers.comparatorProperty().bind(table.comparatorProperty());

            table.setItems(sortedPlayers);

        } catch (IOException | InterruptedException e) {
            System.out.print("Something went wrong!/n Restart the Program and try again!");
        }
    }
}
