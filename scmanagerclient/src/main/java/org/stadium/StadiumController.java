package org.stadium;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.clientconnection.RequestHelper;
import org.mainmenu.UserPanelController;
import org.registration.LoginController;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author: Paul Naebers
 */

public class StadiumController {

    String imbis, parking, capacity;

    @FXML
    private Button btn_stadiumname, btn_upgradeParking, btn_upgradeCapacity, btn_upgradeImbis, btn_refresh;
    @FXML
    private TextField txt_stadiumname;
    @FXML
    private Text stadiumname, txt_changelog, txt_imbis, txt_parking, txt_capacity;

    public void clearStadiumName(){ txt_stadiumname.setText("");}
    public void mouseEnteredStadiumName(){ txt_stadiumname.setStyle("-fx-border-color: #198cff; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 3;"); }
    public void mouseExitedStadiumName(){ txt_stadiumname.setStyle("-fx-border-color: #0066cc; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 2;"); }

    public void SaveEntered(){ btn_stadiumname.setStyle("-fx-background-color: #208af5;"); }
    public void SaveExited(){ btn_stadiumname.setStyle("-fx-background-color: #0066cc;"); }

    public void ParkingEntered(){ btn_upgradeParking.setStyle("-fx-background-color: #208af5;"); }
    public void ParkingExited(){ btn_upgradeParking.setStyle("-fx-background-color: #0066cc;"); }

    public void CapacityEntered(){ btn_upgradeCapacity.setStyle("-fx-background-color: #208af5;"); }
    public void CapacityExited(){ btn_upgradeCapacity.setStyle("-fx-background-color: #0066cc;"); }

    public void ImbisEntered(){ btn_upgradeImbis.setStyle("-fx-background-color: #208af5;"); }
    public void ImbisExited(){ btn_upgradeImbis.setStyle("-fx-background-color: #0066cc;"); }

    public StadiumController() throws IOException, InterruptedException {
    }

    @FXML
    public void initialize() throws IOException, InterruptedException {

        stadiumname.setText(RequestHelper.getStadiumName(LoginController.activeUsername));
        imbis = RequestHelper.getStadiumImbis(LoginController.activeUsername);
        parking = RequestHelper.getStadiumParking(LoginController.activeUsername);
        capacity = RequestHelper.getStadiumCapacity(LoginController.activeUsername);

        txt_imbis.setText("Status: " +imbis);
        txt_capacity.setText("Status: " +capacity);
        txt_parking.setText("Status: " +parking);

        if(RequestHelper.getStadiumName(LoginController.activeUsername).equalsIgnoreCase("")){
            stadiumname.setText("Default Stadium");
            txt_imbis.setText("Status: small");
            txt_capacity.setText("Status: 5000");
            txt_parking.setText("Status: small");
        }
        else if(RequestHelper.getStadiumName(LoginController.activeUsername).equalsIgnoreCase("default")){
            stadiumname.setText("Default Stadium");

        }
    }

