package com.example.projectjavafx.Service;

import com.example.projectjavafx.Model.Customer;
import com.example.projectjavafx.Model.Employee;
import com.example.projectjavafx.Model.Match;
import com.example.projectjavafx.Model.Team;
import com.example.projectjavafx.Repository.*;
import com.example.projectjavafx.Validators.*;

public class Service {
    private TeamRepository teamDBRepository;
    private MatchRepository matchDBRepository;
    private CustomerRepository customerDBRepository;
    private EmployeeRepository employeeDBRepository;

    private final TeamValidator teamValidator = new TeamValidator();
    private final MatchValidator matchValidator = new MatchValidator();
    private final EmployeeValidator employeeValidator = new EmployeeValidator();
    private final CustomerValidator customerValidator = new CustomerValidator();


    public Service(TeamDBRepository teamDBRepository, MatchDBRepository matchDBRepository, CustomerDBRepository customerDBRepository, EmployeeDBRepository employeeDBRepository) {
        this.teamDBRepository = teamDBRepository;
        this.matchDBRepository = matchDBRepository;
        this.customerDBRepository = customerDBRepository;
        this.employeeDBRepository = employeeDBRepository;
    }

    public boolean loginAdmin(String pass){
        return pass.equals("admin");
    }

    public boolean login(String username, String pass){
        if((!username.equals("")) && (!pass.equals(""))){
            return employeeDBRepository.auth(username, pass);
        }
        return false;
    }

    public void addTeam(String teamName){
        Team team = new Team(teamName);
        teamValidator.validate(team);

        teamDBRepository.save(team);
    }

    public void addMatch(String teamName1, String teamName2, int ticketPrice, int numberOfSeats, String status){
        if(!teamDBRepository.checkExistence(teamName1) || !teamDBRepository.checkExistence(teamName2))
            throw new ValidationException("Non-existent teams");

        Match match = new Match(teamDBRepository.findOneByTeamName(teamName1), teamDBRepository.findOneByTeamName(teamName2), ticketPrice, numberOfSeats, status);
        matchValidator.validate(match);
        matchDBRepository.save(match);
    }

    public void addEmployee(String firstName, String lastName, String username, String pass){
        if(employeeDBRepository.checkExistence(username))
            throw new DuplicateException("Username already exists");

        Employee employee = new Employee(firstName, lastName, username, pass);
        employeeValidator.validate(employee);
        employeeDBRepository.save(employee);
    }

    public Iterable<Match> getAllMatches(){
        return matchDBRepository.findAll();
    }

    public void sellTickets(String firstName, String lastName, int numberOfTickets, int matchId){
        Match match = matchDBRepository.findOne(matchId);
        if(numberOfTickets > match.getSeatsAvailable())
            throw new ValidationException("Not enough tickets");
        Customer customer = new Customer(firstName, lastName, match, numberOfTickets);
        customerValidator.validate(customer);
        customerDBRepository.save(customer);

        Match newMatch = new Match(match.getTeam1(), match.getTeam2(), match.getTicketPrice(), match.getSeatsAvailable() - numberOfTickets, match.getStatus());
        newMatch.setId(matchId);
        matchDBRepository.update(newMatch);
    }

    public Iterable<Match> sortMatches(){
        return matchDBRepository.findAllAvailable();
    }
}
