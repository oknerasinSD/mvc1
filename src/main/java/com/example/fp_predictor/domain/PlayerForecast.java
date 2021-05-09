package com.example.fp_predictor.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PlayerForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long systemTournamentId;
    private long fanteamTournamentId;
    private long fanteamPlayerId;
    private String playerName;
    private String team;
    private String position;
    private double expectedPoints;
    private double price;

    public PlayerForecast() {
    }

    public PlayerForecast(
            long systemTournamentId,
            long fanteamTournamentId,
            long fanteamPlayerId,
            String playerName,
            String team,
            String position,
            double expectedPoints, double price
    ) {
        this.systemTournamentId = systemTournamentId;
        this.fanteamTournamentId = fanteamTournamentId;
        this.fanteamPlayerId = fanteamPlayerId;
        this.playerName = playerName;
        this.team = team;
        this.position = position;
        this.expectedPoints = expectedPoints;
        this.price = price;
    }

    public long getSystemTournamentId() {
        return systemTournamentId;
    }

    public void setSystemTournamentId(long systemTournamentId) {
        this.systemTournamentId = systemTournamentId;
    }

    public long getFanteamTournamentId() {
        return fanteamTournamentId;
    }

    public void setFanteamTournamentId(long fanteamTournamentId) {
        this.fanteamTournamentId = fanteamTournamentId;
    }

    public long getFanteamPlayerId() {
        return fanteamPlayerId;
    }

    public void setFanteamPlayerId(long fanteamPlayerId) {
        this.fanteamPlayerId = fanteamPlayerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getExpectedPoints() {
        return expectedPoints;
    }

    public void setExpectedPoints(double expectedPoints) {
        this.expectedPoints = expectedPoints;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
