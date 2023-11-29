package org.Lootbox;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import org.dataclasses.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Author: Parssa Mahdavi
 **/

public class deleteDoubleDataRecords {

    public static void main (String[] args) throws Exception {

        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");

        Dao<Player, String> lootBoxPlayersDao = DaoManager.createDao(connectionSource, Player.class);

        ArrayList<String> spielerNamen = new ArrayList<>();

        int i = 0;

        List<Player> listeAlle = lootBoxPlayersDao.queryForAll();

        outerlooooooop:
        while(i<lootBoxPlayersDao.queryForAll().size()){

            for(String namen : spielerNamen) {
                if(namen.equals(listeAlle.get(i).getName())){
                    DeleteBuilder<Player, String> deleteBuilder = lootBoxPlayersDao.deleteBuilder();
                    deleteBuilder.where().eq("id", i+1);
                    deleteBuilder.delete();
                    System.out.println("hier wurde eine Zeile gelöscht");
                    i++;
                    continue outerlooooooop;
                }
            }
            System.out.println("Hier lief alles gut");
            spielerNamen.add(listeAlle.get(i).getName());
            i++;
        }
    }
}
