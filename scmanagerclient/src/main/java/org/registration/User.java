package org.registration;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "USERS")
public class User {

    @DatabaseField(unique = true)
    private String EMAIL;
    @DatabaseField(unique = true)
    private String USERNAME;
    @DatabaseField
    private String PASSWORD;
    @DatabaseField
    private String STATUS;
    @DatabaseField (generatedId = true)
    private int ID;
    @DatabaseField
    private int POINTS;
    @DatabaseField
    private String FRIENDS;
    @DatabaseField
    private String REQUESTS;

    public User(){

    }
    public User(String EMAIL, String USERNAME, String PASSWORD, String STATUS){
        this.EMAIL = EMAIL;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.STATUS = STATUS;
        this.POINTS = 1000;
        this.FRIENDS = "0";
        this.REQUESTS = "0";
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public int getPoints() {
        return POINTS;
    }

    public void setPoints(int points) {
        this.POINTS = points;
    }

    public String getFRIENDS() {
        return FRIENDS;
    }

    public void setFRIENDS(String FRIENDS) {
        this.FRIENDS = FRIENDS;
    }

    public String getREQUESTS() {
        return REQUESTS;
    }

    public void setREQUESTS(String REQUESTS) {
        this.REQUESTS = REQUESTS;
    }
}
