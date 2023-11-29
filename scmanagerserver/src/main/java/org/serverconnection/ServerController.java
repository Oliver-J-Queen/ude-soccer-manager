package org.serverconnection;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import org.dataclasses.Stadium;
import org.dataclasses.Statistic;
import org.dataclasses.Tournament;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.scraper.Normalizer;
import org.scraper.ScraperDatabase;
import org.scraper.StrengthScraper;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerController {
    @FXML
    Text success_text, fail_text, welcomeText, welcomeTextScraper, scraperTextOutput;
    @FXML
    Button connectButton, disconnectButton, scraperButton, scraperButtonStop;
    @FXML
    CheckBox scraperCheckbox;
    final int port = 8080;
    private Server server;
    public static ConnectionSource userConn;
    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");


    public void startServer(Event e) throws Exception {
        scraperCheckbox.setVisible(false);
        server = new Server(port);
        success_text.setVisible(true);
        fail_text.setVisible(false);
        connectButton.setVisible(false);
        disconnectButton.setVisible(true);
        setHandler(server);
        server.start();
        System.out.println("Server is online!");
        try {
            String databaseURL = "jdbc:h2:~/scmanagerdb";
            userConn = new JdbcConnectionSource(databaseURL);
            System.out.println("Database connected");
            TableUtils.createTableIfNotExists(ServerController.userConn, Statistic.class);
            TableUtils.createTableIfNotExists(ServerController.userConn, Stadium.class);
            TableUtils.createTableIfNotExists(ServerController.userConn, Tournament.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void stopServer(Event e) throws Exception {
        success_text.setVisible(false);
        fail_text.setVisible(true);
        disconnectButton.setVisible(false);
        connectButton.setVisible(true);
        userConn.close();
        System.out.println("Database disconnected");
        server.stop();
        System.out.println("Server is offline!");
        scraperCheckbox.setVisible(true);
    }

    public void activateScraper(Event e) {
        if (!scraperCheckbox.isSelected()) {
            welcomeText.setVisible(true);
            fail_text.setVisible(true);
            connectButton.setVisible(true);
            welcomeTextScraper.setVisible(false);
            scraperButton.setVisible(false);
            scraperTextOutput.setVisible(false);
        } else {
            welcomeText.setVisible(false);
            fail_text.setVisible(false);
            success_text.setVisible(false);
            connectButton.setVisible(false);
            disconnectButton.setVisible(false);
            welcomeTextScraper.setVisible(true);
            scraperButton.setVisible(true);
            scraperTextOutput.setVisible(true);
            String currTime = time.format(new Date());
            scraperTextOutput.setText("The process of scraping will take approximately 25 Minutes.\n" +
                    "Please make sure that your internet connection will be stable within this time frame.\n" +
                    "You will not be able to stop the process while scraping!");
        }

    }

    public void scrape(Event e) throws IOException, SQLException {
        Runnable visual = () -> {
            String currTime = time.format(new Date());
            scraperTextOutput.setText("Scraper is running since " + currTime + " and will take about 25 minutes.\n"+
                    "Please do not stop the application while scraper is running!\n" +
                    "We will let you know then scraping is done.");
            scraperButton.setVisible(false);
            scraperCheckbox.setVisible(false);
        };
        Thread thread = new Thread(visual);

        Runnable scraping = () -> {
            ScraperDatabase.run();
            try {
                StrengthScraper.run();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                Normalizer.run();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            scraperButton.setVisible(true);
            scraperCheckbox.setVisible(true);
            scraperTextOutput.setText("Scraping successful! Have fun!");
        };
        Thread thread1 = new Thread(scraping);
        thread.start();
        thread1.start();
    }


    public void scrapeStop(Event e) {
        scraperButton.setVisible(true);
        scraperButtonStop.setVisible(false);
        //ScraperDatabase.run();
    }

    public void setHandler(Server server) {
        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(new UserEditHandler());
        handlers.addHandler(new FormationHandler());
        handlers.addHandler(new UserLoginHandler());
        handlers.addHandler(new UserLogoutHandler());
        handlers.addHandler(new UserPointsHandler());
        handlers.addHandler(new UserRegistrationHandler());
        handlers.addHandler(new PlayerViewHandler());
        handlers.addHandler(new UserInfoHandler());
        handlers.addHandler(new TeamHandler());
        handlers.addHandler(new UserFriendListHandler());
        handlers.addHandler(new GetAllUserHandler());
        handlers.addHandler(new SendFriendRequestHandler());
        handlers.addHandler(new AnswerFriendRequestHandler());
        handlers.addHandler(new GetRequestHandler());
        handlers.addHandler(new RemoveFriendHandler());
        handlers.addHandler(new ShopHandler());
        handlers.addHandler(new StatisticHandler());
        handlers.addHandler(new CalculateMatchHandler());
        handlers.addHandler(new SendMatchRequestHandler());
        handlers.addHandler(new AnswerMatchRequestHandler());
        handlers.addHandler(new GetMatchRequestHandler());
        handlers.addHandler(new StadiumHandler());
        handlers.addHandler(new StadiumUpgradeHandler());
        handlers.addHandler(new GetStadiumHandler());
        handlers.addHandler(new HomeHandler());
        handlers.addHandler(new SaveTeamhandler());
        handlers.addHandler(new AtPositionHandler());
        handlers.addHandler(new GetTacticsHandler());
        handlers.addHandler(new TournamentHandler());
        server.setHandler(handlers);
    }

}
