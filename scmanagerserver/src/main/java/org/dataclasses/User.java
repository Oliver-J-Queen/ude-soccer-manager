package org.dataclasses;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

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
    @DatabaseField
    private boolean FIRSTBOXRECIEVED;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Integer> MATCHREQUESTS;
    @DatabaseField
    private String profilpicture;

    public User(){

    }

    /**
     * @param EMAIL
     * @param USERNAME
     * @param PASSWORD
     * @param STATUS (online/offline)
     * @param FIRSTBOXRECIEVED
     */
    public User(String EMAIL, String USERNAME, String PASSWORD, String STATUS, boolean FIRSTBOXRECIEVED){
        this.EMAIL = EMAIL;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.STATUS = STATUS;
        this.POINTS = 1000;
        this.FRIENDS = "0";
        this.REQUESTS = "0";
        this.FIRSTBOXRECIEVED = FIRSTBOXRECIEVED;
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

    public String getUsername() {
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

    public boolean isFIRSTBOXRECIEVED() {
        return FIRSTBOXRECIEVED;
    }

    public void setFIRSTBOXRECIEVED(boolean FIRSTBOXRECIEVED) {
        this.FIRSTBOXRECIEVED = FIRSTBOXRECIEVED;
    }

    public ArrayList<Integer> getMATCHREQUESTS() { return MATCHREQUESTS; }

    public void setMATCHREQUESTS(ArrayList<Integer> MATCHREQUESTS) { this.MATCHREQUESTS = MATCHREQUESTS; }

    public String getProfilpicture() {
        return profilpicture;
    }

    public void setProfilpicture(String ProfilPicture) { this.profilpicture = ProfilPicture; }
}
