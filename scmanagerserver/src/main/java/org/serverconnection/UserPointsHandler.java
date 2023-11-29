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
import java.util.List;
/**
 * @author Yelle Lieder
 * had some inspiration from https://git.uni-due.de/sktrkley/self-study-exercises-for-programming.git
 */
public class UserPointsHandler extends AbstractHandler {
    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user")&&splitTarget[2].equals("points")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            if(splitTarget[3].equals("get")){
                getPoints(response.getWriter(), splitTarget[4]);
            }
            originalRequest.setHandled(true);
        }
    }

    public void getPoints(PrintWriter input, String username){
        try{
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            List<User> userList =  userDao.queryForAll();
            for(User user:userList){
                if(user.getUsername().equalsIgnoreCase(username)) {
                    System.out.println(user.getPoints() + " points for User "+ username);
                    input.print(Integer.toString(user.getPoints()));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
