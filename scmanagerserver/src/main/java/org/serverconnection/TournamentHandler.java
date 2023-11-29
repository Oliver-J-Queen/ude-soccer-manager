package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.dataclasses.Team;
import org.dataclasses.Tournament;
import org.dataclasses.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.runTournament.runTournament;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class TournamentHandler extends AbstractHandler {

    ConnectionSource CONNECTION;

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = target.split("/");
        if (splitTarget[1].equals("tournament")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");

            if(splitTarget[2].equals("create")) {
                try {
                    createTournament(response.getWriter(), splitTarget[3], splitTarget[4], splitTarget[5], splitTarget[6], splitTarget[7]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(splitTarget[2].equals("join")){
                try {
                    joinTournament(response.getWriter(), splitTarget[3], splitTarget[4]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(splitTarget[2].equals("getAllTournaments")){
                try{
                    getTournament(response.getWriter());
                }catch (SQLException throwables){
                    throwables.printStackTrace();
                }
            }
            if(splitTarget[2].equals("start")){
                try{
                    startTournament(response.getWriter(), splitTarget[3]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        baseRequest.setHandled(true);
    }

    public void createTournament(PrintWriter input, String userID, String tournamentName, String mode, String entryFee, String maxPlayers) throws SQLException {

        int MAXPLAYERS = Integer.parseInt(maxPlayers);
        int ENTRYFEE = Integer.parseInt(entryFee);


        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        CONNECTION = new JdbcConnectionSource(databaseUrl);

        TableUtils.createTableIfNotExists(CONNECTION, Tournament.class);
        Dao<Tournament, String> tDAO = DaoManager.createDao(CONNECTION, Tournament.class);
        if(!hasTeamSetup(userID)){
            input.print("teamerror");
        }
        else if(isEligible(userID, ENTRYFEE)) {
            Tournament temp = new Tournament(tournamentName, MAXPLAYERS, false, ENTRYFEE, mode);
            temp.addContestant(userID);

            tDAO.create(temp);

            reduceCredits(userID, Integer.toString(temp.getID()));
            String tournamentID = Integer.toString(temp.getID());
            Tournament temp2 = tDAO.queryForId(tournamentID);
            System.out.println("Tournament: " + temp2.getName() + " created by.");

            input.print("success");
        } else {
            input.print("fail");
        }
    }

    public void joinTournament(PrintWriter input, String userID, String tournamentID) throws SQLException {

        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        CONNECTION = new JdbcConnectionSource(databaseUrl);
        Dao<Tournament, String> TOURNAMENT = DaoManager.createDao(CONNECTION, Tournament.class);

        Tournament tournament = TOURNAMENT.queryForId(tournamentID);
        if(!hasTeamSetup(userID)){
            input.print("teamerror");
        }
        else if(!isFull(tournamentID) && isEligible(userID,tournamentID)) {
            if(!tournament.getTeilnehmer().contains(userID)) {
                tournament.addContestant(userID);
                reduceCredits(userID, tournamentID);
                TOURNAMENT.update(tournament);

                if(isFull(tournamentID)){
                    input.print("full");
                }else if(!isFull(tournamentID)){
                    input.print("success");
                }

            }else {
                input.print("duplicate");
            }
        } else if (isFull(tournamentID)){
            input.print("full");
        } else {
            input.print("failure");
        }
    }

    public void startTournament(PrintWriter input, String tournamentID) throws SQLException {

        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        CONNECTION = new JdbcConnectionSource(databaseUrl);
        Dao<Tournament, String> TOURNAMENT = DaoManager.createDao(CONNECTION, Tournament.class);
        Dao<User, String> USER = DaoManager.createDao(CONNECTION, User.class);

        ArrayList<User> joining = new ArrayList<>();

        Tournament temp = TOURNAMENT.queryForId(tournamentID);

        for(User user : USER){
            for(String userID : temp.getTeilnehmer()) {
                if (user.getID() == Integer.parseInt(userID)) {
                    User tp = USER.queryForId(userID);
                    joining.add(tp);
                }
            }
        }

        runTournament run = new runTournament();

        int[] competitors = run.runner(joining, temp.getMode(), temp.getEntryFee());

        input.print(new Gson().toJson(competitors));

        TOURNAMENT.delete(temp);
    }

    //RETURNS FULL LIST OF ALL TOURNAMENTS SAVED IN THE TOURNAMENTS TABLE
    public void getTournament(PrintWriter input) throws SQLException {

        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        CONNECTION = new JdbcConnectionSource(databaseUrl);
        Dao<Tournament, String> TOURNAMENT = DaoManager.createDao(CONNECTION, Tournament.class);

        ArrayList<Tournament> allTournaments = new ArrayList<>(TOURNAMENT.queryForAll());

        input.println(new Gson().toJson(allTournaments, ArrayList.class));
    }

    public boolean isFull(String tournamentID) throws SQLException {
        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        CONNECTION = new JdbcConnectionSource(databaseUrl);
        Dao<Tournament, String> TOURNAMENT = DaoManager.createDao(CONNECTION, Tournament.class);

        Tournament tournament = TOURNAMENT.queryForId(tournamentID);

        if(tournament.getTeilnehmer().size() == tournament.getMaxAnzahl()) {
            return true;
        }else {
            return false;
        }
    }

    public boolean isEligible(String userID, String tournamentID) throws SQLException {
        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        CONNECTION = new JdbcConnectionSource(databaseUrl);
        Dao<User, String> USER = DaoManager.createDao(CONNECTION, User.class);
        Dao<Tournament, String> TOURNAMENT = DaoManager.createDao(CONNECTION, Tournament.class);

        Tournament tournament = TOURNAMENT.queryForId(tournamentID);
        User user = USER.queryForId(userID);

        if(tournament.getEntryFee()<= user.getPoints()){
            return true;
        } else {
            return false;
        }
    }
    public boolean isEligible(String userID, int entryFee) throws SQLException {
        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        CONNECTION = new JdbcConnectionSource(databaseUrl);
        Dao<User, String> USER = DaoManager.createDao(CONNECTION, User.class);

        User user = USER.queryForId(userID);

        if(entryFee<= user.getPoints()){
            return true;
        } else {
            return false;
        }
    }
    public void reduceCredits(String userID, String tournamentID) throws SQLException {
        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        CONNECTION = new JdbcConnectionSource(databaseUrl);
        Dao<User, String> USER = DaoManager.createDao(CONNECTION, User.class);
        Dao<Tournament, String> TOURNAMENT = DaoManager.createDao(CONNECTION, Tournament.class);

        Tournament tournament = TOURNAMENT.queryForId(tournamentID);
        User user = USER.queryForId(userID);

        System.out.println(tournament.getEntryFee());
        user.setPoints(user.getPoints()-tournament.getEntryFee());

        USER.update(user);
    }
    public boolean hasTeamSetup(String userID) throws SQLException {
        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        CONNECTION = new JdbcConnectionSource(databaseUrl);
        Dao<Team, String> TEAM = DaoManager.createDao(CONNECTION, Team.class);

        for(Team cycle : TEAM){
            if(Integer.toString(cycle.getUSER().getID()).equals(userID)){
                if(cycle.getSETUP()==null||cycle.getRESERVE()==null){
                    return false;
                }
            }
        }
        return true;
    }
}
