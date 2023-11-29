package org.mainmenu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.FriendRequests.FriendOverviewController;
import org.clientconnection.RequestHelper;
import org.registration.LoginController;
import org.registration.UserEditPanelController;
import org.stadium.StadiumController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Paul Naebers
 **/


public class UserPanelController {

    @FXML private TextField txt_clubname;

    @FXML private Button home, messages, club, player, friends, profilsettings, signOut, stadium, matches, tournament,     //UserPane
            settings, shop, statistics, tactics,                                    //ClubPane
            welcome_createTeam,                                                     //welcomePane
            btn_changeTeam,                                                         //settingsPane
            save_tactic, saveTeamButton, pointsButton, saveReserveButton;           //TacticPane

    @FXML private Pane tacticpn442,tacticpn541,tacticpn343,tacticpn433,tacticpn352;
    @FXML private TableView teamTable;


    @FXML private Pane pn_home, pn_messages, pn_club, pn_player, pn_tactics, pn_settings, pn_store, pn_stadium, pn_statistics, pn_matches, pn_tournament;
    @FXML private ChoiceBox choicebox_tactic;
    @FXML private ChoiceBox positionDropdown1,positionDropdown2,positionDropdown3,positionDropdown4,positionDropdown5,positionDropdown6,positionDropdown7,positionDropdown8,positionDropdown9,positionDropdown10,positionDropdown11;
    @FXML private ChoiceBox positionDropdown12,positionDropdown13,positionDropdown14,positionDropdown15,positionDropdown16,positionDropdown17,positionDropdown18,positionDropdown19,positionDropdown20;
    @FXML private ImageView field;
    @FXML private Text saveTeamText, pointsText, userName, onlineStatus, saveReserveText, saveFormationText, saveNameText;

    private ArrayList<ChoiceBox> startingeleven = new ArrayList<>();
    private ArrayList<ChoiceBox> reserveplayers = new ArrayList<>();

    private ObservableList <String> choiceboxlist_tactic = FXCollections.observableArrayList("4-4-2", "5-4-1", "3-4-3", "4-3-3", "3-5-2");
    private ObservableList <String> positionDropdownList1 = getPlayers();


    private double x,y;

    public UserPanelController() throws IOException, InterruptedException {
    }

    //changes exit_button_color onMouseEntered
    public void HomeEntered(){ home.setStyle("-fx-background-color: #208af5;"); }
    public void HomeExited(){ home.setStyle("-fx-background-color: #0066cc;"); }
    public void MessagesEntered(){ messages.setStyle("-fx-background-color: #208af5;"); }
    public void MessagesExited(){ messages.setStyle("-fx-background-color: #0066cc;");}
    public void ClubEntered(){ club.setStyle("-fx-background-color: #208af5;"); }
    public void ClubExited(){ club.setStyle("-fx-background-color: #0066cc;");}
    public void PlayerEntered(){ player.setStyle("-fx-background-color: #208af5;"); }
    public void PlayerExited(){ player.setStyle("-fx-background-color: #0066cc;");}
    public void SignOutEntered(){ signOut.setStyle("-fx-background-color: #208af5;"); }
    public void SignOutExited(){ signOut.setStyle("-fx-background-color: #0066cc;");}
    public void FriendsEntered(){ friends.setStyle("-fx-background-color: #208af5;"); }
    public void FriendsExited(){ friends.setStyle("-fx-background-color: #0066cc;");}
    public void ProfilSettingsEntered(){ profilsettings.setStyle("-fx-background-color: #208af5;"); }
    public void ProfilSettingsExited(){ profilsettings.setStyle("-fx-background-color: #0066cc;");}
    public void StadiumEntered(){ stadium.setStyle("-fx-background-color: #208af5;"); }
    public void StadiumExited(){ stadium.setStyle("-fx-background-color: #0066cc;");}
    public void MatchesEntered(){ matches.setStyle("-fx-background-color: #208af5;"); }
    public void MatchesExited(){ matches.setStyle("-fx-background-color: #0066cc;");}
    public void TournamentEntered(){ tournament.setStyle("-fx-background-color: #208af5;"); }
    public void TournamentExited(){ tournament.setStyle("-fx-background-color: #0066cc;");}

