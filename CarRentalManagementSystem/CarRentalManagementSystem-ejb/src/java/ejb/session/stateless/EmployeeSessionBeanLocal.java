/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface EmployeeSessionBeanLocal {
    
    // Create (Backend data initialization only)
    public Employee createNewEmployee(Employee newEmployee);

    // Retrieve by ID
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;
    
    // Retrieve by Name
    public Employee retrieveEmployeeByName(String name) throws EmployeeNotFoundException;

}
