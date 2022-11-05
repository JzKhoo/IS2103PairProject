/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBean;
import entity.Employee;
import entity.Outlet;
import java.sql.Time;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.Role;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    @EJB
    private OutletSessionBean outletSessionBean;
   
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public DataInitializationSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() 
    {
        try
        {
            employeeSessionBeanLocal.retrieveEmployeeById(1l);
        }
        catch(EmployeeNotFoundException ex)
        {
            Outlet defaultOutlet = outletSessionBean.createNewOutlet(new Outlet("One", Time.valueOf("00:00:00"), Time.valueOf("23:59:59")));
            employeeSessionBeanLocal.createNewEmployee(new Employee(Role.CUSTOMER_SERVICE_EXECUTIVE, "User1", "Password", defaultOutlet));
            employeeSessionBeanLocal.createNewEmployee(new Employee(Role.OPERATIONS_MANAGER, "User2", "Password", defaultOutlet));
            employeeSessionBeanLocal.createNewEmployee(new Employee(Role.SALES_MANAGER, "User3", "Password", defaultOutlet));
        }
        
    }
}
