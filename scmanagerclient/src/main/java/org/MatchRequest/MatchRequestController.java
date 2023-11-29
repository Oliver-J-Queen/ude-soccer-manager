package org.MatchRequest;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.clientconnection.RequestHelper;
import org.registration.LoginController;
import org.registration.User;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MatchRequestController implements Initializable {
    public static String getChosenRequest, activeTable;
    private double x,y;
    @FXML
    private AnchorPane FriendsOverview;

    @FXML
    private TableView<User> table_req;

    @FXML
    private TableColumn<User, String> col_Username;

    @FXML
    private ImageView profile_image;

    @FXML
    private Label label_active_username;

    @FXML
    private Label label_status, label_lastMatch, label_statisticInfo;

    @FXML
    private Label label_title;

    @FXML
    private TableView<User> table_friends;

    @FXML
    private TableColumn<User, String> col_Matches;

    public void main(Stage primaryStage) throws Exception {
        //creating a new window
        Parent root = FXMLLoader.load(getClass().getResource("MatchRequestOverview.fxml"));
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
    public void initialize(URL location, ResourceBundle resources) {
        //set name and status from active user
        label_active_username.setText(LoginController.activeUsername);
        try {
            String info = RequestHelper.getUserInfo(LoginController.activeUsername);
            String[] splitInfo = info.split("/");
            label_status.setText(splitInfo[1]);
        } catch (InterruptedException e) { e.printStackTrace();
        } catch (ConnectException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }

        col_Username.setCellValueFactory(new PropertyValueFactory<User, String>("USERNAME"));
        col_Matches.setCellValueFactory(new PropertyValueFactory<User, String>("USERNAME"));

        try {
            ArrayList<User> tmpRequests = RequestHelper.getMatchRequest(LoginController.activeUsername);
            if (tmpRequests.get(0).getEMAIL().equals("1")) {
                System.out.println("Keine Anfragen");
            } else {
                System.out.println("Anfragen vorhanden");
                ObservableList<User> RequestList = FXCollections.observableArrayList(tmpRequests);
                table_req.setItems(RequestList);
            }
            String UserInfo = RequestHelper.getUserInfo(LoginController.activeUsername);
            String[] splitUserInfo = UserInfo.split("/");
            int count = Integer.parseInt(splitUserInfo[2]);
            if (count==0){
                System.out.println("Keine Freunde");
            }else {
                ArrayList<User> tmpFriends = RequestHelper.getFriendList(LoginController.activeUsername);
                ObservableList<User> FriendList = FXCollections.observableArrayList(tmpFriends);
                table_friends.setItems(FriendList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //listener for checking where the user clicks inside the table for requests
        table_req.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Method getSelectionModel().selectedItemProperty() found here: https://java.hotexamples.com/examples/javafx.scene.control/ComboBox/getSelectionModel/java-combobox-getselectionmodel-method-examples.htm
                if(table_req.getSelectionModel().getSelectedItem() != null)
                {
                    User tmpSelected = table_req.getSelectionModel().getSelectedItem();
                    String selectedUserUsername = tmpSelected.getUSERNAME();
                    getChosenRequest = selectedUserUsername;
                    System.out.println(selectedUserUsername);
                    table_req.getScene().getWindow().hide();
                    activeTable = "requests";
                    ConfirmationPanelController cl = new ConfirmationPanelController();
                    try {
                        cl.main(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //listener for checking where the user clicks inside the table for friends
        table_friends.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Method getSelectionModel().selectedItemProperty() found here: https://java.hotexamples.com/examples/javafx.scene.control/ComboBox/getSelectionModel/java-combobox-getselectionmodel-method-examples.htm
                if(table_friends.getSelectionModel().getSelectedItem() != null)
                {
                    User tmpSelected = table_friends.getSelectionModel().getSelectedItem();
                    String selectedUserUsername = tmpSelected.getUSERNAME();
                    getChosenRequest = selectedUserUsername;
                    System.out.println(selectedUserUsername);
                    table_friends.getScene().getWindow().hide();
                    activeTable = "friends";
                    ConfirmationPanelController cl = new ConfirmationPanelController();
                    try {
                        cl.main(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        label_lastMatch.setText(ConfirmationPanelController.matchResult);
    }
}
