package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
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
 * @author Paul Naebers
 **/


public class HomeHandler extends AbstractHandler {
    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equalsIgnoreCase("setIcon")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            saveIcon(response.getWriter(),splitTarget[2], splitTarget[3]);
        }
        else if(splitTarget[1].equalsIgnoreCase("getIcon")){
            getIcon(response.getWriter(),splitTarget[2]);
        }
        originalRequest.setHandled(true);
    }

    public void saveIcon(PrintWriter input, String Icon, String userID) {
        try {
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            for (User user : userDao) {
                if (Integer.toString(user.getID()).equalsIgnoreCase(userID)) {
                    user.setProfilpicture(Icon);
                    userDao.update(user);
                    input.print("success");
                    break;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getIcon(PrintWriter input, String userID) {
        try {
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            User tmp = userDao.queryForId(userID);
            input.print(tmp.getProfilpicture());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
