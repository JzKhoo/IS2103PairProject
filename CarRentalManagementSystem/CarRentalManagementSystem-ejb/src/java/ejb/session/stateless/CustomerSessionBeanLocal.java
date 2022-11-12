/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface CustomerSessionBeanLocal {

    // Register As Customer
    public Long createNewCustomer(Customer newCustomer) throws CustomerExistException, UnknownPersistenceException, InputDataValidationException;
    
    // Retrieve
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException;

}
