/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import entity.Employee;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author khoojingzhi
 */
public class SalesManagerModule {
    
    private Employee currentEmployee;

    public SalesManagerModule() {
    }

    public SalesManagerModule(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }
    
    
    
    public void menuSalesManager() throws InvalidAccessRightException
    {
        if(!currentEmployee.getRole().equals("Sales Manager"))
        {
            throw new InvalidAccessRightException("You don't have Sales Manager rights to access the sales manager module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management System :: Sales Manager MOdule ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 3)
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
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
}
