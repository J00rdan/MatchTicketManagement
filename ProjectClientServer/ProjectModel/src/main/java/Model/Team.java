package Model;

import Model.Entity;
import org.springframework.stereotype.Component;

@Component
public class Team extends Entity<Integer> {
    private String teamName;

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return teamName;
    }
}