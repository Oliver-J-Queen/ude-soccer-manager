package org.dataclasses;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


import java.util.ArrayList;

@DatabaseTable(tableName = "TOURNAMENT")

public class Tournament {

    @DatabaseField(generatedId = true)
    private int ID;

    @DatabaseField
    private String name;

    @DatabaseField
    private int maxAnzahl;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<String> teilnehmer = new ArrayList<>();

    @DatabaseField
    private boolean status;

    @DatabaseField
    private int entryFee;

    @DatabaseField
    private String mode;

    @DatabaseField
    private String playercount;

    public Tournament(){

    }

    public Tournament(String name, int maxAnzahl, boolean status, int entryFee, String mode) {
        this.name = name;
        this.maxAnzahl = maxAnzahl;
        this.status = status;
        this.entryFee = entryFee;
        this.mode = mode;
      }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxAnzahl() {
        return maxAnzahl;
    }

    public void setMaxAnzahl(int maxAnzahl) {
        this.maxAnzahl = maxAnzahl;
    }

    public ArrayList<String> getTeilnehmer() {
        return teilnehmer;
    }

    public void setTeilnehmer(ArrayList<String> teilnehmer) {
        this.teilnehmer = teilnehmer;
    }

    public void addContestant(String userID) { this.teilnehmer.add(userID);}

    public boolean isReady() {
        return status;
    }

    public void setReady(boolean status) {
        this.status = status;
    }

    public int getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(int entryFee) {
        this.entryFee = entryFee;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPlayercount(){
        String actual = teilnehmer.size() + "/" + maxAnzahl;
        return actual;
    }
}
