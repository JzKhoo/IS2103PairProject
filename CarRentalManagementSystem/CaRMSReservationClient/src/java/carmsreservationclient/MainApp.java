/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import util.exception.CustomerExistException;
import util.exception.GeneralException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author khoojingzhi
 */
public class MainApp 
{
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    
    private Customer currentCustomer;
    
    
    
    public MainApp() 
    {
        currentCustomer = null;
    }
    
    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote)
    {
        this();
        
        this.customerSessionBeanRemote = customerSessionBeanRemote;
    }
    
    
    
    public void runApp() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to CaRMS Reservation System ***\n");
            

            System.out.println("1: Login");          
            System.out.println("2: Register as Customer");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if(response == 1)
                {
                    if(currentCustomer == null) 
                    {
                        try
                        {
                            doLogin();
                            System.out.println("Login successful as " + currentCustomer.getName() + "\n");
                            menuMain();
                        }
                        catch(InvalidLoginCredentialException ex) 
                        {
                            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                        }
                    }
                    else
                    {
                        System.out.println("You are already login as " + currentCustomer.getName() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doCreateCustomer();
                }
                else if (response == 3)
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
    
    private void doLogin() throws InvalidLoginCredentialException 
    {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";
        
        System.out.println("*** CaRMS Reservation System :: Login ***\n");
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(email.length() > 0 && password.length() > 0) 
        {
            currentCustomer = customerSessionBeanRemote.login(email, password);
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
            System.out.println("*** CaRMS Reservation System ***\n");
            System.out.println("You are login\n");
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 5) 
            {
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if (response == 5)
                {
                    currentCustomer = null;
                    break;
                }
                else 
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 5) 
            {
                break;
            }
        }
    }
    
    private void doCreateCustomer() 
    {
        try 
        {
            Scanner scanner = new Scanner(System.in);
            Customer newCustomer = new Customer();
            
            System.out.println("*** CaRMS Reservation System :: Register As Customer ***\n\n");
            System.out.print("Enter Email> ");
            newCustomer.setEmail(scanner.nextLine().trim());
            System.out.print("Enter Phone Number> ");
            newCustomer.setPhoneNumber(scanner.nextLine().trim());
            System.out.print("Enter Passport Number> ");
            newCustomer.setPassportNumber(scanner.nextLine().trim());
            System.out.print("Enter Name> ");
            newCustomer.setName(scanner.nextLine().trim());
            System.out.print("Enter Password> ");
            newCustomer.setPassword(scanner.nextLine().trim());
            
            newCustomer = customerSessionBeanRemote.createNewCustomer(newCustomer);
            System.out.println("Registration successful! Customer ID = " + newCustomer.getCustomerId() + "\n");
        }
        catch(CustomerExistException | GeneralException ex)
        {
            System.out.println("An error has occurred while creating the new customer: " + ex.getMessage() + "!\n");
        }
    }
}
