package Model;

public class Team extends Entity<Integer>{
    private String manager;
    private String coach;
    private String[] players;


    public Team(String manager, String coach, String[] players) {
        this.manager = manager;
        this.coach = coach;
        this.players = players;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }
}
