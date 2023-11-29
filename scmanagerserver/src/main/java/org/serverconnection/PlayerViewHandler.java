package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.dataclasses.Player;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerViewHandler  extends AbstractHandler {

    ConnectionSource connectionSource;
    /**
     * @author Yelle Lieder (handle method)
     * had some inspiration from https://git.uni-due.de/sktrkley/self-study-exercises-for-programming.git, nothing directly copied
     */
    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("player")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            try {
                getAllPlayer(response.getWriter());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            originalRequest.setHandled(true);
        }
    }

    public void getAllPlayer(PrintWriter input) throws SQLException {
        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        connectionSource = new JdbcConnectionSource(databaseUrl);
        System.out.println("Connection established!");
        ArrayList<Player> players = new ArrayList<>();
        Dao<Player, String> playerDao = DaoManager.createDao(connectionSource, Player.class);

        for(Player player : playerDao){
            players.add(player);
        }
        input.println(new Gson().toJson(players, ArrayList.class));
    }
}
