package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.dataclasses.Player;
import org.dataclasses.Team;
import org.dataclasses.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author: Amine Bida
 */

public class AtPositionHandler extends AbstractHandler {
    ConnectionSource connectionSource;

    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equalsIgnoreCase("atPosition")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            try {
                handlePosition(response.getWriter(),splitTarget[2], splitTarget[3]);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            originalRequest.setHandled(true);
        }
    }

    public void handlePosition(PrintWriter input, String userID, String position) throws SQLException{

        try {
            String databaseUrl = "jdbc:h2:~/scmanagerdb";
            connectionSource = new JdbcConnectionSource(databaseUrl);
            System.out.println("Connection established!");
            ArrayList<String> players = new ArrayList<>();

            Dao<Team, String> teamDao = DaoManager.createDao(ServerController.userConn, Team.class);

            for (Team team : teamDao) {
                if (Integer.toString(team.getUSER().getID()).equalsIgnoreCase(userID)) { //team found
                    Dao<Player, String> playerDao = DaoManager.createDao(connectionSource, Player.class);
                    for (Player player : playerDao) {
                        for (int id : team.getPlayersList()) {
                            if (player.getId() == id && player.getPosition().equalsIgnoreCase(position)) {
                                players.add(player.toString());
                            }
                        }
                    }
                }
            }
            input.println(new Gson().toJson(players, ArrayList.class));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
