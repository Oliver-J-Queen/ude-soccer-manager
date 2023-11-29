package org.runTournament;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.User;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Author: Parssa Mahdavi
 */

public class test {
    public static void main(String[] args) throws SQLException {

        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
        Dao<User, String> userDao = DaoManager.createDao(connectionSource, User.class);

        runTournament testing = new runTournament();
        ArrayList<User> tmpUser = new ArrayList<>();
        tmpUser.add(userDao.queryForId("4"));
        tmpUser.add(userDao.queryForId("1"));
        tmpUser.add(userDao.queryForId("2"));
        tmpUser.add(userDao.queryForId("3"));


        int[] tempod = testing.runner(tmpUser, "League", 200);

        /*
        for(int[] inta: tmp) {
            for(int bla : inta) {
                System.out.print(bla + " ");
            }
            System.out.println();
        }
         */
        for(int temop : tempod) System.out.println(temop);

    }
}