    public void addChoiceBox(){
        startingeleven.add(positionDropdown1); startingeleven.add(positionDropdown2); startingeleven.add(positionDropdown3); startingeleven.add(positionDropdown4);
        startingeleven.add(positionDropdown5); startingeleven.add(positionDropdown6); startingeleven.add(positionDropdown7);startingeleven.add(positionDropdown8);
        startingeleven.add(positionDropdown9); startingeleven.add(positionDropdown10); startingeleven.add(positionDropdown11);

        reserveplayers.add(positionDropdown12); reserveplayers.add(positionDropdown13); reserveplayers.add(positionDropdown14);
        reserveplayers.add(positionDropdown15); reserveplayers.add(positionDropdown16); reserveplayers.add(positionDropdown17);
        reserveplayers.add(positionDropdown18); reserveplayers.add(positionDropdown19); reserveplayers.add(positionDropdown20);
    };


    public void main(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(new File("src/main/resources/org/registration/UserPanel.fxml").toURI().toURL());
        Parent root = FXMLLoader.load(getClass().getResource("UserPanelREDUX.fxml"));
        stage.setScene(new Scene(root));
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("SCManager Client - Your Personal Sports Club Manager Application");
        stage.setResizable(false);
        stage.show();
        /*
        root.setOnMouseDragged(event -> {
            x = event.getScreenX();
            y = event.getScreenY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

         */
    }

    @FXML
    public void initialize() throws IOException, InterruptedException {

        StadiumController.createStadium();

        pn_home.toFront();
        pointsText.setText(RequestHelper.getPoints(LoginController.activeUsername) + " SEPs");
        userName.setText(LoginController.activeUsername);
        choicebox_tactic.setValue("4-4-2");
        //choicebox_tactic.setValue(RequestHelper.getTactics(LoginController.activeUsername));
        choicebox_tactic.setItems(choiceboxlist_tactic);

        //getChoice(RequestHelper.getTactics(LoginController.activeUsername));

        pn_store.setVisible(false);
        pn_settings.setVisible(false);
        tacticpn352.setVisible(false);
        tacticpn541.setVisible(false);
        tacticpn343.setVisible(false);
        tacticpn433.setVisible(false);

        this.addChoiceBox();

        String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");


        for(ChoiceBox box : startingeleven){
            //box.setItems(positionDropdownList1);
            box.setItems(getPlayers(box));
        }
        for(int j = 0; j < reserveplayers.size(); j++){
            reserveplayers.get(j).setItems(positionDropdownList1);
        }

    }

