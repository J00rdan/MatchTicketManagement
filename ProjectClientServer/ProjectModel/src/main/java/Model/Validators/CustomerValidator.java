package Model.Validators;

import Model.Customer;

public class CustomerValidator implements Validator<Customer> {
    @Override
    public void validate(Customer entity) throws ValidationException {
        String errors = "";
        if(!entity.getFirstName().matches("[a-zA-z]+"))
            errors = errors+"First Name is invalid!\n";
        if(!entity.getLastName().matches("[a-zA-z]+"))
            errors = errors+"Last Name is invalid!\n";

        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}