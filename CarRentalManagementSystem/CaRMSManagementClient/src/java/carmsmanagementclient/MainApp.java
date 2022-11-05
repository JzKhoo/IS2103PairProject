/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author khoojingzhi
 */
public class MainApp {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    
    private CustomerServiceExecutiveModule customerServiceExecutiveModule;
    private OperationsManagerModule operationsManagerModule;
    private SalesManagerModule salesManagerModule;
    
    private Employee currentEmployee;

    
    public MainApp() {
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
    }
    
    
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to CaRMS Management System ***\n");
            System.out.println("1: Login");          
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        customerServiceExecutiveModule = new CustomerServiceExecutiveModule(currentEmployee);
                        operationsManagerModule = new OperationsManagerModule(currentEmployee);
                        salesManagerModule = new SalesManagerModule(currentEmployee);

                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else 
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException 
    {
        Scanner scanner = new Scanner(System.in);
        String userName = "";
        String password = "";
        
        System.out.println("*** CaRMS Management System :: Login ***\n");
        System.out.print("Enter username> ");
        userName = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(userName.length() > 0 && password.length() > 0) 
        {
            currentEmployee = employeeSessionBeanRemote.login(userName, password);
        }
        else 
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management System ***\n");
            System.out.println("You are login as " + currentEmployee.getUserName() + " with " + currentEmployee.getUserRole() + " rights\n");
            System.out.println("1: Customer Service Executive Operation");
            System.out.println("2: Operations Manager");
            System.out.println("3: Sales Manager");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        customerServiceExecutiveModule.menuCustomerServiceExecutive();
                    }
                    catch(InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2)
                {
                    try
                    {
                        operationsManagerModule.menuOperationsManager();
                    }
                    catch(InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 3)
                {
                    try
                    {
                        salesManagerModule.menuSalesManager();
                    }
                    catch(InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 4)
                {
                    currentEmployee = null;
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
