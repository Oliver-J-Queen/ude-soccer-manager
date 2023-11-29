package org.Lootbox;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.Player;
import org.dataclasses.Team;
import org.dataclasses.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


/**
 * @author Yelle Lieder
 */
class SpecialLootboxTest {

    @Test
    public void combinedLootboxTest() throws Exception {
        int numberOfTests=1;
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
        Dao<Team, String> teamsDao = DaoManager.createDao(connectionSource, Team.class);
        Dao<User, String> userDao = DaoManager.createDao(connectionSource, User.class);

        for (int i = 1; i < numberOfTests+1; i++) {
            int b = ThreadLocalRandom.current().nextInt(0, 777777);
            User userObject = new User(b+i + "user@object.me", b+i + "userObjectName", b+i + "userObjectPassword", "online", true);
            userDao.create(userObject);

            ArrayList<Integer> teamList = new ArrayList<>();
            Team team = new Team(b+i+"userObjectNames Teams", userObject, teamList);
            teamsDao.create(team);

            System.out.print(i+". ");
            SpecialLootbox specialLootboxObject1 = new SpecialLootbox();
            Assertions.assertTrue(testHelper(userObject, specialLootboxObject1));

            userDao.delete(userObject);
            teamsDao.delete(team);
        }
        Dao<Player, String> playerDao = DaoManager.createDao(connectionSource, Player.class);
        double y=0;
        for (Player p:playerDao){
            y+=p.getStrength();
        }
        System.out.println("\naverage global strength: "+y/playerDao.queryForAll().size());
    }

    private boolean testHelper(User userObject, SpecialLootbox specialLootboxObject) throws Exception {
        return (specialLootboxObject.ersteller(userObject).size() == 22 &&
                noDoubledPlayer(specialLootboxObject.ersteller(userObject)) &&
                numberOfPlayerAtPosition(specialLootboxObject.ersteller(userObject), "Torwart", 2) &&
                numberOfPlayerAtPosition(specialLootboxObject.ersteller(userObject), "Abwehr", 7) &&
                numberOfPlayerAtPosition(specialLootboxObject.ersteller(userObject), "Mittelfeld", 10) &&
                numberOfPlayerAtPosition(specialLootboxObject.ersteller(userObject), "Angriff", 3));
    }

    private boolean numberOfPlayerAtPosition(ArrayList<Player> teamList, String position, int i) {
        int n = 0;
        for (Player p : teamList) {
            if (p.getPosition().equalsIgnoreCase(position)) {
                n++;
            }
        }
        return n == i;
    }

    private boolean noDoubledPlayer(ArrayList<Player> teamList) {
        Set<Player> set = new HashSet<>(teamList);
        double x= set.stream().mapToDouble(Player::getStrength).sum();
        System.out.print("average team strength: "+ x/22+"\n");
        return set.size()==teamList.size();
    }


}
