package org.scraper;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Player")

public class Player {
    @DatabaseField (generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String age;
    @DatabaseField
    private String nationality;
    @DatabaseField
    private String club;
    @DatabaseField
    private String position;
    @DatabaseField
    double strength;

    public Player() {

    }
    public Player(String name, String age, String nationality, String club, String position) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.club = club;
        this.position = position;
        //Strength has no decimals from normal ecosia scraping, so we can use this as an indicator if strength was already scraped
        this.strength = 111.11;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double age) {
        this.strength = strength;
    }

    public int getId() { return id; }
}
