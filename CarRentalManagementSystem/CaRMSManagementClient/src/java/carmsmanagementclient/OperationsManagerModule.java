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
public class OperationsManagerModule {
    
    private Employee currentEmployee;

    public OperationsManagerModule() {
    }

    public OperationsManagerModule(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }
    
    
    
    public void menuOperationsManager() throws InvalidAccessRightException
    {
        if(currentEmployee.getUserRole() != Role.OPERATIONS_MANAGER && currentEmployee.getUserRole() != Role.SYSTEM_ADMINISTRATOR)
        {
            throw new InvalidAccessRightException("You don't have Operations Manager rights to access the operations manager module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management System :: Operations Manager ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: Update Car");
            System.out.println("9: Delete Car");
            System.out.println("10: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("11: Assign Transit Driver");
            System.out.println("12: Update Transit As Completed");
            System.out.println("13: Back\n");
            response = 0;
            
            while(response < 1 || response > 12)
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
                else if(response == 5)
                {
                    break;
                }
                else if(response == 6)
                {
                    break;
                }
                else if(response == 7)
                {
                    break;
                }
                else if(response == 8)
                {
                    break;
                }
                else if(response == 9)
                {
                    break;
                }
                else if(response == 10)
                {
                    break;
                }
                else if(response == 11)
                {
                    break;
                }
                else if(response == 12)
                {
                    break;
                }
                else if(response == 13)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 13)
            {
                break;
            }
        }
    }
    
}
