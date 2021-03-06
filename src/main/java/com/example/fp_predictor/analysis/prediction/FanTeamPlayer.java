package com.example.fp_predictor.analysis.prediction;

/**
 * Данные об игроке от фэнтези-провайдера.
 */
public class FanTeamPlayer {

    /** UD игрока в системе FanTeam. */
    private final int tournamentId;

    /** ID игрока в системе FanTeam. */
    private final int id;

    /** Имя. */
    private final String name;

    /** Фамилия. */
    private final String surname;

    /** Команда. */
    private final String team;

    /** Позиция. */
    private final String position;

    /** Цена. */
    private final double price;

    /** Ожидаемые очки. */
    private double expectedPoints;

    public FanTeamPlayer(int tournamentId,
                         int id,
                         String name,
                         String surname,
                         String team,
                         String position,
                         double price) {
        this.tournamentId = id;
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.team = team;
        this.position = position;
        this.price = price;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTeam() {
        return team;
    }

    public String getPosition() {
        return position;
    }

    public double getPrice() {
        return price;
    }

    public void setExpectedPoints(double expectedPoints) {
        this.expectedPoints = expectedPoints;
    }

    @Override
    public String toString() {
        return "FanTeamPlayer{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", team='" + team + '\'' +
                ", position='" + position + '\'' +
                ", price=" + price +
                ", expectedPoints=" + expectedPoints +
                '}';
    }
}