    @FXML
    public void handleButtonAction(ActionEvent event) throws  Exception {
        if(event.getSource() == home){
            pn_home.toFront();
        }
        else if(event.getSource() == messages){
            pn_messages.toFront();
        }
        else if(event.getSource() == stadium){
            pn_stadium.toFront();
        }
        else if(event.getSource() == club){
            pn_club.toFront();
        }
        else if(event.getSource() == player){
            pn_player.toFront();
        }
        else if (event.getSource() == matches){
            pn_matches.toFront();
        }
        else if(event.getSource() == signOut){
            System.out.println("signOut pressed");
            RequestHelper.logout(LoginController.activeUsername);
            signOut.getScene().getWindow().hide();
            LoginController lg = new LoginController();
            lg.main(new Stage());
        }
        else if(event.getSource() == tactics){
            pn_tactics.setVisible(true);

            pn_statistics.setVisible(false);
            pn_store.setVisible(false);
            pn_settings.setVisible(false);

            tacticpn442.setVisible(true);
            tacticpn352.setVisible(false);
            tacticpn541.setVisible(false);
            tacticpn343.setVisible(false);
            tacticpn433.setVisible(false);
        }
        else if(event.getSource() == shop){
            pn_settings.setVisible(false);
            pn_tactics.setVisible(false);
            pn_statistics.setVisible(false);
            pn_store.setVisible(true);
        }
        else if(event.getSource() == settings){
            pn_settings.setVisible(true);
            pn_tactics.setVisible(false);
            pn_store.setVisible(false);
            pn_statistics.setVisible(false);
        }
        else if(event.getSource() == statistics){
            pn_settings.setVisible(false);
            pn_tactics.setVisible(false);
            pn_store.setVisible(false);
            pn_statistics.setVisible(true);
        }
        else if(event.getSource() == save_tactic){
            this.getChoice(choicebox_tactic);
            String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
            String tmp = RequestHelper.saveFormation(choicebox_tactic.getValue().toString(), ID[3]);

            if (tmp.equalsIgnoreCase("success")) {
                for(ChoiceBox box : startingeleven){
                    //box.setItems(positionDropdownList1);
                    box.setItems(getPlayers(box));
                }
                Runnable visual = () -> {
                    saveFormationText.setText("Formation saved");
                };
                Thread thread = new Thread(visual);
                Runnable scraping = () -> {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    saveFormationText.setText("");
                };
                Thread thread1 = new Thread(scraping);
                thread.start();
                thread1.start();
            }
        }

        /*
        else if(event.getSource() == btn_createTeam){
            String tmp = RequestHelper.createTeam(txt_clubname.getText(), LoginController.activeUsername);
            if (tmp.equals("success")) {
                System.out.print("Team erstellt");

            }
        }
        */
        else if(event.getSource() == profilsettings){
            UserEditPanelController tmp = new UserEditPanelController();
            tmp.main(new Stage());
        }
        else if (event.getSource() == btn_changeTeam) {
            String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
            String tmp = RequestHelper.changeTeamName(txt_clubname.getText(), ID[3]);
            if (tmp.equalsIgnoreCase("success")) {
                saveNameText.setText("Teamname has been changed succesfully to " + txt_clubname.getText());
            }
        }
        else if(event.getSource() == saveTeamButton){
            if(occupiedPositions(startingeleven) == false){
                saveTeamText.setText("Error");
            }
            else {
                String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                if(RequestHelper.saveSetup(startingeleven, ID[3]).equalsIgnoreCase("success")){
                    RequestHelper.saveSetup(startingeleven, ID[3]);
                    saveTeamText.setText("Positions saved!");
                }
                else{
                    saveTeamText.setText("Database error!");
                }
            }
        }
        else if(event.getSource() == saveReserveButton){
            if(occupiedPositions(reserveplayers) == false){
               saveReserveText.setText("Error");
            }
            else {
                saveReserveText.setText("Positions saved!");
                String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");

                if(RequestHelper.saveSetupReserve(reserveplayers, ID[3]).equalsIgnoreCase("success")){
                    RequestHelper.saveSetupReserve(reserveplayers, ID[3]);
                    saveReserveText.setText("Positions saved!");
                }
                else{
                    saveReserveText.setText("Database error!");
                }
            }
        }
        else if(event.getSource() == friends){
            FriendOverviewController f = new FriendOverviewController();
            f.main(new Stage());
            matches.getScene().getWindow().hide();
        }
        else if(event.getSource()==tournament){
            pn_tournament.toFront();
        }
    }

    private void getChoice(ChoiceBox<String> input){
        String current_tactic = input.getValue();
        if(current_tactic.equalsIgnoreCase("4-4-2")){
            tacticpn442.setVisible(true);

            tacticpn352.setVisible(false);
            tacticpn541.setVisible(false);
            tacticpn343.setVisible(false);
            tacticpn433.setVisible(false);
        }
        else if(current_tactic.equalsIgnoreCase("5-4-1")){
            tacticpn541.setVisible(true);

            tacticpn352.setVisible(false);
            tacticpn442.setVisible(false);
            tacticpn343.setVisible(false);
            tacticpn433.setVisible(false);
        }
        else if(current_tactic.equalsIgnoreCase("3-4-3")){
            tacticpn343.setVisible(true);

            tacticpn352.setVisible(false);
            tacticpn442.setVisible(false);
            tacticpn541.setVisible(false);
            tacticpn433.setVisible(false);
        }
        else if(current_tactic.equalsIgnoreCase("4-3-3")){
            tacticpn433.setVisible(true);

            tacticpn352.setVisible(false);
            tacticpn442.setVisible(false);
            tacticpn541.setVisible(false);
            tacticpn343.setVisible(false);
        }
        else{
            tacticpn352.setVisible(true);

            tacticpn433.setVisible(false);
            tacticpn442.setVisible(false);
            tacticpn541.setVisible(false);
            tacticpn343.setVisible(false);
        }
    }

