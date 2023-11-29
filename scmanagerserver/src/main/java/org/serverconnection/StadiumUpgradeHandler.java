package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.Stadium;
import org.dataclasses.Team;
import org.dataclasses.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * @author: Amine Bida
 */

public class StadiumUpgradeHandler extends AbstractHandler {
    ConnectionSource connsrc;
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        String[] splitTarget = target.split("/");
        if (splitTarget[1].equals("upgrade")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            try {
                if (splitTarget[2].equals("parking") && splitTarget[3].equals("toMedium")) {
                    upgradeParking(response.getWriter(), splitTarget[4],splitTarget[5], 500);
                }
                if (splitTarget[2].equals("parking") && splitTarget[3].equals("toBig")) {
                    upgradeParking(response.getWriter(), splitTarget[4],splitTarget[5], 1000);
                }
                if (splitTarget[2].equals("parking") && splitTarget[3].equals("enlarged")) {
                    upgradeParking(response.getWriter(), splitTarget[4],splitTarget[5], 1500);
                }
                if (splitTarget[2].equals("imbis") && splitTarget[3].equals("toMedium")) {
                    upgradeImbis(response.getWriter(), splitTarget[4],splitTarget[5], 500);
                }
                if (splitTarget[2].equals("imbis") && splitTarget[3].equals("toBig")) {
                    upgradeImbis(response.getWriter(), splitTarget[4],splitTarget[5], 1000);
                }
                if (splitTarget[2].equals("imbis") && splitTarget[3].equals("enlarged")) {
                    upgradeParking(response.getWriter(), splitTarget[4],splitTarget[5], 1500);
                }
                if (splitTarget[2].equals("capacity") && splitTarget[3].equals("step1")) {
                    upgradeCapacity(response.getWriter(), splitTarget[4],25000, 500);
                }
                if (splitTarget[2].equals("capacity") && splitTarget[3].equals("step2")) {
                    upgradeCapacity(response.getWriter(), splitTarget[4],50000, 1000);
                }
                if (splitTarget[2].equals("capacity") && splitTarget[3].equals("step3")) {
                    upgradeCapacity(response.getWriter(), splitTarget[4],65000, 1500);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            baseRequest.setHandled(true);
        }
    }

    private void upgradeCapacity(PrintWriter writer, String username, int capacityNew, int price) throws SQLException{
        try {
            connsrc = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
            Dao<User, String> userDAO = DaoManager.createDao(connsrc, User.class);
            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(connsrc, Stadium.class);
            int actualUserID = findUserId(username);
            for (User searchForUser : userDAO) {
                if(searchForUser.getID()==actualUserID && searchForUser.getPoints() >= price) {
                    for (Stadium stadium : stadiumDAO) {
                        if (stadium.getUSER().getID()==actualUserID) {
                            stadium.setCapacity(capacityNew);
                            stadiumDAO.update(stadium);
                            searchForUser.setPoints(searchForUser.getPoints() - price);
                            userDAO.update(searchForUser);
                            writer.print("success");
                        }
                        else {
                            writer.print("error");
                        }
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void upgradeParking(PrintWriter writer, String username, String capacityNew, int price) throws SQLException {
        try {
            connsrc = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
            Dao<User, String> userDAO = DaoManager.createDao(connsrc, User.class);
            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(connsrc, Stadium.class);
            int loggedUserID = findUserId(username);
                for (User user : userDAO) {
                    if(user.getID()==loggedUserID && user.getPoints() >= price) {
                        for (Stadium stadium : stadiumDAO) {
                            if (stadium.getUSER().getID()==loggedUserID) {
                                stadium.setParking(capacityNew);
                                stadiumDAO.update(stadium);
                                user.setPoints(user.getPoints() - price);
                                userDAO.update(user);
                            }
                            else {
                                writer.print("error");
                            }
                        }
                    }
                }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void upgradeImbis(PrintWriter writer, String username, String capacityNew, int price) throws SQLException{
        try {
            connsrc = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
            Dao<User, String> userDAO = DaoManager.createDao(connsrc, User.class);
            Dao<Team, String> teamDAO = DaoManager.createDao(connsrc, Team.class);
            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(connsrc, Stadium.class);
            int loggedUserID = findUserId(username);
            for (User user : userDAO) {
                if(user.getID()==loggedUserID && user.getPoints() >= price) {
                    for (Stadium stadium : stadiumDAO) {
                        if (stadium.getUSER().getID()==loggedUserID) {
                            stadium.setImbis(capacityNew);
                            stadiumDAO.update(stadium);
                            user.setPoints(user.getPoints() - price);
                            userDAO.update(user);
                        }
                        else {
                            writer.print("error");
                        }
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int findUserId(String username) throws SQLException {
        try {
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            QueryBuilder<User, String> qb = userDao.queryBuilder();
            qb.where().eq("USERNAME", username);
            PreparedQuery<User> pq = qb.prepare();
            User UserToFind = userDao.queryForFirst(pq);
            if (UserToFind == null) {
                return 0;
            } else {
                return UserToFind.getID();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
}
