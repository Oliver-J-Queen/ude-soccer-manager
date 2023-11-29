package org.FriendRequests;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.clientconnection.RequestHelper;
import org.registration.LoginController;
import org.registration.User;
import org.FriendRequests.FriendRequest;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RequestOverviewController implements Initializable {

    private double x,y;
    public static String selectedRequest;

    @FXML
    private AnchorPane FriendsOverview;

    @FXML
    private TableView<FriendRequest> table_req;

    @FXML
    private TableColumn<FriendRequest, String> col_Username;

    @FXML
    private ImageView profile_image;

    @FXML
    private Label label_active_username;

    @FXML
    private Label label_status;

    @FXML
    private Label label_title;

    @FXML
    private Button back_Button;

    @FXML
    void backClicked(ActionEvent event) throws Exception {
        FriendOverviewController tp = new FriendOverviewController();
        tp.main(new Stage());
        back_Button.getScene().getWindow().hide();
    }

    public void main(Stage primaryStage) throws Exception {
        //creating a new RequestOverviewPanel
        Parent root = FXMLLoader.load(getClass().getResource("RequestOverview.fxml"));
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
    public void initialize(URL location, ResourceBundle resources) {
        label_active_username.setText(LoginController.activeUsername);
        try {
            //User Info settings
            String info = RequestHelper.getUserInfo(LoginController.activeUsername);
            String[] splitInfo = info.split("/");
            label_status.setText(splitInfo[1]);
             //Exceptions
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        col_Username.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("name"));
        try {
            //Get open Requests from Server/RequestHandler/GetRequestHandler/Database by username
            ArrayList<FriendRequest> req = RequestHelper.getOpenRequests(LoginController.activeUsername);
            if(req.get(0).getName().equals("/")){
                 //If list is empty
                System.out.println("Keine Requests");
            }else {
                //List has open Requests
                //ArrayList turns into ObservableList
                ObservableList<FriendRequest> reqList = FXCollections.observableList(req);
                //So that table can list the Requests
                table_req.setItems(reqList);
            }
            //Exceptions
        } catch(InterruptedException e){ e.printStackTrace();
        } catch(ConnectException e){ e.printStackTrace();
        } catch(IOException e){ e.printStackTrace(); }


        //listener for selected element
        //Method getSelectionModel().selectedItemProperty() found here: https://java.hotexamples.com/examples/javafx.scene.control/ComboBox/getSelectionModel/java-combobox-getselectionmodel-method-examples.html
        table_req.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableUser, Object oldUser, Object newUser) {
                //If, damit nicht am Start crasht wenn nichts ausgewählt ist, fehlte am Anfang
                if(table_req.getSelectionModel().getSelectedItem() != null)
                {
                    FriendRequest tmpSelectedUser = table_req.getSelectionModel().getSelectedItem();
                    //Get Username from selected User from selected Row
                    String selectedUserUsername = tmpSelectedUser.getName();
                    //Set to static String to access it in RequestAnswerViewController
                    selectedRequest = selectedUserUsername;
                    //following Window
                    back_Button.getScene().getWindow().hide();
                    RequestAnswerViewController tp = new RequestAnswerViewController();
                    try {
                        tp.main(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
