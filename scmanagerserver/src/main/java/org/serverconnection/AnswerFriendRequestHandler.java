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

public class AnswerFriendRequestHandler  extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("answerFriendRequest")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            answerFriendRequest(response.getWriter(), splitTarget[3], splitTarget[4], splitTarget[5]);
            originalRequest.setHandled(true);
        }
    }

    public void answerFriendRequest(PrintWriter input, String username, String friendsUsername, String answer){
        UserEditHandler tp = new UserEditHandler();
        if(answer.equals("angenommen")){
            int idUser = tp.getIDFromUsername(username);
            int idFriend = tp.getIDFromUsername(friendsUsername);
            addFriend(idUser, idFriend);
            addFriend(idFriend, idUser);
            deleteRequest(idUser, idFriend);
            input.print("Request accepted");
        }else if(answer.equals("abgelehnt")){
            int idUser = tp.getIDFromUsername(username);
            int idFriend = tp.getIDFromUsername(friendsUsername);
            deleteRequest(idUser, idFriend);
            input.print("Request deleted");
        }else{
            System.out.println("Something went wrong");
        }
    }

    public void deleteRequest(int iDUser, int iDFriend){
        String friendsID = String.valueOf(iDFriend);
        try{
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            User us = userDao.queryForId(iDUser);
            String tmp = us.getREQUESTS();
            String[] splitRequests = tmp.split("/");
            ArrayList<String> requestList = new ArrayList<String>();
            for(int i=0; i<splitRequests.length;i++){
                requestList.add(splitRequests[i]);
            }
            requestList.remove(friendsID);
            if(requestList.isEmpty()){
                us.setREQUESTS("0");
            }else {
                String newList = requestList.get(0);
                for (int n = 1; n < requestList.size(); n++) {
                    newList = newList + "/" + requestList.get(n);
                }
                us.setREQUESTS(newList);
            }
            userDao.update(us);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addFriend(int id1, int id2){
        String id2St = String.valueOf(id2);
        try {
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            User us = userDao.queryForId(id1);
            if(us.getFRIENDS().equals("0")){
                //if the user has no friends
                us.setFRIENDS(id2St);
                userDao.update(us);
            }else{
                //if the user has already friends
                String us1Friends = us.getFRIENDS();
                us1Friends = us1Friends + "/" + id2St;
                us.setFRIENDS(us1Friends);
                userDao.update(us);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}