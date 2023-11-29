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
import java.util.ArrayList;

public class SendMatchRequestHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("sendMatchRequest")) {
            System.out.println("found handler");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            sendMatchRequest(response.getWriter(), splitTarget[3], splitTarget[4]);
            originalRequest.setHandled(true);
        }
    }

    private void sendMatchRequest(PrintWriter input, String username, String friendsUsername) {
        UserEditHandler tp = new UserEditHandler();
        Integer idUser = tp.getIDFromUsername(username);
        Integer idFriend = tp.getIDFromUsername(friendsUsername);
        try {
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            User friend = userDao.queryForId(idFriend);
            ArrayList<Integer> tmpRequests = new ArrayList<>();
            if(friend.getMATCHREQUESTS()!=null){
                tmpRequests = friend.getMATCHREQUESTS();
            }
            if(tmpRequests.contains(idUser)){
                input.print("Already sent Match Request");
            }else {
                tmpRequests.add(idUser);
                friend.setMATCHREQUESTS(tmpRequests);
                userDao.update(friend);
                input.print("Request succesful1");
            }
        }catch (SQLException throwables) {
            System.out.println("crashed");
            throwables.printStackTrace();
        }

    }
}