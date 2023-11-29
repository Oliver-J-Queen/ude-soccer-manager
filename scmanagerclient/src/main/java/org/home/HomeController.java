package org.home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.clientconnection.RequestHelper;
import org.mainmenu.UserPanelController;
import org.registration.LoginController;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * @author Paul Naebers
 **/

public class HomeController {

    @FXML private Button save;
    @FXML private Text showmail, showid, showpoints, showname;
    @FXML private ImageView dragon, police, giraf;
    @FXML private ChoiceBox pictures;
    @FXML private Text output;
    private ObservableList<String> iconlist = FXCollections.observableArrayList("Giraf", "Dragon", "Police");


    @FXML
    public void initialize() throws IOException, InterruptedException {

        String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");

        showname.setText(LoginController.activeUsername);
        showid.setText(ID[3]);
        showmail.setText(ID[0]);
        showpoints.setText(RequestHelper.getPoints(LoginController.activeUsername));

        pictures.setValue("Dragon");
        pictures.setItems(iconlist);

        dragon.setVisible(false);
        police.setVisible(false);
        giraf.setVisible(false);

        this.setVisible();
    }

    @FXML
    public void handleButtonAction(ActionEvent event) throws Exception {
        if(event.getSource() == save){
            this.getChoice(pictures);

            String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
            String tmp = RequestHelper.saveIcon(pictures.getValue().toString(), ID[3]);
            if (tmp.equalsIgnoreCase("success")) {
                output.setText("Icon saved");
            }
        }
    }

    private void getChoice(ChoiceBox<String> input) {
        String current_icon = input.getValue();
        if(current_icon.equalsIgnoreCase("Dragon")){
            dragon.setVisible(true);
            giraf.setVisible(false);
            police.setVisible(false);
        }
        else if(current_icon.equalsIgnoreCase("Police")){
            dragon.setVisible(false);
            giraf.setVisible(false);
            police.setVisible(true);
        }
        else if(current_icon.equalsIgnoreCase("Giraf")){
            dragon.setVisible(false);
            giraf.setVisible(true);
            police.setVisible(false);
        }
    }

    public void setVisible() throws IOException, InterruptedException {
        String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
        if(RequestHelper.getIcon(ID[3]).equalsIgnoreCase("Dragon")){
            dragon.setVisible(true);
        }
        else if(RequestHelper.getIcon(ID[3]).equalsIgnoreCase("Giraf")){
            giraf.setVisible(true);
        }
        else if(RequestHelper.getIcon(ID[3]).equalsIgnoreCase("Police")){
            police.setVisible(true);
        }
    }
}