    @FXML
    public void handleButtonAction(ActionEvent event) throws Exception {
        if (event.getSource() == btn_stadiumname) {
            System.out.println("BTN stadiumname pressed");
            this.checkAvailable(txt_stadiumname.getText());
        }
        else if(event.getSource() == btn_upgradeCapacity){
            if(capacity.equalsIgnoreCase("5000")){
                if(Integer.parseInt(RequestHelper.getPoints(LoginController.activeUsername)) < 500){
                    txt_capacity.setText("You do not have enough SEP");
                }
                else{
                    RequestHelper.upgradeCapacity1(LoginController.activeUsername, "25000", 500);
                    txt_capacity.setText("Status: " + RequestHelper.getStadiumCapacity(LoginController.activeUsername));
                }
            }
            else if(capacity.equalsIgnoreCase("25000")){
                if(Integer.parseInt(RequestHelper.getPoints(LoginController.activeUsername)) < 1000){
                    txt_capacity.setText("You do not have enough SEP");
                }
                else {
                    RequestHelper.upgradeCapacity2(LoginController.activeUsername, "50000", 1000);
                    txt_capacity.setText("Status: " + RequestHelper.getStadiumCapacity(LoginController.activeUsername));
                }
            }
            else if(capacity.equalsIgnoreCase("50000")){
                if(Integer.parseInt(RequestHelper.getPoints(LoginController.activeUsername)) < 1500){
                    txt_capacity.setText("You do not have enough SEP");
                }
                else {
                    RequestHelper.upgradeCapacity3(LoginController.activeUsername, "65000", 1500);
                    txt_capacity.setText("Status: " + RequestHelper.getStadiumCapacity(LoginController.activeUsername));
                }
            }
            else if(capacity.equalsIgnoreCase("65000")){
                txt_capacity.setText("Status: " + "maximum");
            }
            else{
                txt_capacity.setText("You need to refresh first");
            }
        }
        else if (event.getSource() == btn_upgradeParking) {
            System.out.println("BTN upgradeParking pressed");
            if (parking.equalsIgnoreCase("small")) {
                if(Integer.parseInt(RequestHelper.getPoints(LoginController.activeUsername)) < 500){
                    txt_parking.setText("You do not have enough SEP");
                }
                else{
                    RequestHelper.upgradeParkingMedium(LoginController.activeUsername, "medium", 500);
                    txt_parking.setText("Status: " + RequestHelper.getStadiumParking(LoginController.activeUsername));
                }
            }
            else if (parking.equalsIgnoreCase("medium")) {
                if(Integer.parseInt(RequestHelper.getPoints(LoginController.activeUsername)) < 1000){
                    txt_parking.setText("You do not have enough SEP");
                }
                else{
                    RequestHelper.upgradeParkingBig(LoginController.activeUsername, "big", 1000);
                    txt_parking.setText("Status: " + RequestHelper.getStadiumParking(LoginController.activeUsername));
                }
            }
            else if (parking.equalsIgnoreCase("big")) {
                if(Integer.parseInt(RequestHelper.getPoints(LoginController.activeUsername)) < 1500){
                    txt_parking.setText("You do not have enough SEP");
                }
                else{
                    RequestHelper.upgradeParkingEnlarged(LoginController.activeUsername, "enlarged", 1500);
                    txt_parking.setText("Status: " + RequestHelper.getStadiumParking(LoginController.activeUsername));
                }
            }
            else if (parking.equalsIgnoreCase("enlarged")) {
                txt_parking.setText("Status: " + "maximum");
            }
            else {
                txt_parking.setText("You need to refresh first");
            }

        }
        else if (event.getSource() == btn_upgradeImbis) {
            System.out.println("BTN upgradeParking pressed");
            if(imbis.equalsIgnoreCase("small") ){
                if(Integer.parseInt(RequestHelper.getPoints(LoginController.activeUsername)) < 500){
                    txt_imbis.setText("You do not have enough SEP");
                }
                else{
                    RequestHelper.upgradeImbisMedium(LoginController.activeUsername, "medium", 500);
                    txt_imbis.setText("Status: " + RequestHelper.getStadiumImbis(LoginController.activeUsername));
                }
            }
            else if(imbis.equalsIgnoreCase("medium")){
                if(Integer.parseInt(RequestHelper.getPoints(LoginController.activeUsername)) < 1000){
                    txt_imbis.setText("You do not have enough SEP");
                }
                else{
                    RequestHelper.upgradeImbisBig(LoginController.activeUsername, "big", 1000);
                    txt_imbis.setText("Status: " + RequestHelper.getStadiumImbis(LoginController.activeUsername));
                }
            }
            else if(imbis.equalsIgnoreCase("big")){
                if(Integer.parseInt(RequestHelper.getPoints(LoginController.activeUsername)) < 1500){
                    txt_imbis.setText("You do not have enough SEP");
                }
                else{
                    RequestHelper.upgradeImbisEnlarged(LoginController.activeUsername, "enlarged", 1500);
                    txt_imbis.setText("Status: " + RequestHelper.getStadiumImbis(LoginController.activeUsername));
                }
            }
            else if(imbis.equalsIgnoreCase("enlarged")){
                txt_imbis.setText("Status: " + "maximum");
            }
            else if(RequestHelper.getStadiumImbis(LoginController.activeUsername).equalsIgnoreCase("error")){
                txt_imbis.setText("You do not have enough SEP");
            }
            else{
                txt_imbis.setText("You need to refresh first");
            }
        }
        else if(event.getSource() == btn_refresh){
            btn_refresh.getScene().getWindow().hide();
            UserPanelController u = new UserPanelController();
            u.main(new Stage());
        }
    }

    public static void createStadium() throws IOException, InterruptedException {
        if (RequestHelper.getStadiumName(LoginController.activeUsername).equalsIgnoreCase("")) {
            RequestHelper.setDefaultStadium("default", LoginController.activeUsername);
        }
    }

    public void checkAvailable(String input) throws IOException, InterruptedException {
        if (input.equalsIgnoreCase(RequestHelper.getStadiumName(LoginController.activeUsername))) {
            txt_changelog.setText("Error: You have choosen the same name");
        }
        else if(SpecialCharacter(input) == true){
            txt_changelog.setText("Error: Illegal characters");
        }
        else if(input.equalsIgnoreCase("")){
            txt_changelog.setText("Error: Textfield is empty");
        }
        else if(nameExists(input) == true){
            txt_changelog.setText("Error: Name is already given");
        }
        else{
            RequestHelper.changeStadiumName(txt_stadiumname.getText(), LoginController.activeUsername);
            stadiumname.setText(RequestHelper.getStadiumName(LoginController.activeUsername));
            txt_changelog.setText("You have successfully changed your name");
        }
    }

    public boolean nameExists(String input) throws IOException, InterruptedException {
        ArrayList<String> stadiumnames = RequestHelper.getAllStadium(LoginController.activeUsername);
        try{
            for (String s: stadiumnames) {
                if (s.equalsIgnoreCase(input)) {
                    return true;
                }
            }
        }
        catch(NullPointerException e){
            System.out.println("Es exestiert keine Stadiumdatabase");
        }
        return false;
    }

    public boolean SpecialCharacter(String input){
        String[] chars = {"/","%","§","#","$","&","*","(",")","|","+",
                "-","=","<",">","_","?","{","}","~","!","?","'",",",".",";",":"};
        for(int i = 0; i < chars.length; i++){
            if(input.contains(chars[i])){
                return true;
            }
        }
        return false;
    }

}
