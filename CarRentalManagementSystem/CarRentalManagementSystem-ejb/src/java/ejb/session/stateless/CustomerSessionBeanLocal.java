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
import util.exception.GeneralException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface CustomerSessionBeanLocal {

    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException;

    public Customer createNewCustomer(Customer customer) throws CustomerExistException, GeneralException;
    
}