    private void getChoice(String input){
        String current_tactic = input;
        if(current_tactic.equalsIgnoreCase("4-4-2")){
            tacticpn442.setVisible(true);

            tacticpn352.setVisible(false);
            tacticpn541.setVisible(false);
            tacticpn343.setVisible(false);
            tacticpn433.setVisible(false);
        }
        else if(current_tactic.equalsIgnoreCase("5-4-1")){
            tacticpn541.setVisible(true);

            tacticpn352.setVisible(false);
            tacticpn442.setVisible(false);
            tacticpn343.setVisible(false);
            tacticpn433.setVisible(false);
        }
        else if(current_tactic.equalsIgnoreCase("3-4-3")){
            tacticpn343.setVisible(true);

            tacticpn352.setVisible(false);
            tacticpn442.setVisible(false);
            tacticpn541.setVisible(false);
            tacticpn433.setVisible(false);
        }
        else if(current_tactic.equalsIgnoreCase("4-3-3")){
            tacticpn433.setVisible(true);

            tacticpn352.setVisible(false);
            tacticpn442.setVisible(false);
            tacticpn541.setVisible(false);
            tacticpn343.setVisible(false);
        }
        else{
            tacticpn352.setVisible(true);

            tacticpn433.setVisible(false);
            tacticpn442.setVisible(false);
            tacticpn541.setVisible(false);
            tacticpn343.setVisible(false);
        }
    }

    public boolean occupiedPositions(ArrayList<ChoiceBox> input) {
        Set<String> set = new HashSet<>();
        for (ChoiceBox b:input){
            if(b.getValue()!=null){
                set.add(b.getValue().toString());
            }
        }
        return set.size()==input.size();
    }


