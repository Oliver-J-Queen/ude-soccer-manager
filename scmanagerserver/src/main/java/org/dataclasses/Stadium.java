package org.dataclasses;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author: Amine Bida
 */

@DatabaseTable (tableName = "Stadium")

public class Stadium {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField         // 5000, 25000, 50000, 65000
    private Integer capacity;
    @DatabaseField         // small, medium, big, enlarged
    private String parking;
    @DatabaseField         // small, medium, big, enlarged
    private String imbis;

    @DatabaseField(foreign = true, columnName = "userID")
    private User user;
    @DatabaseField(foreign = true, columnName = "teamID")
    private Team Team;

    public Stadium() {
    }

    public Stadium(String name, Team team, Integer capacity, String parking, String imbis, User user) {
        this.name = name;
        this.Team = team;
        this.capacity = capacity;
        this.parking = parking;
        this.imbis = imbis;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getImbis() {
        return imbis;
    }

    public void setImbis(String imbis) {
        this.imbis = imbis;
    }

    public User getUSER() {
        return user;
    }

    public void setUSER(User user) {
        this.user = user;
    }

    public org.dataclasses.Team getTeam() {
        return Team;
    }

    public void setTeam(org.dataclasses.Team team) {
        Team = team;
    }
}
