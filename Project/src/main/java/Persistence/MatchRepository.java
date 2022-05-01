package Persistence;

import Model.Match;

public interface MatchRepository extends Repository<Integer, Match> {
    Iterable<Match> sortByDate();
}