    public ObservableList <String> getPlayers(ChoiceBox box) throws IOException, InterruptedException {
        //"4-4-2", "5-4-1", "3-4-3", "4-3-3", "3-5-2" Abwehr-Mittelfeld-Sturm
        //positionDropdown1,positionDropdown2,positionDropdown3,positionDropdown4,positionDropdown5,positionDropdown6,positionDropdown7,positionDropdown8,positionDropdown9,positionDropdown10,positionDropdown11

        /**
         * 4-4-2
         */
        if(choicebox_tactic.getValue().toString().equals("4-4-2")){
            if((box.getId().equalsIgnoreCase("positionDropdown11"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Torwart");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;

            }else if(box.getId().equalsIgnoreCase("positionDropdown7") ||
                    (box.getId().equalsIgnoreCase("positionDropdown8")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown9")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown10"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Abwehr");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;
            }
            else if(box.getId().equalsIgnoreCase("positionDropdown3") ||
                    (box.getId().equalsIgnoreCase("positionDropdown4")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown5")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown6"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Mittelfeld");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;
            }
            else if(box.getId().equalsIgnoreCase("positionDropdown1") ||
                    (box.getId().equalsIgnoreCase("positionDropdown2"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Angriff");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;
            }

            /**
             * 5-4-1
             */
        }else if(choicebox_tactic.getValue().toString().equals("5-4-1")){
                if((box.getId().equalsIgnoreCase("positionDropdown11"))){
                    ObservableList<String> returnList = FXCollections.observableArrayList();
                    try{
                        String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                        ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Torwart");
                        for (String string : tmp){
                            returnList.add(string);
                        }
                    }
                    catch (NullPointerException e){
                        returnList.add("no player found");
                    }
                    return returnList;

                }else if(box.getId().equalsIgnoreCase("positionDropdown7") ||
                        (box.getId().equalsIgnoreCase("positionDropdown8")) ||
                        (box.getId().equalsIgnoreCase("positionDropdown9")) ||
                        (box.getId().equalsIgnoreCase("positionDropdown10")) ||
                        (box.getId().equalsIgnoreCase("positionDropdown6"))){
                    ObservableList<String> returnList = FXCollections.observableArrayList();
                    try{
                        String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                        ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Abwehr");
                        for (String string : tmp){
                            returnList.add(string);
                        }
                    }
                    catch (NullPointerException e){
                        returnList.add("no player found");
                    }
                    return returnList;
                }
                else if(box.getId().equalsIgnoreCase("positionDropdown1") ||
                        (box.getId().equalsIgnoreCase("positionDropdown4")) ||
                        (box.getId().equalsIgnoreCase("positionDropdown5")) ||
                        (box.getId().equalsIgnoreCase("positionDropdown3"))){
                    ObservableList<String> returnList = FXCollections.observableArrayList();
                    try{
                        String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                        ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Mittelfeld");
                        for (String string : tmp){
                            returnList.add(string);
                        }
                    }
                    catch (NullPointerException e){
                        returnList.add("no player found");
                    }
                    return returnList;
                }
                else if(box.getId().equalsIgnoreCase("positionDropdown2")) {
                    ObservableList<String> returnList = FXCollections.observableArrayList();
                    try {
                        String[] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                        ArrayList<String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Angriff");
                        for (String string : tmp) {
                            returnList.add(string);
                        }
                    } catch (NullPointerException e) {
                        returnList.add("no player found");
                    }
                    return returnList;
                }
        }

        /**
         * 3-4-3
         */
        else if(choicebox_tactic.getValue().toString().equals("3-4-3")){
            if((box.getId().equalsIgnoreCase("positionDropdown11"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Torwart");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;

            }else if(box.getId().equalsIgnoreCase("positionDropdown8") ||
                    (box.getId().equalsIgnoreCase("positionDropdown9")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown10"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Abwehr");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;
            }
            else if(box.getId().equalsIgnoreCase("positionDropdown4") ||
                    (box.getId().equalsIgnoreCase("positionDropdown5")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown6")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown7"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Mittelfeld");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;
            }
            else if(box.getId().equalsIgnoreCase("positionDropdown1") ||
                    (box.getId().equalsIgnoreCase("positionDropdown2")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown3"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try {
                    String[] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList<String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Angriff");
                    for (String string : tmp) {
                        returnList.add(string);
                    }
                } catch (NullPointerException e) {
                    returnList.add("no player found");
                }
                return returnList;
            }

            /**
             * 4-3-3
             */
        }else if(choicebox_tactic.getValue().toString().equals("4-3-3")){
            if((box.getId().equalsIgnoreCase("positionDropdown11"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Torwart");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;

            }else if(box.getId().equalsIgnoreCase("positionDropdown7") ||
                    (box.getId().equalsIgnoreCase("positionDropdown8")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown9")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown10"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Abwehr");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;
            }
            else if(box.getId().equalsIgnoreCase("positionDropdown4") ||
                    (box.getId().equalsIgnoreCase("positionDropdown5")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown6"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Mittelfeld");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;
            }
            else if(box.getId().equalsIgnoreCase("positionDropdown1") ||
                    (box.getId().equalsIgnoreCase("positionDropdown2")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown3"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try {
                    String[] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList<String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Angriff");
                    for (String string : tmp) {
                        returnList.add(string);
                    }
                } catch (NullPointerException e) {
                    returnList.add("no player found");
                }
                return returnList;
            }

            /**
             * 3-5-2
             */
        }else if(choicebox_tactic.getValue().toString().equals("3-5-2")){
            if((box.getId().equalsIgnoreCase("positionDropdown11"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Torwart");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;

            }else if(box.getId().equalsIgnoreCase("positionDropdown8") ||
                    (box.getId().equalsIgnoreCase("positionDropdown9")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown10"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Abwehr");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;
            }
            else if(box.getId().equalsIgnoreCase("positionDropdown4") ||
                    (box.getId().equalsIgnoreCase("positionDropdown5")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown6")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown7")) ||
                    (box.getId().equalsIgnoreCase("positionDropdown3"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try{
                    String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList <String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Mittelfeld");
                    for (String string : tmp){
                        returnList.add(string);
                    }
                }
                catch (NullPointerException e){
                    returnList.add("no player found");
                }
                return returnList;
            }
            else if(box.getId().equalsIgnoreCase("positionDropdown1") ||
                    (box.getId().equalsIgnoreCase("positionDropdown2"))){
                ObservableList<String> returnList = FXCollections.observableArrayList();
                try {
                    String[] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
                    ArrayList<String> tmp = RequestHelper.getPlayerAtPosition(ID[3], "Angriff");
                    for (String string : tmp) {
                        returnList.add(string);
                    }
                } catch (NullPointerException e) {
                    returnList.add("no player found");
                }
                return returnList;
            }
        }

        ObservableList<String> ret = FXCollections.observableArrayList();
        try{
            String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
            ArrayList <String> tmp = RequestHelper.getTeam(ID[3]);
            for (String string : tmp){
                ret.add(string);
            }
        }
        catch (NullPointerException e){
           ret.add("no player found");
        }
        return ret;
    }

    public ObservableList <String> getPlayers() throws IOException, InterruptedException {
        ObservableList<String> ret = FXCollections.observableArrayList();
        try{
            String [] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
            ArrayList <String> tmp = RequestHelper.getTeam(ID[3]);
            for (String string : tmp){
                ret.add(string);
            }
        }
        catch (NullPointerException e){
            ret.add("no player found");
        }
        return ret;
    }


    public void showPoints(ActionEvent e) throws IOException, InterruptedException {
        if(pointsText.equals("******")) {
            pointsText.setText(RequestHelper.getPoints(LoginController.activeUsername));
            pointsButton.setText("Hide Points");
        }
        else{
            pointsText.setText("******");
            pointsButton.setText("Show Points");
        }

    }

}