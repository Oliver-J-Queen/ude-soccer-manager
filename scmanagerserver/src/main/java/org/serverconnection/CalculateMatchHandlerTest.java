package org.serverconnection;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;

class CalculateMatchHandlerTest {

    //calls: Integer[] SSPESAlgorithm(Integer[] goals)
    @Test
    void schereSteinPapierEchseSpockTest() {
        ArrayList<Integer[]> listOfAllGoalCalculationResults = new ArrayList<>();
        for (int iterator = 0; iterator < 1000; iterator++) {
            Integer[] tmpGoalsArray = new Integer[]{0, 0};
            CalculateMatchHandler matchHandlerObject = new CalculateMatchHandler();
            listOfAllGoalCalculationResults.add(matchHandlerObject.SSPESAlgorithm(tmpGoalsArray));
        }
        Assertions.assertTrue(notAllTheSame(listOfAllGoalCalculationResults));
    }

    //calls: Integer[] randomNumber(Integer[] goals)
    @Test
    void matchResultBasedOnRandomNumberTest() {
        ArrayList<Integer[]> listOfAllGoalCalculationResults = new ArrayList<>();
        for (int iterator = 0; iterator < 1000; iterator++) {
            Integer[] tmpGoalsArray = new Integer[]{0, 0};
            CalculateMatchHandler matchHandlerObject = new CalculateMatchHandler();
            listOfAllGoalCalculationResults.add(matchHandlerObject.randomNumber(tmpGoalsArray));
        }
        Assertions.assertTrue(notAllTheSame(listOfAllGoalCalculationResults));
    }

    //calls: Integer[] randomPlayer(Integer[] goals, Team team1, Team team2)
    @Test
    void randomPlayersFromBenchTest() throws SQLException {
        Team goodTeam = createTeam("good");
        Team badTeam = createTeam("bad");
        Integer[] tmpGoalsArray = new Integer[]{0, 0};
        CalculateMatchHandler matchHandlerObject = new CalculateMatchHandler();
        Assertions.assertTrue(firstTeamIsStronger(matchHandlerObject.randomPlayer(tmpGoalsArray, goodTeam, badTeam)));
    }

    //calls: startMatch()
    @Test
    void cheatTest() throws SQLException, IOException {
        String databaseURL = "jdbc:h2:~/scmanagerdb";
        JdbcConnectionSource conn = new JdbcConnectionSource(databaseURL);
        Dao<User, Integer> userDao = DaoManager.createDao(conn, User.class);
        Dao<Team, Integer> teamDao = DaoManager.createDao(conn, Team.class);
        Team tmp = new Team();
        for (Team t : teamDao) {
            if (t.getTEAMNAME() != null) {
                if (t.getTEAMNAME().equals("Rot-Weiss Essen")) {
                    tmp = t;
                }
            }

        }
        teamDao.delete(tmp);

        //source, next three lines: https://www.baeldung.com/java-random-string
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        User weakUser = new User(generatedString + "x@o.me", generatedString, "xxxxxx", "online", true);
        weakUser.setMATCHREQUESTS(null);
        User strongUser = new User(generatedString + "y@o.me", generatedString + "2", "yyyyyy", "online", true);
        strongUser.setMATCHREQUESTS(null);
        ArrayList<Integer> weakTeamList = new ArrayList<>();
        weakTeamList.addAll(getTenTimesWorstPlayerInDatabase());
        ArrayList<Integer> strongTeamList = new ArrayList<>();
        strongTeamList.addAll(getTenTimesBestPlayerInDatabase());
        Team weakTeam = new Team("Rot-Weiss Essen", weakUser, weakTeamList);
        Team strongTeam = new Team(generatedString + "s team", strongUser, strongTeamList);
        weakTeam.setSETUP(putSetupAsStringListOfNames(weakTeam));
        weakTeam.setRESERVE(putReserveAsStringListOfNames(weakTeam));
        strongTeam.setSETUP(putSetupAsStringListOfNames(strongTeam));
        strongTeam.setRESERVE(putReserveAsStringListOfNames(strongTeam));
        CalculateMatchHandler matchHandlerObject = new CalculateMatchHandler();
        userDao.create(weakUser);
        userDao.create(strongUser);
        teamDao.create(weakTeam);
        teamDao.create(strongTeam);
        weakUser.setREQUESTS(strongUser.getUsername());
        Assertions.assertTrue(weakTeamWins(matchHandlerObject.startMatch(weakUser.getUsername(), strongUser.getUsername())));
        userDao.delete(weakUser);
        userDao.delete(strongUser);
        teamDao.delete(weakTeam);
        teamDao.delete(strongTeam);
        teamDao.create(tmp);
    }

