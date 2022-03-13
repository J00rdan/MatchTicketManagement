package Repository;

import Model.Customer;
import Model.Match;
import Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomerDBRepository implements Repository<Integer, Customer> {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    private MatchDBRepository matchDBRepository;

    public CustomerDBRepository(Properties props, MatchDBRepository matchDBRepository) {
        logger.info("Initializing TeamDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        this.matchDBRepository = matchDBRepository;
    }

    @Override
    public Customer findOne(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "select * from customers where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){

                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    int matchID = result.getInt("match_id");
                    int numberOfTickets = result.getInt("number_of_tickets");
                    Customer customer = new Customer(firstName, lastName, matchDBRepository.findOne(matchID), numberOfTickets);
                    customer.setId(id);

                    logger.traceExit();
                    return customer;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Customer> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Customer> customers = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from customers")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    int matchID = result.getInt("match_id");
                    int numberOfTickets = result.getInt("number_of_tickets");
                    Customer customer = new Customer(firstName, lastName, matchDBRepository.findOne(matchID), numberOfTickets);
                    customer.setId(id);
                    customers.add(customer);
                }
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB "+ e);
        }
        logger.traceExit();
        return customers;
    }

    @Override
    public void save(Customer entity) {
        logger.traceEntry("saving task {} ", entity);
        Connection con = dbUtils.getConnection();

        String query = "insert into customers (first_name, last_name, match_id, number_of_tickets) values (?,?,?,?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){

            preStmt.setString(1, entity.getFirstName());
            preStmt.setString(2, entity.getLastName());
            preStmt.setInt(3, entity.getMatch().getId());
            preStmt.setInt(4, entity.getNumberOfTickets());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);

        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "delete from customers where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setInt(1, id);

            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);

        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Customer entity) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "update customers set number_of_tickets = (?) where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setInt(1, entity.getNumberOfTickets());
            preStmt.setInt(2, entity.getId());

            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);

        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }
}
