/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public EmployeeSessionBean() {
    }
    
    @Override
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException
    {
        Employee employee = em.find(Employee.class, employeeId);
        
        if(employee != null) 
        {
            return employee;
        }
        else 
        {
            throw new EmployeeNotFoundException("Employee does not exist: " + employeeId);
        }
    }
    
    @Override
    public Employee login(String userName, String password) throws InvalidLoginCredentialException
    {
        try 
        {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.userName = :inuserName");
            query.setParameter("inuserName", userName);
            Employee employee = (Employee)query.getSingleResult();
            
            if(employee.getPassword().equals(password))
            {
                return employee;
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
    public Employee createNewEmployee(Employee employee)
    {
        em.persist(employee);
        em.flush();

        return employee;
    }
}
