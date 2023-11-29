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
import org.serverconnection.UserEditHandler;

public class LootTest {

    public static void main(String[] args) throws  Exception{

        TestGeneral tmpTest = new TestGeneral();

        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");

        Dao<Player, String> lootBoxPlayersDao = DaoManager.createDao(connectionSource, Player.class);
        Dao<Team, String> teamsDao            = DaoManager.createDao(connectionSource, Team.class);
        Dao<User, String> usersDao            = DaoManager.createDao(connectionSource, User.class);

        User tmpUser = usersDao.queryForId("1");
/*
        QueryBuilder<Team, String> TeamQueryBuilder = teamsDao.queryBuilder();
        TeamQueryBuilder.where().eq("USERID", tmpUser.getID());
        PreparedQuery<Team> teamQuery = TeamQueryBuilder.prepare();

        Team tmpTeam = teamsDao.queryForFirst(teamQuery);

 */

        tmpTest.generater(tmpUser, 8);

        Dao<User, String> usr = DaoManager.createDao(connectionSource, User.class);



    }

}

