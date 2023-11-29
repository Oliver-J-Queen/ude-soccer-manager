package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.print.PrintSides;
import org.dataclasses.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.security.auth.login.LoginContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserFriendListHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("getFriendList")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            getFriendList(response.getWriter(), splitTarget[3]);
            originalRequest.setHandled(true);
        }
    }

    public void getFriendList(PrintWriter input, String username) {
        try{
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            UserEditHandler ueh = new UserEditHandler();
            int id = ueh.getIDFromUsername(username);
            User tmp = userDao.queryForId(id);
            ArrayList<User> friendlist = new ArrayList<User>();
            String[] friends = tmp.getFRIENDS().split("/");
            for(int i=0;i<friends.length;i++){
                friendlist.add(this.getUser(friends[i]));
            }
            input.print(new Gson().toJson(friendlist, ArrayList.class));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connection failed");
        }
    }

    public User getUser(String id) throws SQLException {
        int i = Integer.parseInt(id);

        Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
        return userDao.queryForId(i);
    }
}
