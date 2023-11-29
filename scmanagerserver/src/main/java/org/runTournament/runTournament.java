package org.runTournament;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.User;
import org.serverconnection.CalculateMatchHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Author: Parssa Mahdavi
 */

public class runTournament {

    public int[] runner(ArrayList<User> User, String mode, int fee) throws SQLException {

        CalculateMatchHandler cMH = new CalculateMatchHandler();

        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
        Dao<User, String> userDao = DaoManager.createDao(connectionSource, User.class);

        int[] platzierung = new int[3];

        //In diesem Array werden folgende Daten gespeichert: für jeden Nutzer (ein Nutzer-> ein Array) wird die ID([0][0]), Punkte([0][1]), Tore([0][2]) und Gegentore([0][3]) gespeichert

        int[][] statistic = new int[User.size()][4];

        int currUser = 0;

        //jedes Array in statistic[][] kriegt beim Index [0] die Id des Nutzers zugeteilt
        for(User nutzer : User)
        {
            statistic[currUser][0] = nutzer.getID();
            currUser++;
        }

        int Pott = 0;
        //Pott wird mit den Gebühren gefüllt und verdoppelt
        Pott = (User.size()*fee)*2;

        System.out.println("DER POTT BETRÄGT: "+Pott);

        double splittedPott = Pott/6;

        double sepForFirst  = 3*splittedPott;
        int sepFirst = (int)sepForFirst;

        System.out.println("DER ERSTE PLATZ BEKOMMT SO VIELE SEPS: "+sepFirst);

        double sepForSecond = 2*splittedPott;
        int sepSecond = (int)sepForSecond;

        System.out.println("DER ZWEITE PLATZ BEKOMMT SO VIELE SEPS: "+sepSecond);

        double sepForThird  =   splittedPott;

        int sepThird = (int)sepForThird;

        System.out.println("DER DRITTE PLATZ BEKOMMT SO VIELE SEPS: "+sepThird);
        int i = 0;
        int j = 0;

        //K.O. Modus
        if(mode.equals("Knockout")) {

            System.out.println("Der K.O. Modus wurde ausgewählt!");
            ArrayList<User> remainingUsers = new ArrayList<>(User);

            System.out.println("Das sind die Turnierteilnehmer: ");
            for(User usa : remainingUsers)System.out.println(usa.getUsername());
            System.out.println();

            int idForFirst  = 0;
            int idForSecond = 0;
            int idForThird  = 0;

            forloop:
            for(int a=0; a<User.size()-1;a++)
            {

                if(remainingUsers.size()==2)
                {
                    //erstes Userobjekt erstellen
                    User firstUser = remainingUsers.get(0);

                    //zweites Userobjekt erstellen
                    User secondUser = remainingUsers.get(1);

                    Integer[][] tmpStatistic = cMH.startTournamentMatch(firstUser.getUsername(), secondUser.getUsername());

                    // bei Unentschieden
                    if (tmpStatistic[0][0] == 0 && tmpStatistic[1][0] == 0) {
                        while (tmpStatistic[0][0] == 0 && tmpStatistic[1][0] == 0) {
                            tmpStatistic = cMH.startTournamentMatch(firstUser.getUsername(), secondUser.getUsername());
                        }
                    }
                    //firstUser hat gewonnen
                    if (tmpStatistic[0][0] == firstUser.getID()) {
                        //User der verloren hat wird aus der liste der übrigen spieler entfernt (in diesem Fall der zweite Spieler, da ja der erste gewonnen hat)
                        remainingUsers.remove(secondUser);

                        if (a == User.size()-3) {
                            idForThird = secondUser.getID();
                        } else if (a == User.size()-2) {
                            idForSecond = secondUser.getID();
                        }
                    }
                    //seconduser hat gewonnen
                    if (tmpStatistic[0][0] == secondUser.getID()) {

                        remainingUsers.remove(firstUser);

                        if (a == User.size() - 3) {
                            idForThird = firstUser.getID();
                        } else if (a == User.size() - 2) {
                            idForSecond = firstUser.getID();
                        }
                    }

                }

                else
                {
                    int firstUserIndex = ThreadLocalRandom.current().nextInt(1, remainingUsers.size());
                    int secondUserIndex = ThreadLocalRandom.current().nextInt(1, remainingUsers.size());

                    if (firstUserIndex == secondUserIndex)
                    {
                        a--;
                        continue;
                    } else
                    {
                        //erstes Userobjekt erstellen
                        User firstUser = remainingUsers.get(firstUserIndex);

                        //zweites Userobjekt erstellen
                        User secondUser = remainingUsers.get(secondUserIndex);

                        Integer[][] tmpStatistic = cMH.startTournamentMatch(firstUser.getUsername(), secondUser.getUsername());

                        // bei Unentschieden
                        if (tmpStatistic[0][0] == 0 && tmpStatistic[1][0] == 0)
                        {
                            while (tmpStatistic[0][0] == 0 && tmpStatistic[1][0] == 0)
                            {
                                tmpStatistic = cMH.startTournamentMatch(firstUser.getUsername(), secondUser.getUsername());
                            }
                        }
                        //firstUser hat gewonnen
                        if (tmpStatistic[0][0] == firstUser.getID())
                        {
                            if (a == User.size() - 3) {
                                idForThird = secondUser.getID();
                            } else if (a == User.size() - 2) {
                                idForSecond = secondUser.getID();
                            }
                            //User der verloren hat wird aus der liste der übrigen spieler entfernt (in diesem Fall der zweite Spieler, da ja der erste gewonnen hat)
                            remainingUsers.remove(secondUser);
                        }
                        //seconduser hat gewonnen
                        if (tmpStatistic[0][0] == secondUser.getID())
                        {
                            if (a == User.size() - 3) {
                                idForThird = firstUser.getID();
                            } else if (a == User.size() - 2) {
                                idForSecond = firstUser.getID();
                            }

                            remainingUsers.remove(firstUser);
                        }
                    }
                }
            }//Ende der for-Schleife

            idForFirst = remainingUsers.get(0).getID();

            platzierung[0] = idForFirst;
            platzierung[1] = idForSecond;
            platzierung[2] = idForThird;

            User winner = userDao.queryForId(String.valueOf(idForFirst));
            System.out.println("DER NAME VOM GEWINNER LAUTET: " + winner.getUsername());
            winner.setPoints(winner.getPoints()+sepFirst);
            userDao.update(winner);

            User second = userDao.queryForId(String.valueOf(idForSecond));
            System.out.println("DER NAME VOM ZWEITEN LAUTET: " + second.getUsername());
            second.setPoints(second.getPoints()+sepSecond);
            userDao.update(second);

            User third = userDao.queryForId(String.valueOf(idForThird));
            System.out.println("DER NAME VOM DRITTEN LAUTET: " + third.getUsername());
            third.setPoints(third.getPoints()+sepThird);
            userDao.update(third);



/*
            QueryBuilder<User, String> userQueryBuilder = userDao.queryBuilder();
            userQueryBuilder.where().eq("ID", idForFirst);
            PreparedQuery<User> teamQuery = userQueryBuilder.prepare();
            User winner = userDao.queryForFirst(teamQuery);

            winner.setPoints((winner.getPoints()+sepFirst));
            userDao.update(winner);

            QueryBuilder<User, String> userQueryBuilderr = userDao.queryBuilder();
            userQueryBuilderr.where().eq("ID", idForSecond);
            PreparedQuery<User> teamQueryy = userQueryBuilderr.prepare();
            User secondPlace = userDao.queryForFirst(teamQueryy);
            userDao.update(secondPlace);

            secondPlace.setPoints((secondPlace.getPoints()+sepSecond));

            QueryBuilder<User, String> userQueryBuilderrr = userDao.queryBuilder();
            userQueryBuilderrr.where().eq("ID", idForThird);
            PreparedQuery<User> teamQueryyy = userQueryBuilderrr.prepare();
            User thirdPlace = userDao.queryForFirst(teamQueryyy);
            userDao.update(thirdPlace);

            thirdPlace.setPoints((thirdPlace.getPoints()+sepThird));

 */

        }

        //Liga Modus
        else if(mode.equals("League")) {

            int anzahlNutzer = User.size();

            boolean[][] games = new boolean[anzahlNutzer][anzahlNutzer];

            //immer wenn ein Nutzer gegen sich selbst spielen würde, wird der Wert in diesem Array dann auf true gesetzt
            for(i=0; i<anzahlNutzer;i++){
                for(j=0; j<anzahlNutzer;j++){
                    if(i==j) {
                        games[i][j]=true;
                    }
                }
            }

            firstLoop:
            for( i=0; i<games.length;i++)
            {
                secondLoop:
                for (j = 0; j < games[0].length; j++)
                {

                    if(games[i][j]) {
                        continue secondLoop;
                    }

                    else
                    {
                        games[i][j]=true;
                        games[j][i]=true;

                        //erstes Userobjekt erstellen
                        User firstUser = User.get(i);

                        //zweites Userobjekt erstellen
                        User secondUser = User.get(j);

                        //hier findet das Spiel statt und anschließend wird dann alles in einem Array gespeichert (NutzerID, Punkte, Tore, Gegentore)
                        Integer[][] tmpStatistics = cMH.startTournamentMatch(firstUser.getUsername(), secondUser.getUsername());

                        //Bei dem Fall, dass das Spiel unentschieden ist
                        if(tmpStatistics[0][0] == 0 && tmpStatistics[1][0] == 0)
                        {
                            int indexFirst = 0;
                            int indexSecond = 0;

                            //Hier wird geguckt bei welchem Index sich der erste Spieler befindet
                            for(int[] spielerStat : statistic)
                            {
                                if(spielerStat[0]==firstUser.getID())
                                {
                                    break;
                                }
                                else
                                {
                                    indexFirst++;
                                }
                            }
                            //Hier wird geguckt bei welchem Index sich der Nutzer befindet, der verloren hat
                            for(int[] spielerStat : statistic)
                            {
                                if(spielerStat[0]==secondUser.getID())
                                {
                                    break;
                                }
                                else
                                {
                                    indexSecond++;
                                }
                            }
                            //Punkte
                            statistic[indexFirst][1]  += 1;
                            statistic[indexSecond][1] += 1;
                            //Tore
                            statistic[indexFirst][2]  += tmpStatistics[0][1];
                            statistic[indexSecond][2] += tmpStatistics[0][2];
                            //Gegentore
                            statistic[indexFirst][3]  += tmpStatistics[0][2];
                            statistic[indexSecond][3] += tmpStatistics[0][1];

                        }

                        //Nutzer 1 hat gewonnen
                        else if(tmpStatistics[0][0] == firstUser.getID())
                        {

                            int indexWinner = 0;

                            int indexLooser = 0;
                            //Hier wird geguckt bei welchem Index sich der Nutzer befindet, der gewonnen hat
                            for(int[] spielerStat : statistic) {
                                if(spielerStat[0]==firstUser.getID()) {
                                    break;
                                }
                                else {
                                    indexWinner++;
                                }
                            }
                            //Hier wird geguckt bei welchem Index sich der Nutzer befindet, der verloren hat
                            for(int[] spielerStat : statistic) {
                                if(spielerStat[0]==secondUser.getID()) {
                                    break;
                                }
                                else {
                                    indexLooser++;
                                }
                            }

                            //Statistiken für den winner
                            //Punkte
                            statistic[indexWinner][1] += 3;
                            //Tore
                            statistic[indexWinner][2] += tmpStatistics[0][1];
                            //Gegentore
                            statistic[indexWinner][3] += tmpStatistics[0][2];

                            //Statistiken für den looser
                            //Tore
                            statistic[indexLooser][2] += tmpStatistics[1][1];
                            //Gegentore
                            statistic[indexLooser][3] += tmpStatistics[1][2];
                        }

                        //Nutzer zwei hat gewonnen
                        else if(tmpStatistics[0][0] == secondUser.getID())
                        {
                            int indexWinner = 0;
                            int indexLooser = 0;

                            //Hier wird geguckt bei welchem Index sich der Nutzer befindet, der gewonnen hat
                            for(int[] spielerStat : statistic) {
                                if(spielerStat[0]==secondUser.getID()) {
                                    break;
                                }
                                else {
                                    indexWinner++;
                                }
                            }

                            //Hier wird geguckt bei welchem Index sich der Nutzer befindet, der verloren hat
                            for(int[] spielerStat : statistic) {
                                if(spielerStat[0]==firstUser.getID()) {
                                    break;
                                }
                                else {
                                    indexLooser++;
                                }
                            }

                            //Statistiken für den winner
                            //Punkte
                            statistic[indexWinner][1] += 3;
                            //Tore
                            statistic[indexWinner][2] += tmpStatistics[0][1];
                            //Gegentore
                            statistic[indexWinner][3] += tmpStatistics[0][2];

                            //Statistiken für den looser
                            //Tore
                            statistic[indexLooser][2] += tmpStatistics[1][1];
                            //Gegentore
                            statistic[indexLooser][3] += tmpStatistics[1][2];
                        }
                    }
                }
            }
            //Hier wird das Array nach den Punkten sortiert
            //Quelle: https://stackoverflow.com/questions/15452429/java-arrays-sort-2d-array
            java.util.Arrays.sort(statistic, java.util.Comparator.comparingInt(a -> a[1]));

            //Hier wird geguckt, ob Nutzer die gleiche Punktzahl haben und falls, ja wird nach Toren sortiert
            boolean[][] testForSamePoints = new boolean[anzahlNutzer][anzahlNutzer];

            for(i=0; i<anzahlNutzer;i++){
                for(j=0; j<anzahlNutzer;j++){
                    if(i==j) {
                        testForSamePoints[i][j]=true;
                    }
                }
            }

            firstLoop:
            for(int s=0; s<anzahlNutzer;s++) {
                secondLoop:
                for(int t=0; t<anzahlNutzer; t++) {

                    if(testForSamePoints[s][t]) continue secondLoop;

                    else {

                        testForSamePoints[s][t] = true;
                        testForSamePoints[t][s] = true;

                        //Wenn die Punktzahl gleich ist
                        if(statistic[s][1]==statistic[t][1]) {

                            //Wenn der Index s mehr Tore hat als t
                            if(statistic[s][2]>statistic[t][2]) {
                                //Daten von dem Nutzer, zwischenspeichern, der mehr Tore hat
                                int tmpID        = statistic[s][0];
                                int tmpPunkte    = statistic[s][1];
                                int tmpTore      = statistic[s][2];
                                int tmpGegenTore = statistic[s][3];

                                //hier werden die Zeilen ausgetauscht
                                statistic[s][0] = statistic[t][0];
                                statistic[s][1] = statistic[t][1];
                                statistic[s][2] = statistic[t][2];
                                statistic[s][3] = statistic[t][3];

                                statistic[t][0] = tmpID;
                                statistic[t][1] = tmpPunkte;
                                statistic[t][2] = tmpTore;
                                statistic[t][3] = tmpGegenTore;
                            }
                            //Wenn der Index t mehr Tore hat als s, passiert nichts
                            //Wenn auch die Tore gleich sind, dann passiert auch nichts
                        }
                    }
                }
            }
            platzierung[0] = statistic[User.size()-1][0];
            platzierung[1] = statistic[User.size()-2][0];
            platzierung[2] = statistic[User.size()-3][0];

            //hier kriegen alle ihre seps
            User winner = userDao.queryForId(String.valueOf(platzierung[0]));
            System.out.println("DER NAME VOM GEWINNER LAUTET: " + winner.getUsername());
            winner.setPoints(winner.getPoints()+sepFirst);
            userDao.update(winner);

            User second = userDao.queryForId(String.valueOf(platzierung[1]));
            System.out.println("DER NAME VOM ZWEITEN LAUTET: " + second.getUsername());
            second.setPoints(second.getPoints()+sepSecond);
            userDao.update(second);

            User third = userDao.queryForId(String.valueOf(platzierung[2]));
            System.out.println("DER NAME VOM DRITTEN LAUTET: " + third.getUsername());
            third.setPoints(third.getPoints()+sepThird);
            userDao.update(third);
        }
        return platzierung;
    }
}
