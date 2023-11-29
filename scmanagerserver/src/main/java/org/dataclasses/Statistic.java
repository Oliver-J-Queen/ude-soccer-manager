package org.dataclasses;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@DatabaseTable(tableName = "Statistic")
public class Statistic {

    ConnectionSource connectionSource;
    @DatabaseField(generatedId = true)
    private int STATISTICID;
    @DatabaseField
    private String TIMESTAMP;
    @DatabaseField(foreign = true, columnName = "USERID")
    private User USER;
    @DatabaseField
    private boolean WIN;
    @DatabaseField
    private int GOALS;
    @DatabaseField
    private int CONCEDED;
    @DatabaseField
    private String SETUP;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Integer> USEDPLAYERS;
    @DatabaseField(foreign = true)
    private User OPPONENT;
    @DatabaseField
    private Double OVERALLSTRENGTH;

    public Statistic(){

    }
    public Statistic(User USER, boolean WIN, int GOALS, int CONCEDED, String SETUP, ArrayList<Integer> USEDPLAYERS, User OPPONENT) throws SQLException {
        this.USER = USER;
        this.WIN = WIN;
        this.GOALS = GOALS;
        this.CONCEDED = CONCEDED;
        this.SETUP = SETUP;
        this.USEDPLAYERS = USEDPLAYERS;
        this.OPPONENT = OPPONENT;
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
        String currDate = date.format(new Date());
        String currTime = time.format(new Date());
        this.TIMESTAMP = currDate+", "+currTime;
        this.OVERALLSTRENGTH=sumUP();
    }
    private double sumUP() throws SQLException {
        double i =0;

        String databaseUrl = "jdbc:h2:~/scmanagerdb";
        connectionSource = new JdbcConnectionSource(databaseUrl);
        ArrayList<Player> player = new ArrayList<>();
        Dao<Player, String> playerDao = DaoManager.createDao(connectionSource, Player.class);


        for (Integer p: this.USEDPLAYERS){
            for (Player x:playerDao){
                if(p==x.getId()){
                    i+= x.getStrength();
                }
            }
        }
        return i;
    }

    public int getSTATISTICID() {
        return STATISTICID;
    }

    public void setSTATISTICID(int STATISTICID) {
        this.STATISTICID = STATISTICID;
    }

    public String getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setTIMESTAMP(String TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public User getUSER() {
        return USER;
    }

    public void setUSER(User USER) {
        this.USER = USER;
    }

    public boolean isWIN() {
        return WIN;
    }

    public void setWIN(boolean WIN) {
        this.WIN = WIN;
    }

    public int getGOALS() {
        return GOALS;
    }

    public void setGOALS(int GOALS) {
        this.GOALS = GOALS;
    }

    public int getCONCEDED() {
        return CONCEDED;
    }

    public void setCONCEDED(int CONCEDED) {
        this.CONCEDED = CONCEDED;
    }

    public String getSETUP() {
        return SETUP;
    }

    public void setSETUP(String SETUP) {
        this.SETUP = SETUP;
    }

    public ArrayList<Integer> getUSEDPLAYERS() {
        return USEDPLAYERS;
    }

    public void setUSEDPLAYERS(ArrayList<Integer> USEDPLAYERS) {
        this.USEDPLAYERS = USEDPLAYERS;
    }

    public User getOPPONENT() {
        return OPPONENT;
    }

    public void setOPPONENT(User OPPONENT) {
        this.OPPONENT = OPPONENT;
    }
}
