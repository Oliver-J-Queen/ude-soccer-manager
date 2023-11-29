package org.scraper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Yelle Lieder
 */

public class Normalizer {

    // to run manually, just switch comments from line 22 to line 23
    //public static void main (String[] args) throws SQLException, IOException {
         public static void run() throws SQLException, IOException {
        String DB_URL = "jdbc:h2:~/scmanagerdb";
        ConnectionSource cs;
        cs = new JdbcConnectionSource(DB_URL);
        Dao<Player, String> playersDao = DaoManager.createDao(cs, Player.class);
        TableUtils.createTableIfNotExists(cs, Player.class);
        File strengthLogger = new File("StrengthLogger.txt");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
        BufferedWriter writer = new BufferedWriter(new FileWriter(strengthLogger, true));
        String currDate = date.format(new Date());
        String currTime = time.format(new Date());


        if (fullyScraped().equals("ok")) {
            deleteSomePlayers();
            int count = 1;
            double max = 0;
            double min = 200000000;
            for (Player player : playersDao) {
                if (player.strength > max) max = player.strength;
                if (player.strength < min) min = player.strength;
            }
            double diff = (max - min) / 100;
            writer.write(currDate + ", " + currTime);
            System.out.println(currDate + ", " + currTime);
            writer.newLine();
            for (Player player : playersDao) {
                double tmp = player.strength;
                player.strength = round(player.strength / diff);
                playersDao.update(player);
                System.out.println(count + ". " + player.getName() + "s strength normalized from " + tmp + " to " + player.strength);
                writer.write(count + ". " + player.getName() + "s strength normalized from " + tmp + " to " + player.strength);
                writer.newLine();
                count++;
            }
            System.out.println("Strength of each player normalized based on max (" + max + ") and min (" + min + ") strength");
        } else if (fullyScraped().equals("not scraped")) {
            System.out.println("please complete scraping before normalizing!");
        } else if (fullyScraped().equals("normalized")) {
            System.out.println("players already got normalized");
        }
        cs.close();
    }

    private static void deleteSomePlayers() throws SQLException, IOException {
        File strengthLogger = new File("StrengthLogger.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(strengthLogger, true));
        int playersCount = 0;
        double max = 0;
        String DB_URL = "jdbc:h2:~/scmanagerdb";
        ConnectionSource cs;
        cs = new JdbcConnectionSource(DB_URL);
        Dao<Player, String> playersDao = DaoManager.createDao(cs, Player.class);

        for(int i = 0; i<50;i++) {
            for (Player player : playersDao) {
                if (player.strength > max) max = player.strength;
            }
            for (Player player : playersDao) {
                if (player.strength == max) {
                    writer.write(player.getName() + " deleted since he was too \"strong\"");
                    System.out.println(player.getName() + " deleted since he was too \"strong\"");
                    playersDao.delete(player);
                    playersCount++;
                    max = 0;
                    break;
                }
            }
        }
        cs.close();
        writer.write("Top "+playersCount+" players were delete due to better normalization.");
        System.out.println("Top "+playersCount+" players were delete due to better normalization.");
    }

    private static String fullyScraped() throws SQLException, IOException {
        String val = "normalized";
        String DB_URL = "jdbc:h2:~/scmanagerdb";
        ConnectionSource cs;
        cs = new JdbcConnectionSource(DB_URL);
        Dao<Player, String> playersDao = DaoManager.createDao(cs, Player.class);
        TableUtils.createTableIfNotExists(cs, Player.class);
        for (Player player : playersDao) {
            if (player.strength == 111.11) {
                //   return "ok";
               return "not scraped";
            }
        }
        for (Player player : playersDao) {
            if (player.strength > 100) {
                return "ok";
            }
        }
        cs.close();
        return val;
    }


    //from https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    private static double round(double v) {
        if (v < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}


