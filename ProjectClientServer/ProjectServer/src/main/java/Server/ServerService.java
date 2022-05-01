package Server;

import Model.Customer;
import Model.Employee;
import Model.Match;
import Model.Team;
import Model.Validators.*;
import Persistence.CustomerRepository;
import Persistence.EmployeeRepository;
import Persistence.MatchRepository;
import Persistence.TeamRepository;
import Services.Observer;
import Services.Service;
import Services.ServiceException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerService implements Service {
    private TeamRepository teamDBRepository;
    private MatchRepository matchDBRepository;
    private CustomerRepository customerDBRepository;
    private EmployeeRepository employeeDBRepository;

    private final TeamValidator teamValidator = new TeamValidator();
    private final MatchValidator matchValidator = new MatchValidator();
    private final EmployeeValidator employeeValidator = new EmployeeValidator();
    private final CustomerValidator customerValidator = new CustomerValidator();

    private Map<Integer, Observer> loggedClients;

    public ServerService(TeamRepository teamDBRepository, MatchRepository matchDBRepository, CustomerRepository customerDBRepository, EmployeeRepository employeeDBRepository) {
        this.teamDBRepository = teamDBRepository;
        this.matchDBRepository = matchDBRepository;
        this.customerDBRepository = customerDBRepository;
        this.employeeDBRepository = employeeDBRepository;

        loggedClients=new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean loginAdmin(String pass) throws ServiceException {
        return pass.equals("admin");
    }

    @Override
    public synchronized boolean login(Employee employee, Observer client) throws ServiceException {
        if((!employee.getUsername().equals("")) && (!employee.getPassword().equals(""))){
            System.out.println(employee.getUsername());
            Employee employeeDB = employeeDBRepository.auth(employee.getUsername(), employee.getPassword());
            if(employeeDB != null){
                loggedClients.put(employeeDB.getId(), client);
                return true;
            }
        }
        return false;
    }

    public synchronized void logout(Employee employee, Observer client) throws ServiceException {
        Observer localClient=loggedClients.remove(employee.getId());
        if (localClient==null)
            throw new ServiceException("User "+employee.getId()+" is not logged in.");
    }

    @Override
    public synchronized void addTeam(String teamName) throws ServiceException {
        Team team = new Team(teamName);
        teamValidator.validate(team);

        teamDBRepository.save(team);
    }

    @Override
    public synchronized void addMatch(String teamName1, String teamName2, int ticketPrice, int numberOfSeats, String status) throws ServiceException {
        if(!teamDBRepository.checkExistence(teamName1) || !teamDBRepository.checkExistence(teamName2))
            throw new ServiceException("Non-existent teams");

        Match match = new Match(teamDBRepository.findOneByTeamName(teamName1), teamDBRepository.findOneByTeamName(teamName2), ticketPrice, numberOfSeats, status);
        matchValidator.validate(match);
        matchDBRepository.save(match);
    }

    @Override
    public synchronized void addEmployee(String firstName, String lastName, String username, String pass) throws ServiceException {
        if(employeeDBRepository.checkExistence(username))
            throw new DuplicateException("Username already exists");

        Employee employee = new Employee(firstName, lastName, username, pass);
        employeeValidator.validate(employee);
        employeeDBRepository.save(employee);
    }

    @Override
    public synchronized Iterable<Match> getAllMatches() throws ServiceException {
        return matchDBRepository.findAll();
    }

    @Override
    public synchronized void sellTickets(Customer customer) throws ServiceException {
        Match match = matchDBRepository.findOne(customer.getMatch().getId());
        if(customer.getNumberOfTickets() > match.getSeatsAvailable())
            throw new ValidationException("Not enough tickets");
        customerValidator.validate(customer);
        customerDBRepository.save(customer);

        Match newMatch = new Match(match.getTeam1(), match.getTeam2(), match.getTicketPrice(), match.getSeatsAvailable() - customer.getNumberOfTickets(), match.getStatus());
        newMatch.setId(customer.getMatch().getId());
        matchDBRepository.update(newMatch);
        notifySoldTickets(customer);
    }

    @Override
    public synchronized Iterable<Match> sortMatches() throws ServiceException {
        return matchDBRepository.findAllAvailable();
    }

    private final int defaultThreadsNo=5;
    private void notifySoldTickets(Customer customer) throws ServiceException {
        System.out.println(loggedClients);
        Iterable<Employee> employees = employeeDBRepository.findAll();

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
//        for(Employee employee: employees){
//            Observer chatClient=loggedClients.get(employee.getId());
//            if (chatClient!=null)
//                executor.execute(() -> {
//                    try {
//                        System.out.println("Notifying [" + employee.getId()+ "]");
//                        chatClient.ticketsSold(customer);
//                    } catch (ServiceException e) {
//                        System.err.println("Error notifying friend " + e);
//                    }
//                });
//        }
        for(Observer client: loggedClients.values()){
            executor.execute(() -> {
                try {
                    //System.out.println("Notifying [" + employee.getId()+ "]");
                    client.ticketsSold(customer);
                } catch (ServiceException e) {
                    System.err.println("Error notifying friend " + e);
                }
            });
        }

        executor.shutdown();
    }

}
