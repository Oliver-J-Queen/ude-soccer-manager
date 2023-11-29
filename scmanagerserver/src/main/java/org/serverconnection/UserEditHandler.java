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
import java.util.List;

public class UserEditHandler  extends AbstractHandler {

    /**
     * @author Yelle Lieder (handle method)
     * had some inspiration from https://git.uni-due.de/sktrkley/self-study-exercises-for-programming.git, othing directly copied
     */
    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user")&&splitTarget[2].equals("edit")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            if(splitTarget[3].equals("username")){
                editUserName(response.getWriter(), splitTarget[4], splitTarget[5]);
            }
            if(splitTarget[3].equals("password")){
                editUserPassword(response.getWriter(), splitTarget[4], splitTarget[5]);
            }
            if(splitTarget[3].equals("email")){
                editUserEmail(response.getWriter(), splitTarget[4], splitTarget[5]);
            }
            originalRequest.setHandled(true);
        }
    }

    public void editUserName(PrintWriter input, String username, String newUsername){
        UserLoginHandler ulh = new UserLoginHandler();
        if(ulh.checkUsername(newUsername)==true){
            input.print("Username already exists");
        }else {
            try {
                Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
                int i = this.getIDFromUsername(username);
                User tmp = userDao.queryForId(i);
                tmp.setUSERNAME(newUsername);
                userDao.update(tmp);
                input.print(tmp.getUsername());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public void editUserPassword(PrintWriter input, String username, String newPassword){
        try{
            Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            int i = this.getIDFromUsername(username);
            User tmp = userDao.queryForId(i);
            tmp.setPASSWORD(newPassword);
            userDao.update(tmp);
            input.print("Password changed successfully");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void editUserEmail(PrintWriter input, String username, String newEmail){
        UserRegistrationHandler urh = new UserRegistrationHandler();
        if(urh.checkEmailisVaild(newEmail)==false){
            input.print("Email is not valid");
        }else {
            if (urh.checkEmail(newEmail) == true) {
                input.print("Email already exists");
            } else {
                try {
                    Dao<User, Integer> userDao = DaoManager.createDao(ServerController.userConn, User.class);
                    int i = this.getIDFromUsername(username);
                    User tmp = userDao.queryForId(i);
                    tmp.setEMAIL(newEmail);
                    userDao.update(tmp);
                    input.print("Email changed successfully.");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public int getIDFromUsername(String username){
        try{
            ConnectionSource cs = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
            Dao<User, String> userDao = DaoManager.createDao(cs, User.class);

            List<User> listuser =  userDao.queryForAll();
            User tmp = new User();
            for(int i=0;i<listuser.size();i++) {
                if(listuser.get(i).getUsername().equalsIgnoreCase(username)) {
                    tmp = listuser.get(i);
                }
            }
            return tmp.getID();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
}
