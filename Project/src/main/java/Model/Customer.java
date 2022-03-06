package Model;

public class Customer extends Entity<Integer>{
    private String firstName;
    private String lastName;
    private int matchID;
    private int numberOfTickets;

    public Customer(String firstName, String lastName, int matchID, int numberOfTickets) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.matchID = matchID;
        this.numberOfTickets = numberOfTickets;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}
