package org.overview;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Player")

public class Player {
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
    private double strength;

    public Player() {

    }
    public Player(String name, String age, String nationality, String club, String position, double strength) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.club = club;
        this.position = position;
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

    public void setStrength(double strength) {
        this.strength = strength;
    }

}
