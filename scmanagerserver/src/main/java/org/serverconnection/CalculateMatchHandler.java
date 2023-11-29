package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.dataclasses.Player;
import org.dataclasses.Statistic;
import org.dataclasses.Team;
import org.dataclasses.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalculateMatchHandler  extends AbstractHandler {
    private Integer[] goals;

    @Override
    public void handle(String body, Request originalRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = body.split("/");
        if (splitTarget[1].equals("user") && splitTarget[2].equals("startMatch")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");


            originalRequest.setHandled(true);
        }
    }

    public Integer[][] startTournamentMatch(String username1, String username2) throws SQLException {
        startMatch(username1, username2);

        UserEditHandler tp = new UserEditHandler();
        Integer[][] statisticArray = new Integer[2][3];

        String databaseURL = "jdbc:h2:~/scmanagerdb";
        JdbcConnectionSource conn = new JdbcConnectionSource(databaseURL);
        Dao<User, Integer> userDao = DaoManager.createDao(conn, User.class);
        int idUser1 = tp.getIDFromUsername(username1);
        int idUser2 = tp.getIDFromUsername(username2);
        if (goals[0] > goals[1]) {
            //User1 hat gewonnen
            statisticArray[0][0] = idUser1;
            statisticArray[0][1] = goals[0];
            statisticArray[0][2] = goals[1];
            statisticArray[1][0] = idUser2;
            statisticArray[1][1] = goals[1];
            statisticArray[1][2] = goals[0];
        } else if (goals[0] < goals[1]) {
            //User2 hat gewonnen
            statisticArray[0][0] = idUser2;
            statisticArray[0][1] = goals[1];
            statisticArray[0][2] = goals[0];
            statisticArray[1][0] = idUser1;
            statisticArray[1][1] = goals[0];
            statisticArray[1][2] = goals[1];
        } else if (goals[0] == goals[1]) {
            //Unentschieden
            statisticArray[0][0] = 0;
            statisticArray[0][1] = goals[0];
            statisticArray[0][2] = goals[1];
            statisticArray[1][0] = 0;
            statisticArray[1][1] = goals[1];
            statisticArray[1][2] = goals[0];
        }
        return statisticArray;
    }

    public String startMatch(String username1, String username2) throws SQLException {
        String result = "";
        String databaseURL = "jdbc:h2:~/scmanagerdb";
        JdbcConnectionSource conn = new JdbcConnectionSource(databaseURL);
        //Torzähler erstellen
        goals = new Integer[2];
        goals[0] = 0;
        goals[1] = 0;

        //IDs von den Spielern holen
        UserEditHandler tp = new UserEditHandler();
        int user1ID = tp.getIDFromUsername(username1);
        int user2ID = tp.getIDFromUsername(username2);

        Dao<User, Integer> userDao = DaoManager.createDao(conn, User.class);
        User user1 = userDao.queryForId(user1ID);
        User user2 = userDao.queryForId(user2ID);

        Team team1 = new Team();
        Team team2 = new Team();

        Dao<Team, Integer> teamDao = DaoManager.createDao(conn, Team.class);
        List<Team> listTeam1 = teamDao.queryForAll();
        for(int i=0;i<listTeam1.size();i++){
            if(listTeam1.get(i).getUSER().getID()==user1ID){
                team1 = listTeam1.get(i);
            }
            if(listTeam1.get(i).getUSER().getID()==user2ID){
                team2 = listTeam1.get(i);
            }
        }
        //Heim- oder Auswärtsspiel
        Random rt = new Random();
        int stadium = rt.nextInt(2);
        if (stadium == 0) {
            //+ SEP Einnahmen
            StadiumHandler sth = new StadiumHandler();
            int stadiumUpgrades = sth.getStadiumStatus(user1);
            if(stadiumUpgrades == 0 || stadiumUpgrades == 3 || stadiumUpgrades == 4){
                user1.setPoints(user1.getPoints() + 20);
                userDao.update(user1);
            }else if(stadiumUpgrades == 5 || stadiumUpgrades == 6){
                user1.setPoints(user1.getPoints() + 50);
                userDao.update(user1);
            }else if(stadiumUpgrades == 7 || stadiumUpgrades == 8){
                user1.setPoints(user1.getPoints() + 75);
                userDao.update(user1);
            }else if(stadiumUpgrades == 9){
                user1.setPoints(user1.getPoints() + 150);
                userDao.update(user1);
            }
        } else if (stadium == 1) {
            //+ SEP Einnahmen
            StadiumHandler sth = new StadiumHandler();
            int stadiumUpgrades = sth.getStadiumStatus(user2);
            if(stadiumUpgrades == 0 || stadiumUpgrades == 3 || stadiumUpgrades == 4){
                user2.setPoints(user2.getPoints() + 20);
                userDao.update(user2);
            }else if(stadiumUpgrades == 5 || stadiumUpgrades == 6){
                user2.setPoints(user2.getPoints() + 50);
                userDao.update(user2);
            }else if(stadiumUpgrades == 7 || stadiumUpgrades == 8){
                user2.setPoints(user2.getPoints() + 75);
                userDao.update(user2);
            }else if(stadiumUpgrades == 9){
                user2.setPoints(user2.getPoints() + 150);
                userDao.update(user2);
            }
        }
        //Cheat "Rot-Weiss Essen"
        if(team1.getTEAMNAME().equals("Rot-Weiss Essen") && team2.getTEAMNAME().equals("Rot-Weiss Essen")){
            Dao<Statistic, Integer> statisticsDao = DaoManager.createDao(conn, Statistic.class);
            result = "Draw";
            System.out.println("User1 & User2 are using Cheats");
            Statistic statisticUser1 = new Statistic(user1, false, goals[0], goals[1], team1.getFORMATION(), team1.getPlayersList(), user2);
            Statistic statisticUser2 = new Statistic(user2, false, goals[1], goals[0], team2.getFORMATION(), team2.getPlayersList(), user1);
            statisticsDao.create(statisticUser1);
            statisticsDao.create(statisticUser2);
        }else if(team1.getTEAMNAME().equals("Rot-Weiss Essen")){
            Dao<Statistic, Integer> statisticsDao = DaoManager.createDao(conn, Statistic.class);
            Statistic statisticUser1 = new Statistic(user1, true, goals[0], goals[1], team1.getFORMATION(), team1.getPlayersList(), user2);
            Statistic statisticUser2 = new Statistic(user2, false, goals[1], goals[0], team2.getFORMATION(), team2.getPlayersList(), user1);
            statisticsDao.create(statisticUser1);
            statisticsDao.create(statisticUser2);
            user1.setPoints(user1.getPoints() + 100);
            userDao.update(user1);
            result = "User1";
            System.out.println("User1 has won with Cheats");
        }else if(team2.getTEAMNAME().equals("Rot-Weiss Essen")){
            Dao<Statistic, Integer> statisticsDao = DaoManager.createDao(conn, Statistic.class);
            Statistic statisticUser1 = new Statistic(user1, false, goals[0], goals[1], team1.getFORMATION(), team1.getPlayersList(), user2);
            Statistic statisticUser2 = new Statistic(user2, true, goals[1], goals[0], team2.getFORMATION(), team2.getPlayersList(), user1);
            statisticsDao.create(statisticUser1);
            statisticsDao.create(statisticUser2);
            user2.setPoints(user2.getPoints() + 100);
            userDao.update(user2);
            result = "User2";
            System.out.println("User2 has won with Cheats");
        }else {
            //zufällige Anzahl an Kriterien
            Random r = new Random();
            int algorithms = r.nextInt(7);

            //zufällige Algortihmen auswählen
            for (int i = 0; i < algorithms; i++) {
                int tmp = r.nextInt(6);
                if (tmp == 0) {
                    goals = this.SSPESAlgorithm(goals);
                } else if (tmp == 1) {
                    goals = this.teamStrenghAlgorithm(goals, team1, team2);
                } else if (tmp == 2) {
                    goals = this.midfieldplayer(goals, team1, team2);
                } else if (tmp == 3) {
                    goals = this.randomPlayer(goals, team1, team2);
                } else if (tmp == 4) {
                    goals = this.tenPlayers(goals, team1, team2);
                } else if (tmp == 5) {
                    goals = this.randomNumber(goals);
                }
            }

            //Ergebnis auswerten
            Dao<Statistic, Integer> statisticsDao = DaoManager.createDao(conn, Statistic.class);
            if (goals[0] > goals[1]) {
                //User1 hat gewonnen
                Statistic statisticUser1 = new Statistic(user1, true, goals[0], goals[1], team1.getFORMATION(), team1.getPlayersList(), user2);
                Statistic statisticUser2 = new Statistic(user2, false, goals[1], goals[0], team2.getFORMATION(), team2.getPlayersList(), user1);
                statisticsDao.create(statisticUser1);
                statisticsDao.create(statisticUser2);
                user1.setPoints(user1.getPoints() + 100);
                userDao.update(user1);
                result = "User1";
            } else if (goals[0] < goals[1]) {
                //User2 hat gewonnen
                Statistic statisticUser1 = new Statistic(user1, false, goals[0], goals[1], team1.getFORMATION(), team1.getPlayersList(), user2);
                Statistic statisticUser2 = new Statistic(user2, true, goals[1], goals[0], team2.getFORMATION(), team2.getPlayersList(), user1);
                statisticsDao.create(statisticUser1);
                statisticsDao.create(statisticUser2);
                user2.setPoints(user2.getPoints() + 100);
                userDao.update(user2);
                result = "User2";
            } else if (goals[0] == goals[1]) {
                //Unentschieden
                Statistic statisticUser1 = new Statistic(user1, false, goals[0], goals[1], team1.getFORMATION(), team1.getPlayersList(), user2);
                Statistic statisticUser2 = new Statistic(user2, false, goals[1], goals[0], team2.getFORMATION(), team2.getPlayersList(), user1);
                statisticsDao.create(statisticUser1);
                statisticsDao.create(statisticUser2);
                result = "Draw";
            }
        }
        AnswerMatchRequestHandler am = new AnswerMatchRequestHandler();
        am.deleteRequest(user2ID, user1ID);
        return result;
    }

    public Integer[] SSPESAlgorithm(Integer[] goals){
        String user1, user2;
        ArrayList<String> SSPESsymbols = new ArrayList<>();
        //Richtige Hierarchie
        SSPESsymbols.add("Spock");
        SSPESsymbols.add("Echse");
        SSPESsymbols.add("Stein");
        SSPESsymbols.add("Papier");
        SSPESsymbols.add("Schere");
        int randomSymbol1 = (int)(Math.random()*4);
        int randomSymbol2 = (int)(Math.random()*4);
        user1 = SSPESsymbols.get(randomSymbol1);
        user2 = SSPESsymbols.get(randomSymbol2);

        //11 Bedingungen statt 21 :D
        if(user1.equals(user2)) {
            goals[0] = goals[0] + 1;
            goals[1] = goals[1] + 1;
        }else if(user1.equals("Spock") && user2.equals("Schere")){
            goals[0] = goals[0] + 1;
        }else if(user1.equals("Schere") && user2.equals("Spock")){
            goals[1] = goals[1] + 1;
        }else if(user1.equals("Stein") && user2.equals("Schere")){
            goals[0] = goals[0] + 1;
        }else if(user1.equals("Schere") && user2.equals("Stein")){
            goals[1] = goals[1] + 1;
        }else if(user1.equals("Echse") && user2.equals("Papier")){
            goals[0] = goals[0] + 1;
        }else if(user1.equals("Papier") && user2.equals("Echse")){
            goals[1] = goals[1] + 1;
        }else if(user1.equals("Spock") && user2.equals("Stein")){
            goals[0] = goals[0] + 1;
        }else if(user1.equals("Stein") && user2.equals("Spock")){
            goals[1] = goals[1] + 1;
        }else if(randomSymbol1 > randomSymbol2){
            goals[0] = goals[0] + 1;
        }else if (randomSymbol1 < randomSymbol2){
            goals[1] = goals[1] + 1;
        }

        return goals;
    }

    public Integer[] teamStrenghAlgorithm(Integer[] goals, Team team1, Team team2) {
        try {
        ArrayList<Player> players1 = this.getPlayers(team1);
        ArrayList<Player> players2 = this.getPlayers(team2);

        //get the strengh from the atttackers
        double attacker1 = this.getAttackerStrengh(players1);
        double attacker2 = this.getAttackerStrengh(players2);


        //get the strengh from the defenders + goalkeeper
        double strengh1 = this.teamStrenghHelpMethod(players1);
        double strengh2 = this.teamStrenghHelpMethod(players2);

        //result
        double algorithm1 = attacker1 / strengh2;
        double algorithm2 = attacker2 / strengh1;

        if(algorithm1 > algorithm2){
            goals[0] = goals[0] + 1;
        }else if(algorithm1 < algorithm2){
            goals[1] = goals[1] + 1;
        }else if(algorithm1 == algorithm2){
            goals[0] = goals[0] + 1;
            goals[1] = goals[1] + 1;
        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return goals;
    }

    public double teamStrenghHelpMethod(ArrayList<Player> players){
        ArrayList<Player> goalkeeper = new ArrayList<>();
        ArrayList<Player> defender = new ArrayList<>();
        for(int i=0;i<players.size();i++){
            if(players.get(i).getPosition().equals("Torwart")){
                goalkeeper.add(players.get(i));
            }
            if(players.get(i).getPosition().equals("Abwehr")){
                defender.add(players.get(i));
            }
        }
        double strenghDefender = 0;
        double strenghGoalkeeper = 0;

        for(int i=0;i<defender.size();i++){
            strenghDefender = strenghDefender + defender.get(i).getStrength();
        }
        strenghDefender = strenghDefender / defender.size();
        for(int i=0;i<goalkeeper.size();i++){
            strenghGoalkeeper = strenghGoalkeeper + goalkeeper.get(i).getStrength();
        }
        strenghGoalkeeper = strenghGoalkeeper / goalkeeper.size();

        //NaN abfangen
        if(Double.isNaN(strenghDefender) && Double.isNaN(strenghGoalkeeper)){
            return 0.01;
        }else if(Double.isNaN(strenghDefender)){
            return 0.01 + strenghGoalkeeper;
        }else if(Double.isNaN(strenghGoalkeeper)){
            return strenghDefender + 0.01;
        }else {
            return strenghDefender + strenghGoalkeeper;
        }
    }

    public double getAttackerStrengh(ArrayList<Player> team){
        ArrayList<Player> attacker = new ArrayList<>();
        for(int i=0;i<team.size();i++){
            if(team.get(i).getPosition().equals("Angriff")){
                attacker.add(team.get(i));
            }
        }

        double strenghAttacker = 0;

        for(int i=0;i<attacker.size();i++){
            strenghAttacker = strenghAttacker + attacker.get(i).getStrength();
        }

        strenghAttacker = strenghAttacker / attacker.size();
        if(Double.isNaN(strenghAttacker)){
            return 0.01;
        }else {
            return strenghAttacker;
        }

    }

    public Integer[] midfieldplayer(Integer[] goals, Team team1, Team team2) {
        try {
            ArrayList<Player> players1 = this.getPlayers(team1);
            ArrayList<Player> players2 = this.getPlayers(team2);

            //entfernt Spieler mit Position !Mittelfeld
            for(int i=0;i<players1.size();i++) {
                if (!players1.get(i).getPosition().equals("Mittelfeld")) {
                    players1.remove(i);
                }
            }
            for(int i=0;i<players2.size();i++){
                if(!players2.get(i).getPosition().equals("Mittelfeld")){
                    players2.remove(i);
                }
            }
            //durchschnittliche Stärke berechnen
            double strength1 = 0;
            double strength2 = 0;
            for(int i=0;i<players1.size();i++){
                strength1 = strength1 + players1.get(i).getStrength();
            }
            for(int i=0;i<players2.size();i++){
                strength2 = strength2 + players2.get(i).getStrength();
            }
            //NaN abfangen
            if(Double.isNaN(strength1)){
                strength1=0.01;
            }if (Double.isNaN(strength2)) {
                strength2=0.01;
            }
            strength1 = strength1 / players1.size();
            strength2 = strength2 / players2.size();

            if(strength1 > strength2){
                goals[0] = goals[0] + 1;
            }else if(strength1 < strength2){
                goals[1] = goals[1] + 1;
            }else if(strength1 == strength2){
                goals[0] = goals[0] + 1;
                goals[1] = goals[1] + 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return goals;
    }

    public Integer[] randomPlayer(Integer[] goals, Team team1, Team team2) {
        try{
            ArrayList<Player> players1 = this.getReserve(team1);
            ArrayList<Player> players2 = this.getReserve(team2);

            ArrayList<Player> randomPlayers1 = new ArrayList<>();
            ArrayList<Player> randomPlayers2 = new ArrayList<>();

            //3 random Spieler aus jedem Team abspeichern
            Random randi = new Random();
            int t = 9;
            for(int i=0;i<3;i++){
                int r = randi.nextInt(t);
                randomPlayers1.add(players1.get(r));
                players1.remove(r);
                t--;
            }
            t = 9;
            for(int i=0;i<3;i++){
                int r = randi.nextInt(t);
                randomPlayers2.add(players2.get(r));
                players2.remove(r);
                t--;
            }
            //Stärken berechnen
            double strength1 = 0;
            double strength2 = 0;
            for(int i=0;i<randomPlayers1.size();i++){
                strength1 = strength1 + randomPlayers1.get(i).getStrength();
            }
            for(int i=0;i<randomPlayers2.size();i++){
                strength2 = strength2 + randomPlayers2.get(i).getStrength();
            }

            //NaN abfangen
            if(Double.isNaN(strength1)){
                strength1=0.01;
            }if (Double.isNaN(strength2)) {
                strength2=0.01;
            }

            strength1 = strength1 / 3;
            strength2 = strength2 / 3;

            //Gewinner ermitteln
            if(strength1 > strength2){
                goals[0] = goals[0] + 1;
            }else if(strength1 < strength2){
                goals[1] = goals[1] + 1;
            }else if(strength1 == strength2){
                goals[0] = goals[0] + 1;
                goals[1] = goals[1] + 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return goals;
    }

    public Integer[] tenPlayers (Integer[] goals, Team team1, Team team2) {
        try{
            ArrayList<Player> players1 = this.getPlayers(team1);
            ArrayList<Player> players2 = this.getPlayers(team2);

            //entfernt Spieler mit Position Torwart
            for(int i=0;i<players1.size();i++) {
                if (players1.get(i).getPosition().equals("Torwart")) {
                    players1.remove(i);
                }
            }
            for(int i=0;i<players2.size();i++){
                if(players2.get(i).getPosition().equals("Torwart")){
                    players2.remove(i);
                }
            }
            //Interne Ergebnisse der 10 Vergleiche
            int winsPlayer1 = 0;
            int winsPlayer2 = 0;

            for (int i = 0; i<10; i++){
                //Zehn Paare mit zufälligen Spielern, theoretisch auch doppelte,
                Random random = new Random();
                int randomPlayer1 =  random.nextInt(9);
                int randomPlayer2 =  random.nextInt(9);
                if (players1.get(randomPlayer1).getStrength() > players2.get(randomPlayer2).getStrength()){
                    //Spieler aus Team 1 ist bei Paar i stärker als Spieler aus Team 2
                    winsPlayer1++;
                }else if (players1.get(randomPlayer1).getStrength() < players2.get(randomPlayer2).getStrength()){
                    //Spieler aus Team 2 ist bei Paar i stärler als Spieler aus Team 1
                    winsPlayer2++;
                }else{
                    //Spieler sind gleich stark
                    winsPlayer1++;
                    winsPlayer2++;
                }
            }
            //Auswertung internes Ergebnis
            if (winsPlayer1 > winsPlayer2){
                //Wenn Spieler 1 mehr interne Siege hat als Spieler 2
                goals[0] = goals[0] + 1;
            }else if (winsPlayer1 < winsPlayer2){
                //Wenn Spieler 2 mehr interne Siege hat als SPieler 1
                goals[1] = goals[1] + 1;
            }else {
                //Spieler haben gleichstand
                goals[0] = goals[0] + 1;
                goals[1] = goals[1] + 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return goals;
    }

    public Integer[] randomNumber(Integer[] goals){
        Random random = new Random();
        int randomPlayer1 = random.nextInt(10);
        int randomPlayer2 = random.nextInt(10);
        if(randomPlayer1 > randomPlayer2){
            goals[0] = goals[0] + 1;
        }else if(randomPlayer1 < randomPlayer2){
            goals[1] = goals[1] + 1;
        }else if(randomPlayer1 == randomPlayer2){
            goals[0] = goals[0] + 1;
            goals[1] = goals[1] + 1;
        }
        return goals;
    }

    public ArrayList<Player> getPlayers(Team team) throws SQLException {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<String> playersname = team.getSETUP();
        ArrayList<Integer> playersIDs = team.getPlayersList();

        //speichert alle Spieler aus dem Team
        String databaseURL = "jdbc:h2:~/scmanagerdb";
        JdbcConnectionSource conn = new JdbcConnectionSource(databaseURL);
        Dao<Player, Integer> playerDao = DaoManager.createDao(conn, Player.class);
        for(Integer id:playersIDs){
            for(String name:playersname) {
                if (playerDao.queryForId(id).getName().equals(name)) {
                    players.add(playerDao.queryForId(id));
                }
            }
        }
        return players;
    }

    public ArrayList<Player> getReserve(Team team) throws SQLException {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<String> playersname = team.getRESERVE();
        ArrayList<Integer> playersIDs = team.getPlayersList();

        //speichert alle Spieler von der Reservebank
        String databaseURL = "jdbc:h2:~/scmanagerdb";
        JdbcConnectionSource conn = new JdbcConnectionSource(databaseURL);
        Dao<Player, Integer> playerDao = DaoManager.createDao(conn, Player.class);
        for(Integer id:playersIDs){
            for(String name:playersname) {
                if (playerDao.queryForId(id).getName().equals(name)) {
                    players.add(playerDao.queryForId(id));
                }
            }
        }
        return players;
    }
}