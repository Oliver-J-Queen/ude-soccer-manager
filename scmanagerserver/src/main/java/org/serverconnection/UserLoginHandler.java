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

public class UserLoginHandler  extends AbstractHandler {

    /**
     * @author Yelle Lieder (handle method)
     * had some inspiration from https://git.uni-due.de/sktrkley/self-study-exercises-for-programming.git, nothing directly copied
     */
    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user")&&splitTarget[2].equals("login")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            loginUser(response.getWriter(), splitTarget[3], splitTarget[4]);
            originalRequest.setHandled(true);
        }
    }

    public void loginUser(PrintWriter input, String username, String password){
        if(this.checkUsername(username)==true){
            if(this.checkPassword(username, password)==true){
                this.setStatus(username, "Online");
                input.print("Login Successful");
            }
            else{
                input.print("Passwort falsch");
            }
        }else{
            input.print("User nicht registriert");
        }
    }

    public boolean checkUsername(String username){
        try{
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);

            List<User> listuser =  userDao.queryForAll();

            for(int i=0;i<listuser.size();i++) {
                if(listuser.get(i).getUsername().equalsIgnoreCase(username)) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean checkPassword(String username, String password){
        try{
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);

            List<User> listuser =  userDao.queryForAll();
            User tmp = new User();
            for(int i=0;i<listuser.size();i++) {
                if(listuser.get(i).getUsername().equalsIgnoreCase(username)) {
                    tmp = listuser.get(i);
                }
            }

            if(tmp.getPASSWORD().equalsIgnoreCase(password)){
                return true;
            }else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void setStatus(String username, String status){
        try{
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);

            UserEditHandler tp = new UserEditHandler();
            int id = tp.getIDFromUsername(username);

            User newUser = userDao.queryForId(id);
            newUser.setSTATUS(status);
            userDao.update(newUser);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
