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
    
    
    // Create (Backend data initialization only)
    @Override
    public Employee createNewEmployee(Employee newEmployee)
    {
        em.persist(newEmployee);
        em.flush();

        return newEmployee;
    }
    
    
    // Retrieve
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
    
    
    // Login via CaRMSManagamentClient
    @Override
    public Employee login(String name, String password) throws InvalidLoginCredentialException
    {
        try 
        {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.name = :inName");
            query.setParameter("inName", name);
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
    
}
