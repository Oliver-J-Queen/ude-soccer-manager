package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.dataclasses.Statistic;
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
 * @author Yelle Lieder
 */

public class GetTacticsHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("getTactics")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            try {
                getTactics(response.getWriter(), splitTarget[2]);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            originalRequest.setHandled(true);
        }
    }

    public void getTactics(PrintWriter input, String username) throws SQLException {
        String x="";
        Dao<Team, Integer> teamDao = DaoManager.createDao(ServerController.userConn, Team.class);
        Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
        int b=0;
        for(User u:userDao){
            if(u.getUsername().equalsIgnoreCase(username)){
                b=u.getID();
            }
        }

        for (Team t : teamDao) {
            if(t.getUSER().getID()==b){
                x=t.getFORMATION();
                if(x.equalsIgnoreCase("")){
                    x="4-4-2";
                }
                break;
            }
        }
        input.println(x);
    }
}
