/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import entity.Employee;
import java.util.Scanner;
import util.enumeration.Role;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author khoojingzhi
 */
public class CustomerServiceExecutiveModule {
    
    private Employee currentEmployee;

    public CustomerServiceExecutiveModule() {
    }

    public CustomerServiceExecutiveModule(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }
    
    
    
    public void menuCustomerServiceExecutive() throws InvalidAccessRightException
    {
        if(currentEmployee.getUserRole() != Role.CUSTOMER_SERVICE_EXECUTIVE && currentEmployee.getUserRole() != Role.SYSTEM_ADMINISTRATOR)
        {
            throw new InvalidAccessRightException("You don't have Customer Service Executive rights to access the customer service executive module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management System :: Customer Service Executive ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Back\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    break;
                }
                else if(response == 2)
                {
                    break;
                }
                else if(response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
}
