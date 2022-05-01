package Model.Validators;

import Model.Employee;


public class EmployeeValidator implements Validator<Employee> {
    @Override
    public void validate(Employee entity) throws ValidationException {
        String errors = "";
        if(!entity.getFirstName().matches("[a-zA-z]+"))
            errors = errors+"First Name is invalid!\n";
        if(!entity.getLastName().matches("[a-zA-z]+"))
            errors = errors+"Last Name is invalid!\n";
        if(entity.getPassword().equals(""))
            errors = errors+"Username is null!\n";
        if(entity.getPassword().equals(""))
            errors = errors+"Password is null!\n";

        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}