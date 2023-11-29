package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
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

public class AnswerMatchRequestHandler extends AbstractHandler {

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("answerMatchRequest")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            answerMatchRequest(response.getWriter(), splitTarget[3], splitTarget[4], splitTarget[5]);
            originalRequest.setHandled(true);
        }
    }

    public void answerMatchRequest(PrintWriter input, String username, String opponentUsername, String answer){
        UserEditHandler tp = new UserEditHandler();
        int idUser = tp.getIDFromUsername(username);
        int idOpponent = tp.getIDFromUsername(opponentUsername);
        if(answer.equals("angenommen")){
            try {
                Team team1 = new Team();
                Team team2 = new Team();
                String databaseURL = "jdbc:h2:~/scmanagerdb";
                JdbcConnectionSource conn = new JdbcConnectionSource(databaseURL);
                Dao<Team, Integer> teamDao = DaoManager.createDao(conn, Team.class);
                List<Team> listTeam = teamDao.queryForAll();
                for(int i=0;i<listTeam.size();i++){
                    if(listTeam.get(i).getUSER().getID()==idUser){
                        team1 = listTeam.get(i);
                    }
                    if(listTeam.get(i).getUSER().getID()==idOpponent){
                        team2 = listTeam.get(i);
                    }
                }
            if(team1.getSETUP()!=null && team1.getRESERVE()!=null) {
                if(team2.getSETUP()!=null && team2.getRESERVE()!=null) {
                    CalculateMatchHandler matchHandler = new CalculateMatchHandler();
                    String result = matchHandler.startMatch(username, opponentUsername);
                    if (result.equals("User1")) {
                        input.print("User1");
                    } else if (result.equals("User2")) {
                        input.print("User2");
                    } else if (result.equals("Draw")) {
                        input.print("Draw");
                    }
                }else{
                    input.print("Opponent");
                }
            }else{
                input.print("Null");
            }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(answer.equals("abgelehnt")){
            deleteRequest(idOpponent, idUser);
            input.print("Request deleted");
        }else{
            System.out.println("Something went wrong");
        }
    }

    public void deleteRequest(int idFromSender, int idFromReceiver){
        try {
            String databaseURL = "jdbc:h2:~/scmanagerdb";
            JdbcConnectionSource conn = new JdbcConnectionSource(databaseURL);
            Dao<User, Integer> userDao = DaoManager.createDao(conn, User.class);
            User receiver = userDao.queryForId(idFromReceiver);
            if(receiver.getMATCHREQUESTS()!=null) {
                ArrayList<Integer> requests = receiver.getMATCHREQUESTS();
                for (int i = 0; i < requests.size(); i++) {
                    if (requests.get(i) == idFromSender) {
                        requests.remove(i);
                    }
                }
                receiver.setMATCHREQUESTS(requests);
                userDao.update(receiver);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}