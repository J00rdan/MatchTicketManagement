package com.example.projectjavafx.Repository;

import com.example.projectjavafx.Model.Employee;

public interface EmployeeRepository extends Repository<Integer, Employee>{
    boolean auth(String username, String pass);
    boolean checkExistence(String username);
}
