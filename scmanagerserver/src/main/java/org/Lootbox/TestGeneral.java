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

public class TestGeneral {

    ArrayList<Player> earnedPlayers = new ArrayList<>();

    public ArrayList<Player> generater(User user, int anzahl) throws SQLException {

        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");

        Dao<Player, String> lootBoxPlayersDao = DaoManager.createDao(connectionSource, Player.class);
        Dao<Team, String> teamsDao            = DaoManager.createDao(connectionSource, Team.class);


        QueryBuilder<Team, String> TeamQueryBuilder = teamsDao.queryBuilder();
        TeamQueryBuilder.where().eq("USERID", user.getID());
        PreparedQuery<Team> teamQuery = TeamQueryBuilder.prepare();

        Team tmpTeam = teamsDao.queryForFirst(teamQuery);

        int i = 1;

        double searchStrength = 0;

        double gesamteStrength = 0;
        double anzahlDerSpieler = lootBoxPlayersDao.queryForAll().size();

        for (Player player : lootBoxPlayersDao) {
            gesamteStrength += player.getStrength();
        }

        double average = Math.round(gesamteStrength / anzahlDerSpieler);

        outerloop:
        while (i <= anzahl) {
            System.out.println("Neuer Schleifendurchlauf!");

            if(i%2 == 1)
            {
                System.out.println("stärke wurde zufällig ausgewählt");
                searchStrength = ThreadLocalRandom.current().nextInt(1, 97);
            }
            else if(i%2 == 0)
            {
                searchStrength = (average * 2) - searchStrength;
            }

            QueryBuilder<Player, String> queryBuilder = lootBoxPlayersDao.queryBuilder();
            queryBuilder.where().eq("strength", searchStrength);
            PreparedQuery<Player> preparedQuery = queryBuilder.prepare();

            List<Player> tmpPlayersList = lootBoxPlayersDao.query(preparedQuery);

            int counter = 0;

            if(tmpTeam.getPlayersList()==null)
            {
                ArrayList<Integer> tmpList = new ArrayList<>();
                tmpList.add(tmpPlayersList.get(counter).getId());
                tmpTeam.setPlayersList(tmpList);
                teamsDao.update(tmpTeam);
                i++;
                earnedPlayers.add(tmpPlayersList.get(counter));
                continue;
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

