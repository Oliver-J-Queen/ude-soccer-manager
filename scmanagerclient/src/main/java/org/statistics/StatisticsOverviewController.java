package org.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.clientconnection.RequestHelper;
import org.registration.LoginController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Yelle Lieder
 */

public class StatisticsOverviewController implements Initializable {

    private double x, y;

    @FXML
    private TextField searchfield;
    @FXML
    private TableView<Statistic> table;
    @FXML
    private TableColumn<Statistic, String> timestamp;
    @FXML
    private TableColumn<Statistic, Boolean> win;
    @FXML
    private TableColumn<Statistic, Integer> goals;
    @FXML
    private TableColumn<Statistic, Integer> conceded;
    @FXML
    private TableColumn<Statistic, String> setup;
    @FXML
    private TableColumn<Statistic, Double> overallstrength;
    @FXML
    private TableColumn<Statistic, Integer> statisticsid;
    @FXML
    private LineChart<?, ?> goalsChart;
    @FXML
    CategoryAxis dateAxis;
    @FXML
    NumberAxis goalsAxis;
    @FXML
    PieChart winLoss;

    public void main(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("StatisticsOverview.fxml"));
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
        try {
            timestamp.setCellValueFactory(new PropertyValueFactory<>("TIMESTAMP"));
            win.setCellValueFactory(new PropertyValueFactory<>("WIN"));
            goals.setCellValueFactory(new PropertyValueFactory<>("GOALS"));
            conceded.setCellValueFactory(new PropertyValueFactory<>("CONCEDED"));
            setup.setCellValueFactory(new PropertyValueFactory<>("SETUP"));
            overallstrength.setCellValueFactory(new PropertyValueFactory<>("OVERALLSTRENGTH"));
            statisticsid.setCellValueFactory(new PropertyValueFactory<>("STATISTICID"));
            String[] ID = RequestHelper.getUserInfo(LoginController.activeUsername).split("/");
            ArrayList<Statistic> allStatistics = RequestHelper.getStatistic(ID[3]);
            ObservableList<Statistic> observableStatistics = FXCollections.observableArrayList(allStatistics);
            FilteredList<Statistic> filteredStatistics = new FilteredList<>(observableStatistics, b -> true);
            searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredStatistics.setPredicate(tmpStatisticsObject -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Integer.toString(tmpStatisticsObject.getCONCEDED()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (Integer.toString(tmpStatisticsObject.getGOALS()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (Double.toString(tmpStatisticsObject.getOVERALLSTRENGTH()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (tmpStatisticsObject.getSETUP().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (Integer.toString(tmpStatisticsObject.getSTATISTICID()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (tmpStatisticsObject.getTIMESTAMP().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (tmpStatisticsObject.getUSER().getUsername().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Statistic> sortedStatistics = new SortedList<Statistic>(filteredStatistics);
            sortedStatistics.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedStatistics);
            if(!sortedStatistics.isEmpty()){
                initializeLineChart(sortedStatistics);
                initializePieChart(sortedStatistics);
            }
        } catch (IOException | InterruptedException e) {
            System.out.print("Something went wrong!");
        }
    }

    private void initializePieChart(SortedList<Statistic> sortedStatistics) {
        int w = 0;
        int l = 0;
        for (Statistic s : sortedStatistics) {
            if (s.isWIN()) {
                w++;
            } else {
                l++;
            }
        }
        ObservableList<PieChart.Data> pieContent = FXCollections.observableArrayList(
                new PieChart.Data("Loss", l),
                new PieChart.Data("Win", w)
        );
        winLoss.setData(pieContent);
    }

    private void initializeLineChart(SortedList<Statistic> sortedStatistics) {
        XYChart.Series data = new XYChart.Series<>();
        for (Statistic s : sortedStatistics) {
            data.getData().add(new XYChart.Data(s.getTIMESTAMP(), s.getGOALS()));
        }
        goalsChart.getData().addAll(data);
    }
}
