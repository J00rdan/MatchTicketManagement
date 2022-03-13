package Model;

public class Customer extends Entity<Integer>{
    private String firstName;
    private String lastName;
    private Match match;
    private int numberOfTickets;

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", match=" + match +
                ", numberOfTickets=" + numberOfTickets +
                ", id=" + id +
                '}';
    }

    public Customer(String firstName, String lastName, Match match, int numberOfTickets) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.match = match;
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

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
