package protobuffprotocol;

import Model.Customer;
import Model.Employee;
import Model.Match;
import Model.Team;

import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    public static Protobufs.Request createLoginRequest(Employee employee){
        Protobufs.Employee employeeDTO = Protobufs.Employee.newBuilder().setId(String.valueOf(employee.getId())).setFirstName(employee.getFirstName()).setLastName(employee.getLastName()).setUserName(employee.getUsername()).setPasswd(employee.getPassword()).build();
        Protobufs.Request request =Protobufs.Request.newBuilder().setType(Protobufs.Request.Type.Login).setEmployee(employeeDTO).build();

        return request;
    }

    public static Protobufs.Request createLogoutRequest(Employee employee){
        Protobufs.Employee employeeDTO = Protobufs.Employee.newBuilder().setId(String.valueOf(employee.getId())).setFirstName(employee.getFirstName()).setLastName(employee.getLastName()).setUserName(employee.getUsername()).setPasswd(employee.getPassword()).build();
        Protobufs.Request request =Protobufs.Request.newBuilder().setType(Protobufs.Request.Type.Logout).setEmployee(employeeDTO).build();

        return request;
    }

    public static Protobufs.Request createGetAllRequest(){
        Protobufs.Request request = Protobufs.Request.newBuilder().setType(Protobufs.Request.Type.GetAllMatches).build();
        return request;
    }

    public static Protobufs.Request createGetAllAvailableRequest(){
        Protobufs.Request request = Protobufs.Request.newBuilder().setType(Protobufs.Request.Type.GetAllAvailable).build();
        return request;
    }

    public static Protobufs.Request createSellTicketsRequest(Customer customer){
        Protobufs.Customer customerDTO = Protobufs.Customer.newBuilder().setFirstName(customer.getFirstName()).setLastName(customer.getLastName()).setMatchId(customer.getMatch().getId().toString()).setNumberOfTickets(String.valueOf(customer.getNumberOfTickets())).build();
        Protobufs.Request request = Protobufs.Request.newBuilder().setType(Protobufs.Request.Type.SellTickets).setCustomer(customerDTO).build();
        return request;
    }

    public static Customer getCustomer(Protobufs.Response response){
        Match match = new Match();
        match.setId(Integer.valueOf(response.getCustomer().getMatchId()));
        Customer customer = new Customer(response.getCustomer().getFirstName(), response.getCustomer().getLastName(), match, Integer.parseInt(response.getCustomer().getNumberOfTickets()));
        customer.setId(Integer.valueOf(response.getCustomer().getId()));
        return customer;
    }

    public static String getError(Protobufs.Response response){
        return response.getError();
    }

    public static Iterable<Match> getMatches(Protobufs.Response response){
        List<Match> matches = new ArrayList<>();
        for(int i = 0; i < response.getMatchesCount(); i++){
            Protobufs.Match match = response.getMatches(i);
            Team team1 = new Team(match.getTeam1());
            Team team2 = new Team(match.getTeam2());
            Match realMatch = new Match(team1, team2, Integer.parseInt(match.getTicketPrice()), Integer.parseInt(match.getSeatsAvailable()), match.getStatus());
            realMatch.setId(Integer.parseInt(match.getId()));
            matches.add(realMatch);
        }
        return matches;
    }

}
