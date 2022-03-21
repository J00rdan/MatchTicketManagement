package com.example.projectjavafx.Repository;

import com.example.projectjavafx.Model.Team;

public interface TeamRepository extends Repository<Integer, Team>{
    boolean checkExistence(String teamName);
    Team findOneByTeamName(String teamName);
}
