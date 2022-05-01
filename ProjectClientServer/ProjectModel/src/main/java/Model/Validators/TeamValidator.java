package Model.Validators;

import Model.Team;


public class TeamValidator implements Validator<Team> {
    @Override
    public void validate(Team entity) throws ValidationException {
        String errors = "";
        if(!entity.getTeamName().matches("[a-zA-z]+"))
            errors = errors+"Team Name is invalid!\n";

        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
