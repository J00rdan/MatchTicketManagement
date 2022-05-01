package Services;

import Model.Customer;
import Model.Employee;
import Model.Match;

public interface Service {
    public boolean loginAdmin(String pass) throws ServiceException;
    public boolean login(Employee employee, Observer client) throws ServiceException;
    public void addTeam(String teamName) throws ServiceException;
    public void addMatch(String teamName1, String teamName2, int ticketPrice, int numberOfSeats, String status) throws ServiceException;
    public void addEmployee(String firstName, String lastName, String username, String pass) throws ServiceException;
    public Iterable<Match> getAllMatches()  throws ServiceException;
    public void sellTickets(Customer customer) throws ServiceException;
    public Iterable<Match> sortMatches() throws ServiceException;
    public void logout(Employee employee, Observer client) throws ServiceException;
}
