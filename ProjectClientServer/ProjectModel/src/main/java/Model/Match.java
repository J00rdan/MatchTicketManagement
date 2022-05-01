package Model;

import Model.Entity;

public class Match extends Entity<Integer> {
    private Team Team1;
    private Team Team2;
    private int ticketPrice;
    private int seatsAvailable;
    private String status;

    public Match(Team Team1, Team Team2, int ticketPrice, int seatsAvailable, String status) {
        this.Team1 = Team1;
        this.Team2 = Team2;
        this.ticketPrice = ticketPrice;
        this.seatsAvailable = seatsAvailable;
        this.status = status;
    }

    public Match(){

    }

    public Team getTeam1() {
        return Team1;
    }

    public void setTeam1(Team Team1) {
        this.Team1 = Team1;
    }

    public Team getTeam2() {
        return Team2;
    }

    public void setTeam2(Team Team2) {
        this.Team2 = Team2;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Model.Match{" +
                "id=" + id +
                ", Team1=" + Team1 +
                ", Team2=" + Team2 +
                ", ticketPrice=" + ticketPrice +
                ", seatsAvailable=" + seatsAvailable +
                ", status='" + status + '\'' +
                '}';
    }
}
