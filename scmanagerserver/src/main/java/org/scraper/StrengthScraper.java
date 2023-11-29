package org.scraper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Yelle Lieder
 * basically just watched those videos:
 * https://www.youtube.com/watch?v=ZtXXvtI8jcs
 * https://www.youtube.com/watch?v=mCibdSd09x
 * and did what they did
 */

public class StrengthScraper {

    // to run manually, just switch comments from line 22 to line 23
    // public static void main (String[] args) {
    public static void run () throws SQLException, IOException {
        String DB_URL = "jdbc:h2:~/scmanagerdb";
        File strengthLogger = new File("StrengthLogger.txt");
        ConnectionSource cs;
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
        DecimalFormat df = new DecimalFormat("#.##");
        BufferedWriter writer = new BufferedWriter(new FileWriter(strengthLogger, true));
        String currDate = date.format(new Date());
        writer.write(currDate);
        System.out.println(currDate);
        writer.newLine();
        cs = new JdbcConnectionSource(DB_URL);
        int count = 1;

        System.out.println("----------opened Connection!----------");
        Dao<Player, String> playersDao = DaoManager.createDao(cs, Player.class);
        TableUtils.createTableIfNotExists(cs,  Player.class);

        for(Player player:playersDao){
            String currTime = time.format(new Date());
            if(player.strength == 111.11) {
                String splitname=player.getName().replace(" ", "+");
                try {
                    player.strength = run(splitname);
                    playersDao.update(player);
                    System.out.println(currTime);
                    System.out.println(count+". " + player.getName() +"s strength updated to "+ player.strength);
                    System.out.println("https://www.ecosia.org/search?q=\""+splitname+"\"");
                    writer.write(currTime);
                    writer.newLine();
                    writer.write("https://www.ecosia.org/search?q=\""+splitname+"\"");
                    writer.newLine();
                    writer.write(count+". " + player.getName() +"s strength updated to "+ player.strength);
                    writer.newLine();
                    count++;
                }catch (Exception e){
                    System.out.println(currTime);
                    System.out.println("Scraping restricted for the moment, please try again later");
                    writer.write(currTime);
                    writer.newLine();
                    writer.write("Scraping restricted for the moment, please try again later");
                    writer.newLine();
                    break;
                }
            }
            else{
                System.out.println(currTime);
                System.out.println(count+". " + player.getName() +"s strength was already scraped");
                writer.write(currTime);
                writer.newLine();
                writer.write(count+". " + player.getName() +"s strength was already scraped");
                writer.newLine();
                count ++;
            }
        }
        cs.close();
    }

    public static double run(String plusName) throws Exception {
        //used ecosia search engine in consultation with Semir, due to more scraping restrictions from google
        String url = "https://www.ecosia.org/search?q=\""+plusName+"\"";
        String a = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0";
        String b = "Mozilla/5.0 (Linux; Android 10; SM-G965U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Mobile Safari/537.36";
        String c = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
        String d= "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
        String in ="";
        Random r = new Random();
        int i = (r.nextInt(3)+1);
        if(i==1) in = a;
        else if(i==2) in = b;
        else if(i==3) in = c;
        else in = d;
        Document document = Jsoup.connect(url).userAgent(in).get();
        String results = document.getElementsByClass("result-count").text();
        String[] textArray = results.split(" ");
        String result = textArray[0];
        result = result.replace(",", "");
        return result.equals("")? 0.00: Double.parseDouble(result);
    }
}
