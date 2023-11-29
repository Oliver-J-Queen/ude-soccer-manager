package org.Lootbox;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.Player;
import org.dataclasses.Team;
import org.dataclasses.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Author: Parssa Mahdavi
 **/

public class GeneralLootbox {

    protected int anzahl;
    protected int price;

    /*public GeneralLootbox(int anzahl, int price)
    {
        this.anzahl = anzahl;
        this.price = price;
    }
*/
    public int getPrice()

    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public ArrayList<Player> generater(User user, int anzahl) throws SQLException {

        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");

        Dao<Player, String> lootBoxPlayersDao = DaoManager.createDao(connectionSource, Player.class);
        Dao<Team, String> teamsDao            = DaoManager.createDao(connectionSource, Team.class);


        QueryBuilder<Team, String> TeamQueryBuilder = teamsDao.queryBuilder();
        TeamQueryBuilder.where().eq("USERID", user.getID());
        PreparedQuery<Team> teamQuery = TeamQueryBuilder.prepare();

        Team tmpTeam = teamsDao.queryForFirst(teamQuery);

        double gesamteStrength = 0;
        double anzahlDerSpieler = lootBoxPlayersDao.queryForAll().size();

        for (Player player : lootBoxPlayersDao) gesamteStrength += player.getStrength();


        double average = Math.round(gesamteStrength / anzahlDerSpieler);
        average = Math.round(100.0*average)/100.0;
        double doubleAverage = average+average;

        System.out.println(gesamteStrength + " " + anzahlDerSpieler + " " + average);

        double searchStrength = 0;

        ArrayList<Player> earnedPlayers = new ArrayList<>();

        int i = 1;

        outerloop:
        while (i <= anzahl) {
            System.out.println("Neuer Schleifendurchlauf!");

            if(i%2 == 1)
            {
                System.out.println("stärke wurde zufällig ausgewählt");
                int searchID = ThreadLocalRandom.current().nextInt(1, lootBoxPlayersDao.queryForAll().size());
                String searchingID = Integer.toString(searchID);
                searchStrength = lootBoxPlayersDao.queryForId(searchingID).getStrength();
                System.out.println("zufällig ausgewählte stärke beträgt: "+ searchStrength);
            }
            else if(i%2 == 0)
            {
                System.out.println("stärke wurde berechnet");
                searchStrength = Math.abs(doubleAverage - searchStrength);
                searchStrength = Math.round(100.0*searchStrength)/100.0;
                if(searchStrength==0.0) searchStrength=100.0;
                System.out.println("Berechnete stärke beträgt: " + searchStrength);
            }

            QueryBuilder<Player, String> queryBuilder = lootBoxPlayersDao.queryBuilder();
            queryBuilder.where().eq("strength", searchStrength);
            PreparedQuery<Player> preparedQuery = queryBuilder.prepare();

            List<Player> tmpPlayersList = lootBoxPlayersDao.query(preparedQuery);

            int counter = 0;

            if(tmpPlayersList.size()==0){
                System.out.println("Listengröße = 0");
                searchStrength =doubleAverage-(searchStrength-0.01);
                searchStrength = Math.round(100.0*searchStrength)/100.0;
                System.out.println(searchStrength);
                continue;
            }

            if(tmpTeam.getPlayersList()==null)
            {
                ArrayList<Integer> tmpList = new ArrayList<>();
                tmpList.add(tmpPlayersList.get(counter).getId());
                tmpTeam.setPlayersList(tmpList);
                teamsDao.update(tmpTeam);
                i++;
                earnedPlayers.add(tmpPlayersList.get(counter));
            }
            else
            {
                forloop:
                for (Player momentanerSpieler : tmpPlayersList) {
                    if (momentanerSpieler.getPosition().equals("Unknown"))
                    {
                        System.out.println("Dieser Spieler hatte eine unbekannte Position und wird daher übersprungen!");
                        counter++;
                    }
                    else
                    {
                        for (Player gezogeneSpieler : earnedPlayers) {
                            if (momentanerSpieler.getName().equals(gezogeneSpieler.getName())) {
                                System.out.println("Dieser Spieler wurde schon gezogen!");
                                counter++;
                                continue forloop;
                            }
                        }
                    }
                }

                if(tmpPlayersList.size()==1 && counter==1) {
                    counter--;
                }

                earnedPlayers.add(tmpPlayersList.get(counter));
                tmpTeam.getPlayersList().add(tmpPlayersList.get(counter).getId());
                teamsDao.update(tmpTeam);
                i++;
            }
        }
        teamsDao.update(tmpTeam);
        return earnedPlayers;
    }
}

