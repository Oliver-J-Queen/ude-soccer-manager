package org.serverconnection;

import com.google.gson.Gson;
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
import java.util.List;

public class GetAllUserHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("getAllUser")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            getAllUser(response.getWriter(), splitTarget[3]);
            originalRequest.setHandled(true);
        }
    }

    public void getAllUser(PrintWriter input, String username) {
        try{
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            UserEditHandler ueh = new UserEditHandler();
            int id = ueh.getIDFromUsername(username);
            User tmp = userDao.queryForId(id);

            ArrayList<User> allUserList = new ArrayList<User>();
            List<User> tmpAllUserList = userDao.queryForAll();
           for (int i = 0; i < tmpAllUserList.size(); i++){
               allUserList.add(tmpAllUserList.get(i));
           }
           if(allUserList.size()==1){
               allUserList.remove(0);
               User back = new User("/","/","/","/", false);
               allUserList.add(back);
           }else {
               //get all friends
               UserFriendListHandler uf = new UserFriendListHandler();
               ArrayList<User> friendList = new ArrayList<User>();
               if (!tmp.getFRIENDS().equals("0")) {
                   String[] splitf = tmp.getFRIENDS().split("/");
                   for (int i = 0; i < splitf.length; i++) {
                       friendList.add(uf.getUser(splitf[i]));
                   }
                   //remove friends & own user
                   for (int n = 0; n < friendList.size(); n++) {
                       User t = friendList.get(n);
                       for (int i = 0; i < allUserList.size(); i++) {
                           if (allUserList.get(i).getID() == t.getID()) {
                               allUserList.remove(i);
                           }
                       }
                       for (int m = 0; m < allUserList.size(); m++) {
                           if (allUserList.get(m).getID() == tmp.getID()) {
                               allUserList.remove(m);
                           }
                       }
                   }
                   if(allUserList.isEmpty()==true){
                       User back = new User("/","/","/","/", false);
                       allUserList.add(back);
                   }
               }else{
                   for (int m = 0; m < allUserList.size(); m++) {
                       if (allUserList.get(m).getID() == tmp.getID()) {
                           allUserList.remove(m);
                       }
                   }
               }
           }
           input.print(new Gson().toJson(allUserList, ArrayList.class));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connection failed");
        }
    }
}
