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
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchUserController implements Initializable {
    public static String sendRequestString;
    private double x,y;

    @FXML
    private ImageView profile_image;

    @FXML
    private Label label_title;

    @FXML
    private Label label_active_username;

    @FXML
    private AnchorPane SearchUser;

    @FXML
    private TextField searchfield;

    @FXML
    private Label label_status;
    @FXML
    private Button back_button;

    @FXML private TableView<User> table_UserOverview;
    @FXML private TableColumn<User, String> col_Status;
    @FXML private TableColumn<User, String> col_Email;
    @FXML private TableColumn<User, String> col_Username;

    public void main(Stage primaryStage) throws Exception {
        //creating a new window
        Parent root = FXMLLoader.load(getClass().getResource("SearchUser.fxml"));
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
    public void initialize (URL location, ResourceBundle resources){
        label_active_username.setText(LoginController.activeUsername);
        try {
            String info = RequestHelper.getUserInfo(LoginController.activeUsername);
            String[] splitInfo = info.split("/");
            label_status.setText(splitInfo[1]);
        } catch (InterruptedException e) { e.printStackTrace();
        } catch (ConnectException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }

        col_Username.setCellValueFactory(new PropertyValueFactory<User, String>("USERNAME"));
        col_Email.setCellValueFactory(new PropertyValueFactory<User, String>("EMAIL"));
        col_Status.setCellValueFactory(new PropertyValueFactory<User, String>("STATUS"));

        try {
                ArrayList<User> allUserList = RequestHelper.getAllUser(LoginController.activeUsername);
                if(allUserList.get(0).getUSERNAME().equals("/")){
                    System.out.println("No other Users");
                }else {
                    ObservableList<User> allUser = FXCollections.observableArrayList(allUserList);
                    FilteredList<User> filteredUser = new FilteredList<>(allUser, b -> true);

                    searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
                        filteredUser.setPredicate(user -> {
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            String lowerCaseFilter = newValue.toLowerCase();

                            if (user.getUSERNAME().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            } else if (user.getEMAIL().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            return false;
                        });
                    });

                    SortedList<User> sortedUser = new SortedList<>(filteredUser);
                    sortedUser.comparatorProperty().bind(table_UserOverview.comparatorProperty());
                    table_UserOverview.setItems(sortedUser);

                }
        } catch(InterruptedException e){ e.printStackTrace();
        } catch(ConnectException e){ e.printStackTrace();
        } catch(IOException e){ e.printStackTrace(); }

        //Send Friend Request

        //listener ausgewähltes element
        table_UserOverview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
               //IF, damit nicht am start crasht wenn nichts ausgewählt ist
                if(table_UserOverview.getSelectionModel().getSelectedItem() != null)
                {
                    //Method getSelectionModel().selectedItemProperty() found here: https://java.hotexamples.com/examples/javafx.scene.control/ComboBox/getSelectionModel/java-combobox-getselectionmodel-method-examples.htm
                    User tmpSelected = table_UserOverview.getSelectionModel().getSelectedItem();
                    String selectedUserUsername = tmpSelected.getUSERNAME();
                    sendRequestString = selectedUserUsername;
                    System.out.println(selectedUserUsername);
                    back_button.getScene().getWindow().hide();
                    SendRequestViewController srvr = new SendRequestViewController();
                    try {
                        srvr.main(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @FXML
    public void backToOverview(ActionEvent event) throws Exception {
        FriendOverviewController tp = new FriendOverviewController();
        back_button.getScene().getWindow().hide();
        tp.main(new Stage());
    }
}
