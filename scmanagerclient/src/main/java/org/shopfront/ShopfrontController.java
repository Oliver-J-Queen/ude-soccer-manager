package org.shopfront;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.clientconnection.RequestHelper;
import org.errormessages.ErrorMessageController;
import org.mainmenu.UserPanelController;
import org.overview.Player;
import org.registration.LoginController;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShopfrontController implements Initializable {

    @FXML
    private Button buy_s, buy_m, buy_l, buy_special, clear, add;

    @FXML
    private ListView<String>playerListView;

    private String activeUser = LoginController.activeUsername;
    public ArrayList<Player> recievedPlayers = new ArrayList<>();
    public List<String> playernames = new ArrayList<>();
    public ObservableList<String> plnames = FXCollections.observableList(playernames);


    public void buy(ActionEvent event) throws Exception {
        try {
            if (event.getSource() == buy_s) {
                playernames = RequestHelper.buy_small(activeUser);
                plnames.clear();
                plnames.addAll(playernames);

                playerListView.refresh();
                playerListView.getItems().clear();
                playerListView.getItems().addAll(plnames);

                if (plnames == null || plnames.isEmpty()) {
                    ErrorMessageController er = new ErrorMessageController();
                    er.main(new Stage());
                }
                System.out.print(plnames);

            } else if (event.getSource() == buy_m) {
                playernames = RequestHelper.buy_medium(activeUser);
                plnames.clear();
                plnames.addAll(playernames);

                for (Player player : recievedPlayers) {
                    plnames.add(player.getName());
                }

                playerListView.refresh();
                playerListView.getItems().clear();
                playerListView.getItems().addAll(plnames);

                if (plnames == null || plnames.isEmpty()) {
                    ErrorMessageController er = new ErrorMessageController();
                    er.main(new Stage());
                }

                System.out.println(plnames);

            } else if (event.getSource() == buy_l) {
                playernames = RequestHelper.buy_large(activeUser);
                plnames.clear();
                plnames.addAll(playernames);


                playerListView.refresh();
                playerListView.getItems().clear();
                playerListView.getItems().addAll(plnames);

                if (plnames == null || plnames.isEmpty()) {
                    ErrorMessageController er = new ErrorMessageController();
                    er.main(new Stage());
                }
                System.out.println(plnames);

            } else if (event.getSource() == buy_special) {
                playernames = RequestHelper.buy_special(activeUser);
                plnames.clear();
                plnames.addAll(playernames);

                playerListView.refresh();
                playerListView.getItems().clear();
                playerListView.getItems().addAll(plnames);

                if (plnames == null || plnames.isEmpty()) {
                    ErrorMessageController er = new ErrorMessageController();
                    er.main(new Stage());
                }
                System.out.println(playernames);
            }
            else if (event.getSource() == add) {
                buy_m.getScene().getWindow().hide();
                UserPanelController u = new UserPanelController();
                u.main(new Stage());
            }
        }catch(Exception throwables){
            System.err.print("An Error Occured!");
            ErrorMessageController er = new ErrorMessageController();
            er.main(new Stage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
         playerListView.getItems().clear();
         playerListView.getItems().addAll(plnames);
         System.out.print("Changed Playerlist!");
        }catch (Exception ex){
            System.err.print("Error!");
        }
    }
}