    //calls: Integer[] teamStrenghAlgorithm(Integer[] goals, Team team1, Team team2)
    @Test
    void averageWeightedTeamStrengthTest() throws SQLException {
        Team goodTeam = createTeam("good");
        Team badTeam = createTeam("bad");
        Integer[] tmpGoalsArray = new Integer[]{0, 0};
        CalculateMatchHandler matchHandlerObject = new CalculateMatchHandler();
        //System.out.println("goals good team: " + matchHandlerObject.teamStrenghAlgorithm(tmpGoalsArray, goodTeam, badTeam)[0] + " goals bad team: " + matchHandlerObject.teamStrenghAlgorithm(tmpGoalsArray, goodTeam, badTeam)[1]);
        Assertions.assertTrue(firstTeamIsStronger(matchHandlerObject.teamStrenghAlgorithm(tmpGoalsArray, goodTeam, badTeam)));
    }

    //calls: Integer[] midfieldplayer(Integer[] goals, Team team1, Team team2)
    @Test
    void averageWeightedMidfieldStrengthTest() throws SQLException {
        Team goodTeam = createTeam("good");
        Team badTeam = createTeam("bad");
        Integer[] tmpGoalsArray = new Integer[]{0, 0};
        CalculateMatchHandler matchHandlerObject = new CalculateMatchHandler();
        //System.out.println("goals good team: " + matchHandlerObject.teamStrenghAlgorithm(tmpGoalsArray, goodTeam, badTeam)[0] + " goals bad team: " + matchHandlerObject.teamStrenghAlgorithm(tmpGoalsArray, goodTeam, badTeam)[1]);
        Assertions.assertTrue(firstTeamIsStronger(matchHandlerObject.midfieldplayer(tmpGoalsArray, goodTeam, badTeam)));
    }


    //calls: Integer[] tenPlayers (Integer[] goals, Team team1, Team team2)
    @Test
    void tenRandomPairsTest() throws SQLException {
        Team goodTeam = createTeam("good");
        Team badTeam = createTeam("bad");
        Integer[] tmpGoalsArray = new Integer[]{0, 0};
        CalculateMatchHandler matchHandlerObject = new CalculateMatchHandler();
        //System.out.println("goals good team: " + matchHandlerObject.teamStrenghAlgorithm(tmpGoalsArray, goodTeam, badTeam)[0] + " goals bad team: " + matchHandlerObject.teamStrenghAlgorithm(tmpGoalsArray, goodTeam, badTeam)[1]);
        Assertions.assertTrue(firstTeamIsStronger(matchHandlerObject.tenPlayers(tmpGoalsArray, goodTeam, badTeam)));
    }


    //-------------------------------------helper methods--------------------------------------------


    private boolean firstTeamIsStronger(Integer[] teamStrenghAlgorithm) {
        return teamStrenghAlgorithm[0] > teamStrenghAlgorithm[1];
    }

    private Team createTeam(String lvl) throws SQLException {
        //source, next three lines: https://www.baeldung.com/java-random-string
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        User userObject = new User(generatedString + "@object.me", generatedString, generatedString, "online", true);
        ArrayList<Integer> teamList = new ArrayList<>();
        teamList.addAll(getPlayersIDList(lvl));
        Team team = new Team(generatedString, userObject, teamList);
        team.setSETUP(putSetupAsStringListOfNames(team));
        team.setRESERVE(putReserveAsStringListOfNames(team));
        return team;
    }

    private ArrayList<String> putSetupAsStringListOfNames(Team team) throws SQLException {
        ArrayList<String> returnListWithWorstMidfieldPlayer = new ArrayList<>();
        int x = 0;
        for (Integer i : team.getPlayersList()) {
            if (x == 11) break;
            returnListWithWorstMidfieldPlayer.add(getPlayerName(i));
            //System.out.println("Added : " + getPlayerName(i) + ", to setup");
            x++;
        }
        return returnListWithWorstMidfieldPlayer;
    }

    private String getPlayerName(Integer i) throws SQLException {
        String output = "";
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
        Dao<Player, String> playerDao = DaoManager.createDao(connectionSource, Player.class);
        for (Player p : playerDao) {
            if (p.getId() == i) {
                output = p.getName();
            }
        }
        return output;
    }

