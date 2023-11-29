package org.statistics;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import org.overview.Player;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Yelle Lieder
 */
@DatabaseTable(tableName = "Statistic")
public class Statistic {

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
    @DatabaseField
    private User OPPONENT;
    @DatabaseField
    private Double OVERALLSTRENGTH;

    public Double getOVERALLSTRENGTH() {
        return OVERALLSTRENGTH;
    }

    public void setOVERALLSTRENGTH(Double OVERALLSTRENGTH) {
        this.OVERALLSTRENGTH = OVERALLSTRENGTH;
    }



    /**
     * @param USER for which match gets stored
     * @param WIN of he has won
     * @param GOALS how many he striked
     * @param CONCEDED how many he received
     * @param SETUP his setup in this match
     * @param USEDPLAYERS all players he setup
     * @param OPPONENT against whom he competed
     */
    public Statistic(User USER, boolean WIN, int GOALS, int CONCEDED, String SETUP, ArrayList<Integer> USEDPLAYERS, User OPPONENT) {
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

    public User getOPPONENT() { return OPPONENT; }

    public void setOPPONENT(User OPPONENT) { this.OPPONENT = OPPONENT; }
}
