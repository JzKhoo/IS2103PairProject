/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    public CustomerSessionBean() {
    }

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Customer retrieveCustomerById(Long customerId) throws CustomerNotFoundException 
    {
        Customer customer = em.find(Customer.class, customerId);
        
        if(customer != null) 
        {
            return customer;
        }
        else 
        {
            throw new CustomerNotFoundException("Customer does not exist: " + customerId);
        }
    }
    
    @Override
    public Customer login(String email, String password) throws InvalidLoginCredentialException
    {
        try 
        {
            Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail");
            query.setParameter("inEmail", email);
            Customer customer = (Customer)query.getSingleResult();
            
            if(customer.getPassword().equals(password))
            {
                return customer;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
            
        }
        catch(NoResultException ex) 
        {
            throw new InvalidLoginCredentialException("Invalid login credential");
        }
    }
    
    @Override
    public Customer createNewCustomer(Customer customer) throws CustomerExistException, GeneralException
    {
        try
        {
            em.persist(customer);
            em.flush();

            return customer;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new CustomerExistException("Customer with same email already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
}
