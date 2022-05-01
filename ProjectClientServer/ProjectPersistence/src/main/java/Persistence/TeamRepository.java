package Persistence;

import Model.Team;

public interface TeamRepository extends Repository<Integer, Team>{
    boolean checkExistence(String teamName);
    Team findOneByTeamName(String teamName);
}
