package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.dataclasses.Stadium;
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

/**
 * @author: Amine Bida
 */

public class StadiumHandler extends AbstractHandler {
    ConnectionSource connectionSource;

    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equalsIgnoreCase("stadium")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            if(splitTarget[2].equals("stadiumname")){
                setStadiumName(response.getWriter(),splitTarget[3],splitTarget[4]);
            }
            /*if(splitTarget[2].equals("check")) {
                getStadiumCheck(response.getWriter(),splitTarget[3]);
            }*/
            if(splitTarget[2].equals("create")){
                try {
                    createDefaultStadium(response.getWriter(),splitTarget[3],splitTarget[4]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            originalRequest.setHandled(true);
        }
    }

    private void createDefaultStadium(PrintWriter input, String stadiumname, String username) throws SQLException {
        try {
            String databaseUrl = "jdbc:h2:~/scmanagerdb";
            connectionSource = new JdbcConnectionSource(databaseUrl);
            System.out.println("Connection established!");
            System.out.println("Serverside: createDefaultTeam");

            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(ServerController.userConn, Stadium.class);
            TableUtils.createTableIfNotExists(ServerController.userConn, Stadium.class);

            int tmp = findUserId(username);
            User user = showUser(tmp);
            int searchedTeamID = findTeamID(tmp);
            Team team = showTeam(searchedTeamID);

            Stadium tmpStadium = new Stadium(stadiumname, team, 5000, "small", "small", user);
            stadiumDAO.create(tmpStadium);

            Dao<Team, String> teamDAO = DaoManager.createDao(ServerController.userConn, Team.class);
            for (Team teamToUpdate : teamDAO) {

                if (teamToUpdate.getUSER().getID()==tmp) {
                    teamToUpdate.setSTADIUM(tmpStadium);
                }
                teamDAO.update(teamToUpdate);
                teamDAO.refresh(teamToUpdate);
            }

            input.print("success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setStadiumName(PrintWriter input, String stadiumname, String username) {
      stadiumname=stadiumname.replace("+", " ");
        try {
            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(ServerController.userConn, Stadium.class);
            int searchedUserID = this.findUserId(username);
            int searchedTeamID = this.findTeamID(searchedUserID);
            for (Stadium stadium : stadiumDAO) {
                if (stadium.getTeam().getTEAMID() == searchedTeamID) {
                    stadium.setName(stadiumname);
                    stadiumDAO.update(stadium);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getStadiumStatus (User user) throws SQLException{
        int result = 0;
        try {
            connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
            Dao<User, String> userDAO = DaoManager.createDao(connectionSource, User.class);
            Dao<Stadium, Integer> stadiumDAO = DaoManager.createDao(connectionSource, Stadium.class);
            for (Stadium searchStadium : stadiumDAO) {
                if (searchStadium.getUSER().getID() == user.getID()) {
                    if (searchStadium.getCapacity() == 5000) {
                        result = result +1;
                    }
                    if (searchStadium.getCapacity() == 25000) {
                        result = result +2;
                    }
                    if (searchStadium.getCapacity() == 65000) {
                        result = result +3;
                    }
                    if (searchStadium.getImbis().equals("small")) {
                        result = result +1;
                    }
                    if (searchStadium.getImbis().equals("medium")) {
                        result = result +2;
                    }
                    if (searchStadium.getImbis().equals("Big")) {
                        result = result +3;
                    }
                    if (searchStadium.getParking().equals("small")) {
                        result = result +1;
                    }
                    if (searchStadium.getParking().equals("medium")) {
                        result = result +2;
                    }
                    if (searchStadium.getParking().equals("Big")) {
                        result = result +3;
                    }
                    return result;
                }
                return result;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public int findTeamID(Integer userID) throws SQLException {
        try {
            Dao<Team, String> teamDAO = DaoManager.createDao(ServerController.userConn, Team.class);
            QueryBuilder<Team, String> qb = teamDAO.queryBuilder();
            qb.where().eq("USERID", userID);
            PreparedQuery<Team> pq = qb.prepare();
            Team teamToCheck = teamDAO.queryForFirst(pq);
            if (teamToCheck == null) {
                return 0;
            } else {
                return teamToCheck.getTEAMID();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Team showTeam(int id) throws  SQLException {
        try {
            Dao<Team, String> teamDAO = DaoManager.createDao(ServerController.userConn, Team.class);
            Team sTeam = teamDAO.queryForId(String.valueOf(id));
            return sTeam;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int findUserId(String username) throws SQLException {
        try {
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
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

    /*
    public void getStadiumCheck(PrintWriter input, String username) {
        try {
            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(ServerController.userConn, Stadium.class);
            int searchedUserID = this.findUserId(username);
            for (Stadium stadium : stadiumDAO) {
                if (stadium.getUSER().getID()==searchedUserID) {
                    input.print(stadium.getName());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