    private ArrayList<String> putReserveAsStringListOfNames(Team team) throws SQLException {
        ArrayList<String> returnList = new ArrayList<>();
        int x = 0;
        for (Integer i : team.getPlayersList()) {
            if (x == 9) break;
            returnList.add(getPlayerName(i));
            //System.out.println("Added : " + getPlayerName(i) + ", to reserve");
            x++;
        }
        return returnList;
    }

    private Collection<? extends Integer> getPlayersIDList(String lvl) throws SQLException {
        ArrayList<Integer> listToBeReturned = new ArrayList<>();
        listToBeReturned.addAll(getSpecificPlayers(lvl, "Angriff", 3));
        listToBeReturned.addAll(getSpecificPlayers(lvl, "Mittelfeld", 10));
        listToBeReturned.addAll(getSpecificPlayers(lvl, "Abwehr", 7));
        listToBeReturned.addAll(getSpecificPlayers(lvl, "Torwart", 2));
        return listToBeReturned;
    }

    private Collection<? extends Integer> getSpecificPlayers(String lvl, String position, int i) throws SQLException {
        ArrayList<Integer> listToBeReturned = new ArrayList<>();
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
        Dao<Player, String> playerDao = DaoManager.createDao(connectionSource, Player.class);
        if (lvl.equalsIgnoreCase("good")) {
            double maxValue = 0;
            Player tmpPlayer = new Player();
            for (Player p : playerDao) {
                if (p.getStrength() > maxValue && p.getPosition().equalsIgnoreCase(position)) {
                    tmpPlayer = p;
                    maxValue = p.getStrength();
                }
            }
            for (int j = 0; j < i; j++) {
                listToBeReturned.add(tmpPlayer.getId());
                //System.out.println("Added good player: " + tmpPlayer.getName() + ", Strength: " + tmpPlayer.getStrength());
            }
        } else {
            double minValue = 10000;
            Player tmpPlayer = new Player();
            for (Player p : playerDao) {
                if (p.getStrength() < minValue && p.getPosition().equalsIgnoreCase(position)) {
                    tmpPlayer = p;
                    minValue = p.getStrength();
                }
            }
            for (int j = 0; j < i; j++) {
                listToBeReturned.add(tmpPlayer.getId());
                //System.out.println("Added bad player : " + tmpPlayer.getName() + ", Strength: " + tmpPlayer.getStrength());
            }
        }
        return listToBeReturned;
    }

    private boolean weakTeamWins(String s) {
        return s.equalsIgnoreCase("User1");
    }

    private boolean notAllTheSame(ArrayList<Integer[]> listOfAllGoalCalculationResults) {
        Map map = new HashMap<>();
        Map map2 = new HashMap<>();
        for (Integer[] i : listOfAllGoalCalculationResults) {
            map.put(i[0], i[1]);
            map2.put(i[1], i[0]);
        }
        return ((listOfAllGoalCalculationResults.size() != map.size()) || (listOfAllGoalCalculationResults.size() != map2.size()));
    }

    private Collection<? extends Integer> getTenTimesBestPlayerInDatabase() throws SQLException {
        ArrayList<Integer> tenTimesBestPlayer = new ArrayList<>();
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
        Dao<Player, String> playerDao = DaoManager.createDao(connectionSource, Player.class);
        double maxValue = 0;
        int strongPlayerID = -1;

        for (Player player : playerDao) {
            if ((player.getStrength() > maxValue)) {
                maxValue = player.getStrength();
                strongPlayerID = player.getId();
            }
        }
        for (int i = 0; i < 11; i++) {
            tenTimesBestPlayer.add(strongPlayerID);
        }

        return tenTimesBestPlayer;
    }

    private Collection<? extends Integer> getTenTimesWorstPlayerInDatabase() throws SQLException {
        ArrayList<Integer> tenTimesWorstPlayer = new ArrayList<>();
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
        Dao<Player, String> playerDao = DaoManager.createDao(connectionSource, Player.class);
        double minValue = 1000;
        int weakPlayerID = -1;

        for (Player player : playerDao) {
            if ((player.getStrength() > minValue)) {
                minValue = player.getStrength();
                weakPlayerID = player.getId();
            }
        }
        for (int i = 0; i < 11; i++) {
            tenTimesWorstPlayer.add(weakPlayerID);
        }
        return tenTimesWorstPlayer;
    }
}