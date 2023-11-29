package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
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

public class FormationHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equalsIgnoreCase("formation")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            saveFormation(response.getWriter(),splitTarget[2], splitTarget[3]);
            originalRequest.setHandled(true);
        }
    }

    public void saveFormation(PrintWriter input, String formation, String userID) {
        try {
            Dao<Team, String> teamDao = DaoManager.createDao(ServerController.userConn, Team.class);
            for (Team team : teamDao) {
                if (Integer.toString(team.getUSER().getID()).equalsIgnoreCase(userID)) {
                    team.setFORMATION(formation);
                    teamDao.update(team);
                    input.print("success");
                    break;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
