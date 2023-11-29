package org.scraper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.dataclasses.Team;
import org.dataclasses.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerMock {

    public static void main(String [] args) throws SQLException {
        String DB_URL = "jdbc:h2:~/scmanagerdb";
        ConnectionSource cs;
        cs = new JdbcConnectionSource(DB_URL);
        Dao<User, String> userDao = DaoManager.createDao(cs, User.class);
        TableUtils.createTableIfNotExists(cs, User.class);
        Dao<Team, String> teamDao = DaoManager.createDao(cs, Team.class);
        TableUtils.createTableIfNotExists(cs, Team.class);

        User user = new User("mock@mock.de", "mock", "123456", "Online", false);
        ArrayList<Integer> player = new ArrayList<>();
        player.add(1);
        player.add(2);
        player.add(3);
        player.add(4);
        Team team = new Team("MockTeam", user, player);

        teamDao.create(team);
        userDao.create(user);
    }
}
