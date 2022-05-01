package Persistence;

import Model.Employee;

public interface EmployeeRepository extends Repository<Integer, Employee>{
    boolean auth(String pass);
}
