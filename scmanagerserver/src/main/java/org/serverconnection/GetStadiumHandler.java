package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.dataclasses.Stadium;
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
 * @author: Paul Naebers
 */

public class GetStadiumHandler extends AbstractHandler {
    ConnectionSource connectionSource;
    TeamHandler teamHandler = new TeamHandler();

    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equalsIgnoreCase("getStadium")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            if(splitTarget[2].equals("imbis")) {
                getImbis(response.getWriter(),splitTarget[3]);
            }
            else if(splitTarget[2].equals("capacity")) {
                getCapacity(response.getWriter(),splitTarget[3]);
            }
            else if(splitTarget[2].equals("parking")) {
                getParking(response.getWriter(),splitTarget[3]);
            }
            else if(splitTarget[2].equals("name")) {
                getName(response.getWriter(),splitTarget[3]);
            }
            else if(splitTarget[2].equals("stadium")) {
                try {
                    getAllStadium(response.getWriter(),splitTarget[3]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            originalRequest.setHandled(true);
        }
    }

    public void getImbis(PrintWriter input, String username) {
        try {
            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(ServerController.userConn, Stadium.class);

            int searchedUserID = teamHandler.findUserId(username);

            for (Stadium current : stadiumDAO) {
                if (current.getUSER().getID()==searchedUserID) {
                    input.print(current.getImbis());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getCapacity(PrintWriter input, String username) {
        try {
            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(ServerController.userConn, Stadium.class);

            int searchedUserID = teamHandler.findUserId(username);

            for (Stadium currentCapacity : stadiumDAO) {
                if (currentCapacity.getUSER().getID()==searchedUserID) {
                    input.print(currentCapacity.getCapacity());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getParking(PrintWriter input, String username) {
        try {
            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(ServerController.userConn, Stadium.class);

            int searchedUserID = teamHandler.findUserId(username);

            for (Stadium currentParking : stadiumDAO) {
                if (currentParking.getUSER().getID()==searchedUserID) {
                    input.print(currentParking.getParking());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getName(PrintWriter input, String username) {
        try {
            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(ServerController.userConn, Stadium.class);

            int searchedUserID = teamHandler.findUserId(username);

            for (Stadium currentName : stadiumDAO) {
                if (currentName.getUSER().getID() == searchedUserID) {
                    input.print(currentName.getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAllStadium(PrintWriter input, String username) throws SQLException {
        try {
            String databaseUrl = "jdbc:h2:~/scmanagerdb";
            connectionSource = new JdbcConnectionSource(databaseUrl);
            TableUtils.createTableIfNotExists(connectionSource, Stadium.class);
            ArrayList<String> stadiumnames = new ArrayList<>();

            Dao<Stadium, String> stadiumDAO = DaoManager.createDao(ServerController.userConn, Stadium.class);

            for (Stadium stadium: stadiumDAO) {
                stadiumnames.add(stadium.getName());
            }
            input.println(new Gson().toJson(stadiumnames, ArrayList.class));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
