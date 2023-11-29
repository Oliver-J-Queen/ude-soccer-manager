package org.serverconnection;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.Lootbox.GeneralLootbox;
import org.Lootbox.SpecialLootbox;
import org.dataclasses.Player;
import org.dataclasses.Team;
import org.dataclasses.User;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopHandler extends AbstractHandler {
    ConnectionSource src;


    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitTarget = target.split("/");
        if (splitTarget[1].equals("shop") && splitTarget[2].equals("buy")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/text; charset=utf-8");
            try {
                if (splitTarget[3].equals("small")) {
                    buy_lootbox(response.getWriter(), splitTarget[4], 4, 400);
                }
                if (splitTarget[3].equals("medium")) {
                    buy_lootbox(response.getWriter(), splitTarget[4], 8, 800);
                }
                if (splitTarget[3].equals("large")) {
                    buy_lootbox(response.getWriter(), splitTarget[4], 12, 1200);
                }
                if (splitTarget[3].equals("special")) {
                    buy_special(response.getWriter(),splitTarget[4]);
                }
            }catch(SQLException ex){
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            baseRequest.setHandled(true);
        }
    }

    public void buy_lootbox(PrintWriter input, String username, int size, int price) {
        try {
            src = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
            ArrayList<Player> acquiredPlayers;
            Dao<User, String> USER = DaoManager.createDao(src, User.class);
            UserEditHandler ID = new UserEditHandler();
            String usrID = Integer.toString(ID.getIDFromUsername(username));

            User usr = USER.queryForId(usrID);

            if (usr.getPoints() >= price) {
                GeneralLootbox temp = new GeneralLootbox();
                List<String> tmp = new ArrayList<>();
                acquiredPlayers = temp.generater(usr, size);

                for (Player player : acquiredPlayers) {
                    tmp.add(player.getName());
                }
                usr.setPoints(usr.getPoints() - price);
                USER.update(usr);

                input.println(new Gson().toJson(tmp, ArrayList.class));
            }
        }catch(SQLException throwables){
            System.err.print("There is an internal issue with the database.");
        }
    }


    public void buy_special(PrintWriter input, String username) throws Exception {
        src = new JdbcConnectionSource("jdbc:h2:~/scmanagerdb");
        ArrayList<Player> acquiredPlayers;
        Dao<User, String> USER = DaoManager.createDao(src, User.class);

        UserEditHandler ID = new UserEditHandler();
        String usrID = Integer.toString(ID.getIDFromUsername(username));

        User usr = USER.queryForId(usrID);

        if(!usr.isFIRSTBOXRECIEVED()){
            SpecialLootbox lb = new SpecialLootbox();
            acquiredPlayers = lb.ersteller(usr);
            List<String>tmp = new ArrayList<>();

            for(Player player : acquiredPlayers){
                tmp.add(player.getName());
            }

            usr.setFIRSTBOXRECIEVED(true);
            USER.update(usr);
            
            input.println(new Gson().toJson(tmp, ArrayList.class));
        }
    }
}
