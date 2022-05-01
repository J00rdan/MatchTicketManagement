package Persistence;

import Model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmployeeDBRepository implements EmployeeRepository {
    //private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    private SessionFactory sessionFactory;

    public EmployeeDBRepository(SessionFactory sessionFactory) {
        //logger.info("Initializing TeamDBRepository with properties: {} ",props);
        //dbUtils=new JdbcUtils(props);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Employee findOne(Integer id) {
        logger.traceEntry();

        try {
            try(Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    String queryString = "from Employee employee where employee.id = :idP";

                    List<Employee> result = session.createQuery(queryString, Employee.class).setParameter("idP", id).list();
                    tx.commit();

                    if(result.size() == 1){
                        return result.get(0);
                    }

                } catch (RuntimeException ex) {
                    System.err.println("Eroare la findOne "+ex);
                    if (tx != null)
                        tx.rollback();
                }
            }

        }catch (Exception e){
            System.err.println("Exception "+e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Employee> findAll() {
        logger.traceEntry();

        try {
            try(Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    List<Employee> result = session.createQuery("from Employee", Employee.class).list();
                    tx.commit();

                    return result;

                } catch (RuntimeException ex) {
                    System.err.println("Eroare la findAll "+ex);
                    if (tx != null)
                        tx.rollback();
                }
            }

        }catch (Exception e){
            System.err.println("Exception "+e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Employee entity) {
        logger.traceEntry();

        try {
            try(Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    session.save(entity);

                    tx.commit();

                } catch (RuntimeException ex) {
                    System.err.println("Eroare la save "+ex);
                    if (tx != null)
                        tx.rollback();
                }
            }

        }catch (Exception e){
            System.err.println("Exception "+e);
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry();

        try {
            try(Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    String queryString = "from Employee employee where employee.id = :idP";

                    Employee employee = session.createQuery(queryString, Employee.class).setParameter("idP", id).setMaxResults(1).uniqueResult();

                    session.delete(employee);

                    tx.commit();

                } catch (RuntimeException ex) {
                    System.err.println("Eroare la delete "+ex);
                    if (tx != null)
                        tx.rollback();
                }
            }

        }catch (Exception e){
            System.err.println("Exception "+e);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Employee entity) {
        logger.traceEntry();

        try {
            try(Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    Employee employee = session.load(Employee.class, entity.getId());
                    employee.setPassword(entity.getPassword());

                    tx.commit();

                } catch (RuntimeException ex) {
                    System.err.println("Eroare la update "+ex);
                    if (tx != null)
                        tx.rollback();
                }
            }

        }catch (Exception e){
            System.err.println("Exception "+e);
            e.printStackTrace();
        }
    }

    @Override
    public Employee auth(String username, String pass) {
        logger.traceEntry();

        try {
            try(Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    List<Employee> result = session.createQuery("from Employee", Employee.class).list();
                    tx.commit();

                    for (Employee employee :  result) {
                        if(employee.getUsername().equals(username) && employee.getPassword().equals(pass))
                            return employee;
                    }

                } catch (RuntimeException ex) {
                    System.err.println("Eroare la auth "+ex);
                    if (tx != null)
                        tx.rollback();
                }
            }

        }catch (Exception e){
            System.err.println("Exception "+e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkExistence(String username) {
        logger.traceEntry();

        try {
            try(Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    String queryString = "from Employee employee where employee.username = :userN";

                    List<Employee> result = session.createQuery(queryString, Employee.class).setParameter("userN", username).list();
                    tx.commit();

                    if(result.size() == 1)
                        return true;

                } catch (RuntimeException ex) {
                    System.err.println("Eroare la select "+ex);
                    if (tx != null)
                        tx.rollback();
                }
            }

        }catch (Exception e){
            System.err.println("Exception "+e);
            e.printStackTrace();
        }
        return false;
    }
}
