package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import org.dataclasses.Team;
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
import java.util.regex.Pattern;

public class UserRegistrationHandler  extends AbstractHandler {

    /**
     * @author Yelle Lieder (handle method)
     * had some inspiration from https://git.uni-due.de/sktrkley/self-study-exercises-for-programming.git, nothing directly copied
     */

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user")&&splitTarget[2].equals("registration")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            registerNewUser(response.getWriter(), splitTarget[3], splitTarget[4], splitTarget[5]);
            originalRequest.setHandled(true);
        }
    }

    public void registerNewUser(PrintWriter input, String username, String email, String password){
        if (checkUsername(username) == false) {
            if(checkEmailisVaild(email)==true) {
                if (checkEmail(email) == false) {
                    try {
                        Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
                        TableUtils.createTableIfNotExists(ServerController.userConn, User.class);
                        User tmp = new User(email, username, password, "Offline", false);
                        userDao.create(tmp);

                        Dao<Team, Integer> teamDao = DaoManager.createDao(ServerController.userConn, Team.class);
                        TableUtils.createTableIfNotExists(ServerController.userConn, Team.class);
                        ArrayList<Integer> players = new ArrayList<>();
                        Team tmpTeam = new Team(tmp.getUsername()+"s Team", tmp, players);
                        teamDao.create(tmpTeam);
                        UserLoginHandler tp = new UserLoginHandler();
                        tp.setStatus(username, "Online");
                        input.print("Registration successful");
                        System.out.println("Registration passed..");
                    } catch (SQLException  throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    input.print("Email already taken");
                }
            }else{
                input.print("Email is not valid");
            }
        }else{
            input.print("Username already taken");
        }


    }

    public boolean checkUsername(String username){
        try{
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            TableUtils.createTableIfNotExists(ServerController.userConn, User.class);
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

    public boolean checkEmail(String email){
        try{
            Dao<User, String> userDao = DaoManager.createDao(ServerController.userConn, User.class);
            TableUtils.createTableIfNotExists(ServerController.userConn, User.class);
            List<User> listuser =  userDao.queryForAll();

            for(int i=0;i<listuser.size();i++) {
                if(listuser.get(i).getEMAIL().equalsIgnoreCase(email)) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean checkEmailisVaild(String email){
        //the regular expressions are from the Website: https://www.geeksforgeeks.org/check-email-address-valid-not-java/
        String emailSymbols = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$";
        
        Pattern tmp = Pattern.compile(emailSymbols);
        return tmp.matcher(email).matches();
        }
    }