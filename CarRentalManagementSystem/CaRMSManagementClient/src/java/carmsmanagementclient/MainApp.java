/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;

/**
 *
 * @author khoojingzhi
 */
public class MainApp {
    
    private CarSessionBeanRemote carSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    
    private CustomerServiceExecutiveModule customerServiceExecutiveModule;
    private OperationsManagerModule operationsManagerModule;
    private SalesManagerModule salesManagerModule;
    
    private Employee currentEmployee;

    
    public MainApp() {
    }

    public MainApp(CarSessionBeanRemote carSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote) {
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
    }
    
    // Employee Login & Logout page
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
                        operationsManagerModule = new OperationsManagerModule(currentEmployee, carSessionBeanRemote, carCategorySessionBeanRemote, modelSessionBeanRemote, outletSessionBeanRemote, transitDriverDispatchRecordSessionBeanRemote);
                        salesManagerModule = new SalesManagerModule(currentEmployee, carCategorySessionBeanRemote, rentalRateSessionBeanRemote);

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
    
    // Employee Login
    private void doLogin() throws InvalidLoginCredentialException 
    {
        Scanner scanner = new Scanner(System.in);
        String name = "";
        String password = "";
        
        System.out.println("*** CaRMS Management System :: Login ***\n");
        System.out.print("Enter name> ");
        name = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(name.length() > 0 && password.length() > 0) 
        {
            currentEmployee = employeeSessionBeanRemote.login(name, password);
        }
        else 
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    // Select Module page
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management System ***\n");
            System.out.println("You are login as " + currentEmployee.getName() + " with " + currentEmployee.getRole() + " rights\n");
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
