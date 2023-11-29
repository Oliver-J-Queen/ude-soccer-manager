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

public class GetMatchRequestHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("getMatchRequest")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            getMatchRequest(response.getWriter(), splitTarget[3]);
            originalRequest.setHandled(true);
        }
    }

    public void getMatchRequest(PrintWriter input, String username){
        UserEditHandler tp = new UserEditHandler();
        int id = tp.getIDFromUsername(username);
        try {
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            User tmp = userDao.queryForId(id);
            ArrayList<User> userList = new ArrayList<>();
            if (tmp.getMATCHREQUESTS() == null || tmp.getMATCHREQUESTS().isEmpty()) {
                User leer = new User("1", "/", "/", "/", false);
                userList.add(leer);
            }else{
                ArrayList<Integer> requestList = tmp.getMATCHREQUESTS();
                for(int i=0;i<requestList.size();i++){
                    userList.add(userDao.queryForId(requestList.get(i)));
                }
            }
            input.print(new Gson().toJson(userList, ArrayList.class));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
