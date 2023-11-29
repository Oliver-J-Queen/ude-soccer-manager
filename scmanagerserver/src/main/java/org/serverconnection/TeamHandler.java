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

public class TeamHandler extends AbstractHandler {
    ConnectionSource connectionSource;

    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equalsIgnoreCase("team")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            if(splitTarget[2].equals("create")){
                createTeam(response.getWriter(),splitTarget[3], splitTarget[4]);
            }
            if(splitTarget[2].equals("change")){
                changeTeamName(response.getWriter(),splitTarget[3], splitTarget[4]);
            }
            if(splitTarget[2].equals("get")){
                try {
                    getTeam(response.getWriter(),splitTarget[3]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            originalRequest.setHandled(true);
        }
    }
    public void getTeam(PrintWriter input, String userID) throws SQLException {

        try {
            String databaseUrl = "jdbc:h2:~/scmanagerdb";
            connectionSource = new JdbcConnectionSource(databaseUrl);
            System.out.println("Connection established!");
            ArrayList<String> players = new ArrayList<>();

            Dao<Team, String> teamDao = DaoManager.createDao(ServerController.userConn, Team.class);

            for (Team team : teamDao) {
                if (Integer.toString(team.getUSER().getID()).equalsIgnoreCase(userID)) {
                    Dao<Player, String> playerDao = DaoManager.createDao(connectionSource, Player.class);
                    for (Player player : playerDao) {
                        for (int id : team.getPlayersList()) {
                            if (player.getId() == id) {
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

    public boolean createTeam(PrintWriter input, String clubname, String username) {
        try {
            Dao<Team, String> teamDao = DaoManager.createDao(ServerController.userConn, Team.class);
            TableUtils.createTableIfNotExists(ServerController.userConn, Team.class);

            int tmp = findUserId(username);
            User user = showUser(tmp);
            Team tmpteam = new Team(clubname, user);
            teamDao.create(tmpteam);

            input.print("success");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void changeTeamName(PrintWriter input, String clubname, String username) {
        clubname=clubname.replace("+", " ");
        try {
            Dao<Team, String> teamDao = DaoManager.createDao(ServerController.userConn, Team.class);


            for (Team team : teamDao) {
                if (Integer.toString(team.getUSER().getID()).equalsIgnoreCase(username)) {
                    team.setTEAMNAME(clubname);
                    teamDao.update(team);
                    input.print("success");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findUserId(String username) throws SQLException {
       try {
           Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
           int count = 0;


           QueryBuilder<User, String> qb = userDao.queryBuilder();
           qb.where().eq("USERNAME", username);
           PreparedQuery<User> pq = qb.prepare();
           User userToCheck = userDao.queryForFirst(pq);
           if (userToCheck == null) {
               return 0;
           } else {
               return userToCheck.getID();
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
        return 0;
    }

    public User showUser(int id) throws  SQLException {
        try {
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            User reUser = userDao.queryForId(String.valueOf(id));
            return reUser;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}
