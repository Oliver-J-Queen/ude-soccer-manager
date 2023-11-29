package org.dataclasses;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

import static com.j256.ormlite.field.DataType.*;

/**
 * @author: Amine Bida
 */

@DatabaseTable (tableName = "Team")


public class Team {

    @DatabaseField(generatedId = true)
    private int TEAMID;
    @DatabaseField(unique = true)
    private String TEAMNAME;
    @DatabaseField
    private String FORMATION;

    @DatabaseField(dataType = SERIALIZABLE)
    private ArrayList<String> SETUP;

    @DatabaseField(dataType = SERIALIZABLE)
    private ArrayList<Integer> playersList;

    @DatabaseField(dataType = SERIALIZABLE)
    private ArrayList<String> RESERVE;


    @DatabaseField(foreign = true, columnName = "USERID")
    private User USER;

    //**
    @DatabaseField(foreign = true, columnName = "StadiumID")
    private Stadium STADIUM;

    public Team(String clubname, User user) {
        this.TEAMNAME = clubname;
        this.USER = user;
    }
    //**

    public Team(String clubname, User user, ArrayList<Integer> playersList) {
        this.TEAMNAME = clubname;
        this.USER = user;
        this.playersList=playersList;
    }

    public Team() {
    }

    public int getTEAMID() {

        return TEAMID;
    }

    public void setTEAMID(int TEAMID) {

        this.TEAMID = TEAMID;
    }

    public String getTEAMNAME() {

        return TEAMNAME;
    }

    public void setTEAMNAME(String TEAMNAME) {

        this.TEAMNAME = TEAMNAME;
    }

    public String getFORMATION() {

        return FORMATION;
    }

    public void setFORMATION(String FORMATION) {

        this.FORMATION = FORMATION;
    }

    public User getUSER() {
        return this.USER;
    }

    public void setUSER(User USER) {
        this.USER = USER;
    }

    public ArrayList<Integer> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(ArrayList<Integer> playersList) {
        this.playersList = playersList;
    }

    public ArrayList<String> getSETUP() {
        return SETUP;
    }

    public void setSETUP(ArrayList<String> SETUP) {
        this.SETUP = SETUP;
    }

    public ArrayList<String> getRESERVE() {
        return RESERVE;
    }

    public void setRESERVE(ArrayList<String> RESERVE) {
        this.RESERVE = RESERVE;
    }

    public Stadium getSTADIUM() {
        return STADIUM;
    }

    public void setSTADIUM(Stadium STADIUM) {
        this.STADIUM = STADIUM;
    }
}
