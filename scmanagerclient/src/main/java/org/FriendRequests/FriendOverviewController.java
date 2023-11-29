package org.FriendRequests;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.clientconnection.RequestHelper;
import org.mainmenu.UserPanelController;
import org.registration.LoginController;
import org.registration.User;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FriendOverviewController implements Initializable{

    private double x,y;
    public static String getChosenFriend;

    @FXML
    private AnchorPane FriendsOverview;
    @FXML
    private ImageView profile_image;
    @FXML
    private Label label_title;
    @FXML
    private Label label_active_username;
    @FXML
    private Label label_status;
    @FXML
    private TextField searchfield;
    @FXML
    private Button search_Button, request_Button, backtomain_Button;

    @FXML private TableView<User> table_friendsOverview;
    @FXML private TableColumn<User, String> col_Status;
    @FXML private TableColumn<User, String> col_Email;
    @FXML private TableColumn<User, String> col_Username;


    public void main(Stage primaryStage) throws Exception {
        //creating a new window
        Parent root = FXMLLoader.load(getClass().getResource("FriendOverview.fxml"));
        primaryStage.setScene(new Scene(root));
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("SCManager Client");
        primaryStage.show();


        //window is draggable
        /*
        root.setOnMouseDragged(event -> {
            x = event.getScreenX();
            y = event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

         */
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        //set name and status from active user
        label_active_username.setText(LoginController.activeUsername);
        try {
            String info = RequestHelper.getUserInfo(LoginController.activeUsername);
            String[] splitInfo = info.split("/");
            label_status.setText(splitInfo[1]);
        } catch (InterruptedException e) { e.printStackTrace();
        } catch (ConnectException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }

        //set cell value <User>
        col_Username.setCellValueFactory(new PropertyValueFactory<User, String>("USERNAME"));
        col_Email.setCellValueFactory(new PropertyValueFactory<User, String>("EMAIL"));
        col_Status.setCellValueFactory(new PropertyValueFactory<User, String>("STATUS"));

        try {
            //check if active user has friends
            String userI = RequestHelper.getUserInfo(LoginController.activeUsername);
            String[] splituserI = userI.split("/");
            int count = Integer.parseInt(splituserI[2]);
            System.out.println(count);
            if(count==0){
                //No friends found
                System.out.println("no Friends");
            }else {
                //Friends found. Get all friends in an ArrayList<User>
                ArrayList<User> friendList = RequestHelper.getFriendList(LoginController.activeUsername);
                ObservableList<User> friends = FXCollections.observableArrayList(friendList);
                FilteredList<User> filteredFriends = new FilteredList<>(friends, b -> true);

                //set searchfield
                searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredFriends.setPredicate(friend -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (friend.getUSERNAME().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (friend.getEMAIL().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    });
                });

                //put friends in the table
                SortedList<User> sortedFriends = new SortedList<>(filteredFriends);
                sortedFriends.comparatorProperty().bind(table_friendsOverview.comparatorProperty());
                table_friendsOverview.setItems(sortedFriends);
            }
            } catch(InterruptedException e){ e.printStackTrace();
            } catch(ConnectException e){ e.printStackTrace();
            } catch(IOException e){ e.printStackTrace();
        }
        //listener for checking where the user clicks inside the table
        table_friendsOverview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Method getSelectionModel().selectedItemProperty() found here: https://java.hotexamples.com/examples/javafx.scene.control/ComboBox/getSelectionModel/java-combobox-getselectionmodel-method-examples.htm
                if(table_friendsOverview.getSelectionModel().getSelectedItem() != null)
                {
                    User tmpSelected = table_friendsOverview.getSelectionModel().getSelectedItem();
                    String selectedUserUsername = tmpSelected.getUSERNAME();
                    getChosenFriend = selectedUserUsername;
                    System.out.println(selectedUserUsername);
                    request_Button.getScene().getWindow().hide();
                    DeleteFriendController df = new DeleteFriendController();
                    try {
                        df.main(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    public void searchForNewFriends(ActionEvent event) throws Exception {
        //open Search User Overview
        SearchUserController tp = new SearchUserController();
        search_Button.getScene().getWindow().hide();
        tp.main(new Stage());
    }

    @FXML
    public void openRequests(MouseEvent event) throws Exception {
        //open Request Overview
        RequestOverviewController tp = new RequestOverviewController();
        tp.main(new Stage());
        search_Button.getScene().getWindow().hide();
    }

    @FXML
    void backtomain(MouseEvent event) throws Exception {
        //close Friend Overview and go back to User Panel
        backtomain_Button.getScene().getWindow().hide();
        UserPanelController tp = new UserPanelController();
        tp.main(new Stage());
    }
}
