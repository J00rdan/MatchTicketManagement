package Persistence;

import Model.Employee;
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

public class EmployeeDBRepository implements EmployeeRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public EmployeeDBRepository(Properties props) {
        logger.info("Initializing TeamDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Employee findOne(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "select * from employees where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){

                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String pass = result.getString("pass");
                    Employee employee = new Employee(firstName, lastName, pass);
                    employee.setId(id);

                    logger.traceExit();
                    return employee;
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
    public Iterable<Employee> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Employee> employees = new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from employees")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String pass = result.getString("pass");
                    Employee employee = new Employee(firstName, lastName, pass);
                    employee.setId(id);
                    employees.add(employee);
                }
            }
        }
        catch (SQLException e){
            logger.error(e);
            System.err.println("Error DB "+ e);
        }
        logger.traceExit();
        return employees;
    }

    @Override
    public void save(Employee entity) {
        logger.traceEntry("saving task {} ", entity);
        Connection con = dbUtils.getConnection();

        String query = "insert into employees (first_name, last_name, pass) values (?,?,?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){

            preStmt.setString(1, entity.getFirstName());
            preStmt.setString(2, entity.getLastName());
            preStmt.setString(3, entity.getPassword());
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

        String query = "delete from employees where id = (?)";
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
    public void update(Employee entity) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        String query = "update employees set pass = (?) where id = (?)";
        try(PreparedStatement preStmt = con.prepareStatement(query)){
            preStmt.setString(1, entity.getPassword());
            preStmt.setInt(2, entity.getId());

            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);

        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public boolean auth(String pass) {
        return true;
    }
}
