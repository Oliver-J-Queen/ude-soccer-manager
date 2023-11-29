package org.scraper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.serverconnection.ServerController;

import java.io.IOException;
import java.sql.SQLException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Parssa Mahdavi
 **/

public class   ScraperDatabase {

   // to run manually, just switch comments from line 22 to line 23
   // public static void main (String[] args) {
        public static void run () {
        File logger = new File("logger.txt");
        //String databaseUrl = "jdbc:h2:~/IdeaProjects/gruppe-4/PlayerDatabase";
        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        final String url = "https://www.fussballdaten.de/person/";
        ConnectionSource connectionSource;
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
        int count = 0;

        {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(logger, true));

                String datum = date.format(new Date());

                writer.write(datum);
                writer.newLine();

                connectionSource = new JdbcConnectionSource(databaseUrl);

                System.out.println("----------opened Connection!----------");

                Dao<Player, String> playersDao = DaoManager.createDao(connectionSource, Player.class);

                TableUtils.createTableIfNotExists(connectionSource,  Player.class);

                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Linux; Android 10; SM-G965U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Mobile Safari/537.36").get();

                Elements Buchstaben = doc.getElementsByClass("btn-group").first().select("a[href]");

                String Uhrzeit1 = time.format(new Date());

                writer.write(url + ": " + Uhrzeit1 + ", ");
                System.out.println(url + ": " + Uhrzeit1 + ", ");

                for(Element buchstabe : Buchstaben) {

                    String Uhrzeit2 = time.format(new Date());

                    writer.write(buchstabe.attr("abs:href") + ": " + Uhrzeit2 + ", ");

                    System.out.println(buchstabe.attr("abs:href") + ": " + Uhrzeit2 + ", ");

                    Document docu = Jsoup.connect(buchstabe.attr("abs:href")).get();

                    Elements unterBuchstaben = docu.select("a.btn-second-char.btn-secondary.btn").select("a[href]");

                    for(Element unterBuchstabe : unterBuchstaben) {

                        String Uhrzeit3 = time.format(new Date());

                        writer.write(unterBuchstabe.attr("abs:href") + ": " + Uhrzeit3 + ", ");

                        System.out.println(unterBuchstabe.attr("abs:href") + ": " + Uhrzeit3 + ", ");

                        Document docum = Jsoup.connect(unterBuchstabe.attr("abs:href")).get();


                        Elements Players = docum.getElementsByClass("col-md-4").select("a[href]");

                        int i = 0;

                        playerLoop:
                        for (Element player : Players) {

                            if (i++ > 5) {
                                break;
                            }
                            try {
                                String Uhrzeit4 = time.format(new Date());

                                writer.write(player.attr("abs:href") + ": " + Uhrzeit4 + ", ");

                                System.out.println(player.attr("abs:href") + ": " + Uhrzeit4 + ", ");


                                Document person = Jsoup.connect(player.attr("abs:href")).get();

                                String name = person.getElementsByClass("box-person-header").select("b:nth-of-type(2)").text();

                                String nation = person.select("dl.dl-horizontal:nth-of-type(1)").select("dd:nth-of-type(1)").text();

                                String verein = person.select("dl.dl-horizontal:nth-of-type(1)").select("dd:nth-of-type(2)").text();

                                String position = person.select("dl.dl-horizontal:nth-of-type(2)").select("dd:nth-of-type(3)").text();

                                String alter = person.select("dl.dl-horizontal:nth-of-type(1)").select("dd:nth-of-type(5)").text();

                                String[] namesplitter = name.split(" ");

                                if (name.equals("") || nation.equals("") || verein.equals("") || position.equals("") || alter.equals("")
                                        || name.equals("-") || nation.equals("-") || verein.equals("-") || position.equals("-") || alter.equals("-")
                                        || name.equals("Unbekannt") || nation.equals("Unbekannt") || verein.equals("Unbekannt") || position.equals("Unbekannt") || alter.equals("Unbekannt")
                                        || namesplitter.length == 1) {
                                    continue;
                                } else {
                                    count++;
                                    Player newPlayer = new Player(name, alter, nation, verein, position);
                                    playersDao.create(newPlayer);
                                    //System.out.println("----------A new Player got inserted!----------");
                                    System.out.println(count + ". Name: " + name + ", Nation: " + nation + ", Verein: " + verein + ", Position: " + position + ", Alter: " + alter);
                                    writer.write("Name: " + name + ", Nation: " + nation + ", Verein: " + verein + ", Position: " + position + ", Alter: " + alter);
                                    writer.newLine();
                                }
                            }
                            catch(Exception e){
                                continue playerLoop;
                            }
                        }
                    }
                }

                writer.close();
                connectionSource.close();
                System.out.println("----------Connection closed!----------");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
