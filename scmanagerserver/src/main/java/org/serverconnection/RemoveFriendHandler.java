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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RemoveFriendHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user")&&splitTarget[2].equals("removeFriend")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            removeFriend(response.getWriter(), splitTarget[3], splitTarget[4]);
            originalRequest.setHandled(true);
        }
    }

    public void removeFriend(PrintWriter input, String username, String friendusername) {
        try{
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            UserEditHandler ueh = new UserEditHandler();
            int idU = ueh.getIDFromUsername(username);
            int idFU = ueh.getIDFromUsername(friendusername);

            User logUser = userDao.queryForId(idU);
            User friend = userDao.queryForId(idFU);

            String[] userFList = logUser.getFRIENDS().split("/");
            String userF = remove(userFList, idFU);
            logUser.setFRIENDS(userF);
            userDao.update(logUser);

            String[] friendFList = friend.getFRIENDS().split("/");
            String friendF = remove(friendFList, idU);
            friend.setFRIENDS(friendF);
            userDao.update(friend);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connection failed");
        }
    }

    public String remove(String[] list, int id){
        String idS = String.valueOf(id);
        ArrayList<String> tp = new ArrayList<>();
        String result = "0";
        for(int i=0;i<list.length;i++){
            tp.add(list[i]);
        }
        for(int n=0;n<tp.size();n++){
            if(tp.get(n).equals(idS)){
                tp.remove(n);
            }
        }
        for(int m=0;m<tp.size();m++){
            if(result.equals("0")){
                result = tp.get(m);
            }else {
                result = result + "/" + tp.get(m);
            }
        }
        return result;
    }
}