package Services;

import Model.Customer;
import Model.Match;

public interface Observer {
    void ticketsSold(Customer customer) throws ServiceException;
}
