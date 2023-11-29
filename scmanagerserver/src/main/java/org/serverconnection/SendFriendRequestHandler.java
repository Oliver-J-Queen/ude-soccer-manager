package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SendFriendRequestHandler  extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("sendFriendRequest")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            sendFriendRequest(response.getWriter(), splitTarget[3], splitTarget[4]);
            originalRequest.setHandled(true);
        }
    }

    public void sendFriendRequest(PrintWriter input, String username, String friendsUsername) {
        UserEditHandler tp = new UserEditHandler();
        int idUser = tp.getIDFromUsername(username);
        int idFriend = tp.getIDFromUsername(friendsUsername);

        try {
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            User friend = userDao.queryForId(idFriend);
            String id = String.valueOf(idUser);
            if(friend.getREQUESTS().equals("0")){
                friend.setREQUESTS(id);
                userDao.update(friend);
                input.print("Request succesful1");
            }else{
                String tmp = friend.getREQUESTS();
                String[] splittmp = tmp.split("/");
                ArrayList<String> list = new ArrayList<String>();
                for(int i=0;i<splittmp.length;i++){
                    list.add(splittmp[i]);
                }
                if(list.contains(id)){
                    input.print("Already sent Friend Request");
                }else {
                    friend.setREQUESTS(tmp + "/" + id);
                    userDao.update(friend);
                    input.print("Request succesful2");
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}