/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author khoojingzhi
 */
@Remote
public interface CustomerSessionBeanRemote {
    
    public Customer login(String email, String password) throws InvalidLoginCredentialException;
    
    public Long createNewCustomer(Customer customer);
}
