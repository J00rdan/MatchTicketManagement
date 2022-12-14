import Model.Employee;
import Persistence.CustomerDBRepository;
import Persistence.EmployeeDBRepository;
import Persistence.MatchDBRepository;
import Persistence.TeamDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        TeamDBRepository teamDBRepository=new TeamDBRepository(props);
        MatchDBRepository matchDBRepository = new MatchDBRepository(props, teamDBRepository);
        CustomerDBRepository customerDBRepository = new CustomerDBRepository(props, matchDBRepository);
        EmployeeDBRepository employeeDBRepository = new EmployeeDBRepository(props);

        employeeDBRepository.save(new Employee("B", "B", "Asd"));

        for(Employee employee:employeeDBRepository.findAll())
            System.out.println(employee);

        /////Employee newEmployee = new Employee("B", "B", "AAA");
        ////newEmployee.setId(2);
       // employeeDBRepository.update(newEmployee);
       // System.out.println(newEmployee);

        //employeeDBRepository.delete(1);
    }
}
