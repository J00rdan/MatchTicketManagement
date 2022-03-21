package com.example.projectjavafx.Validators;

import com.example.projectjavafx.Model.Match;
import com.example.projectjavafx.Model.Team;

public class MatchValidator implements Validator<Match> {
    @Override
    public void validate(Match entity) throws ValidationException {
        String errors = "";
        if(entity.getSeatsAvailable() < 0)
            errors = errors+"Seats number must be positive!\n";
        if((!entity.getStatus().equals("Regular")) && (!entity.getStatus().equals("Playoff")))
            errors = errors+"Invalid status!\n";

        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}