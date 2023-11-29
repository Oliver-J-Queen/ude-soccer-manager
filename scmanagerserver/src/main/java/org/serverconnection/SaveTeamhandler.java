package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.Team;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;


public class SaveTeamhandler extends AbstractHandler {
    ConnectionSource src;
    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equalsIgnoreCase("saveteam")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            ArrayList<String> team = new ArrayList<>();
            team.add(splitTarget[3].replace("_"," "));
            team.add(splitTarget[4].replace("_"," "));
            team.add(splitTarget[5].replace("_"," "));
            team.add(splitTarget[6].replace("_"," "));
            team.add(splitTarget[7].replace("_"," "));
            team.add(splitTarget[8].replace("_"," "));
            team.add(splitTarget[9].replace("_"," "));
            team.add(splitTarget[10].replace("_"," "));
            team.add(splitTarget[11].replace("_"," "));
            team.add(splitTarget[12].replace("_"," "));
            team.add(splitTarget[13].replace("_"," "));

            //löscht Stärke aus namen
            ArrayList<String> splitedTargetNoStrenght = new ArrayList<>();
            for (String l : team){
                    //Jeder Spielername + Strenght gesplittet an Leerzeichen
                    String[] tmpSplitString =  l.split(" ");
                    String fullname = "";
                    for (int i=0;i<tmpSplitString.length;i++){
                        if (tmpSplitString[i+1].equals("Strength:")){
                            int length = tmpSplitString[i].length();
                            String temp = "";
                            for(int n=0;n<length-1;n++) {
                                temp += tmpSplitString[i].charAt(n);
                            }
                            fullname += " "+temp;
                            splitedTargetNoStrenght.add(fullname);
                            break;
                        }else{
                            if(fullname.equals("")) {
                                fullname = tmpSplitString[i];
                            }else{
                                fullname += " "+ tmpSplitString[i];
                            }
                        }
                    }
            }
            saveFormation(response.getWriter(),splitTarget[2], splitedTargetNoStrenght);
        }
        else if(splitTarget[1].equalsIgnoreCase("savebench")){
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            ArrayList<String> team = new ArrayList<>();
            team.add(splitTarget[3].replace("_"," "));
            team.add(splitTarget[4].replace("_"," "));
            team.add(splitTarget[5].replace("_"," "));
            team.add(splitTarget[6].replace("_"," "));
            team.add(splitTarget[7].replace("_"," "));
            team.add(splitTarget[8].replace("_"," "));
            team.add(splitTarget[9].replace("_"," "));
            team.add(splitTarget[10].replace("_"," "));
            team.add(splitTarget[11].replace("_"," "));

            //löscht Stärke aus namen
            ArrayList<String> splitedTargetNoStrenght = new ArrayList<>();
            for (String l : team){
                //Jeder Spielername + Strenght gesplittet an Leerzeichen
                String[] tmpSplitString =  l.split(" ");
                String fullname = "";
                for (int i=0;i<tmpSplitString.length;i++){
                    if (tmpSplitString[i+1].equals("Strength:")){
                        int length = tmpSplitString[i].length();
                        String temp = "";
                        for(int n=0;n<length-1;n++) {
                            temp += tmpSplitString[i].charAt(n);
                        }
                        fullname += " "+temp;
                        splitedTargetNoStrenght.add(fullname);
                        break;
                    }else{
                        if(fullname.equals("")) {
                            fullname = tmpSplitString[i];
                        }else{
                            fullname += " "+ tmpSplitString[i];
                        }
                    }
                }
            }
            saveFormation(response.getWriter(),splitTarget[2], splitedTargetNoStrenght);
        }
        else if(splitTarget[1].equalsIgnoreCase("getstartingeleven")){
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            getFormation(response.getWriter(),splitTarget[2]);
        }
        else if(splitTarget[1].equalsIgnoreCase("getbench")){
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            getBench(response.getWriter(),splitTarget[2]);
        }
        originalRequest.setHandled(true);
    }

    public void saveFormation(PrintWriter input, String userID, ArrayList<String> teamList) {
        try {
            src = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
            Dao<Team, String> teamDao = DaoManager.createDao(src, Team.class);
            for (Team team : teamDao) {
                if (Integer.toString(team.getUSER().getID()).equalsIgnoreCase(userID)) {
                    if(teamList.size() == 11) {
                        team.setSETUP(teamList);
                        teamDao.update(team);
                        input.print("success");
                        break;
                    } else if(teamList.size()==9){
                        team.setRESERVE(teamList);
                        teamDao.update(team);
                        input.print("success");
                        break;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getFormation(PrintWriter input, String userID){
        try{
            src = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
            Dao<Team, String>teamsDao = DaoManager.createDao(src , Team.class);
            QueryBuilder<Team, String> TeamQueryBuilder = teamsDao.queryBuilder();
            TeamQueryBuilder.where().eq("USERID", userID);
            PreparedQuery<Team> teamQuery = TeamQueryBuilder.prepare();

            Team tmpTeam = teamsDao.queryForFirst(teamQuery);

            ArrayList<String> tp = tmpTeam.getSETUP();

            input.println(new Gson().toJson(tp, ArrayList.class));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void getBench(PrintWriter input, String userID){
        try{
            src = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");

            Dao<Team, String>teamsDao = DaoManager.createDao(src , Team.class);
            QueryBuilder<Team, String> TeamQueryBuilder = teamsDao.queryBuilder();
            TeamQueryBuilder.where().eq("USERID", userID);
            PreparedQuery<Team> teamQuery = TeamQueryBuilder.prepare();

            Team tmpTeam = teamsDao.queryForFirst(teamQuery);

            ArrayList<String> tp = tmpTeam.getRESERVE();

            input.println(new Gson().toJson(tp, ArrayList.class));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



}