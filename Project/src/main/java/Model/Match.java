package Model;

public class Match extends Entity<Integer>{
    private int IDTeam1;
    private int IDTeam2;
    private int ticketPrice;
    private int seatsAvailable;
    private String status;

    public Match(int IDTeam1, int IDTeam2, int ticketPrice, int seatsAvailable, String status) {
        this.IDTeam1 = IDTeam1;
        this.IDTeam2 = IDTeam2;
        this.ticketPrice = ticketPrice;
        this.seatsAvailable = seatsAvailable;
        this.status = status;
    }

    public int getIDTeam1() {
        return IDTeam1;
    }

    public void setIDTeam1(int IDTeam1) {
        this.IDTeam1 = IDTeam1;
    }

    public int getIDTeam2() {
        return IDTeam2;
    }

    public void setIDTeam2(int IDTeam2) {
        this.IDTeam2 = IDTeam2;
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
}
