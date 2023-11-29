package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.print.PrintSides;
import org.dataclasses.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserInfoHandler  extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("getUserInfo")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            getUserInfo(response.getWriter(), splitTarget[3]);
            originalRequest.setHandled(true);
        }
    }

    public void getUserInfo(PrintWriter input, String username){
        try{
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            UserEditHandler ueh = new UserEditHandler();
            int id = ueh.getIDFromUsername(username);
            User tmp = userDao.queryForId(id);
            String friends = tmp.getFRIENDS();
            String[] splitFriends = friends.split("/");
            int count = splitFriends.length;
            if(splitFriends[0].equals("0")){
                input.print(tmp.getEMAIL() + "/" + tmp.getSTATUS() + "/" + "0"+"/"+tmp.getID()+"/");
            }else {
                String countSt = String.valueOf(count);
                input.print(tmp.getEMAIL() + "/" + tmp.getSTATUS() + "/" + countSt+"/"+tmp.getID()+"/");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connection failed");
        }
    }
}