package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.Player;
import org.dataclasses.Statistic;
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

public class StatisticHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("statistic")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            try {
                getStatistic(response.getWriter(), splitTarget[2]);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            originalRequest.setHandled(true);
        }
    }

    public void getStatistic(PrintWriter input, String requestUserID) throws SQLException {
        int requestUserIDInt = Integer.parseInt(requestUserID);
        ArrayList<Statistic> returnList = new ArrayList<>();
        Dao<Statistic, Integer> statsDao = DaoManager.createDao(ServerController.userConn, Statistic.class);
        for (Statistic stat : statsDao) {
            if (stat.getUSER().getID() == requestUserIDInt) {
                returnList.add(stat);
            }
        }
        input.println(new Gson().toJson(returnList, ArrayList.class));
    }
}
