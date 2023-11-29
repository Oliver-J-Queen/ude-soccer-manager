package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.dataclasses.User;
import org.dataclasses.FriendRequest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetRequestHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("getOpenRequests")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            getRequests(response.getWriter(), splitTarget[3]);
            originalRequest.setHandled(true);
        }
    }

   public void getRequests(PrintWriter input, String username){
        UserEditHandler tp = new UserEditHandler();
        //Get UserID from UserEditHandler Method and save to String
        int UserID = tp.getIDFromUsername(username);
       try {
           Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
           User tpUser = userDao.queryForId(UserID);
           String OpenUserReqs = tpUser.getREQUESTS();
           String[] splitedReqs = OpenUserReqs.split("/");
           ArrayList<FriendRequest> OpenUserReqList = new ArrayList<FriendRequest>();
           //If String is empty (0) the Json and Gson can not handle it, so the String stays "empty" but with a / insteatd of 0
           if(splitedReqs[0].equals("0")){
               OpenUserReqList.add(new FriendRequest("/"));
           }else {
               for (int i = 0; i < splitedReqs.length; i++) {
                   //add every Request from UserRow to ArrayList as a FriendRequest class
                   int ids = Integer.parseInt(splitedReqs[i]);
                   OpenUserReqList.add(new FriendRequest(userDao.queryForId(ids).getUsername()));
               }
           }
           input.print(new Gson().toJson(OpenUserReqList, ArrayList.class));
       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
   }
}
