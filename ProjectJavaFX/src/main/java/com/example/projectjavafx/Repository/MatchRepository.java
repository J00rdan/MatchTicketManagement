package com.example.projectjavafx.Repository;

import com.example.projectjavafx.Model.Match;

public interface MatchRepository extends Repository<Integer, Match> {
    Iterable<Match> findAllAvailable();
}
