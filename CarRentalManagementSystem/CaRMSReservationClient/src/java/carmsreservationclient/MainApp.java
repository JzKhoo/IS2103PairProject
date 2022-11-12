/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Car;
import entity.Customer;
import entity.Outlet;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
public class MainApp 
{
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CarSessionBeanRemote carSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    
    private Customer currentCustomer;
    
    
    
    public MainApp() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public MainApp(CarSessionBeanRemote carSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote)
    {   
        this();
        
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
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
            System.out.println("3: Search Car");
            System.out.println("4: Exit\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");
                
                response = scanner.nextInt();
                
                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doCreateCustomer();
                }
                else if (response == 3)
                {
                    try
                    {
                        doSearchCar();
                    }
                    catch(OutletNotFoundException ex)
                    {
                        System.out.println(ex.getMessage() + "\n");
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
    
    
    // Customer Login
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
    
    
    // Register As Customer
    private void doCreateCustomer() 
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
        System.out.print("Enter Card Number> ");
        newCustomer.setCardNumber(scanner.nextLine().trim());

        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);

        if(constraintViolations.isEmpty())
        {
            try
            {
                Long newCustomerId = customerSessionBeanRemote.createNewCustomer(newCustomer);
                System.out.println("New customer registered successfully!: " + newCustomerId + "\n");
            }
            catch(CustomerExistException ex)
            {
                System.out.println("An error has occurred while registering!: The email already exist\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new staff!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForCustomerEntity(constraintViolations);
        }
    }
    
    
    // Search Car
    private void doSearchCar() throws OutletNotFoundException 
    {
        Scanner scanner = new Scanner(System.in);
        Outlet pickUpOutlet;
        Outlet returnOutlet;
        
        System.out.println("*** CaRMS Reservation System :: Search Car ***\n\n");
        
        System.out.println("Enter year of pickup date (YYYY)");
        int pickUpYear = scanner.nextInt() - 1900;
        System.out.println("Enter month of pickup date (MM) (1-12)");
        int pickUpMonth = scanner.nextInt() - 1;
        System.out.println("Enter day of pickup date (DD) (1-31)");
        int pickUpDay = scanner.nextInt();
        System.out.println("Enter hour of pickup date (HH) (0-23)");
        int pickUpHour = scanner.nextInt();
        System.out.println("Enter minutes of pickup date (HH) (0-59)");
        int pickUpMinutes = scanner.nextInt();
        Date pickUpDate = new Date(pickUpYear, pickUpMonth, pickUpDay, pickUpHour, pickUpMinutes);
        
        System.out.println("Enter year of return date (YYYY)");
        int returnYear = scanner.nextInt() - 1900;
        System.out.println("Enter month of return date (MM) (1-12)");
        int returnMonth = scanner.nextInt() - 1;
        System.out.println("Enter day of return date (DD) (1-31)");
        int returnDay = scanner.nextInt();
        System.out.println("Enter hour of return date (HH) (0-23)");
        int returnHour = scanner.nextInt();
        System.out.println("Enter minutes of return date (HH) (0-59)");
        int returnMinutes = scanner.nextInt();
        Date returnDate = new Date(returnYear, returnMonth, returnDay, returnHour, returnMinutes);
        
        scanner.nextLine();
        
        try
        {
            System.out.print("Enter pickup Outlet Name> ");
            pickUpOutlet = outletSessionBeanRemote.retrieveOutletByName(scanner.nextLine().trim());
        }
        catch(OutletNotFoundException ex)
        {
            throw new OutletNotFoundException("Search car unsuccessful!: " + ex.getMessage() + "\n");
        }
        
        try
        {
            System.out.print("Enter return Outlet Name> ");
            returnOutlet = outletSessionBeanRemote.retrieveOutletByName(scanner.nextLine().trim());
        }
        catch(OutletNotFoundException ex)
        {
            throw new OutletNotFoundException("Search car unsuccessful!: " + ex.getMessage() + "\n");
        }
        
        List<Car> cars = carSessionBeanRemote.searchCar(pickUpDate, returnDate, pickUpOutlet, returnOutlet);
        System.out.printf("%20s%20s%20s%30s%20s%20s%20s%20s\n", "Car Category", "Make", "Model", "License Plate Number", "Status", "Location", "Is Disabled", "Outlet");
        
        for(Car car:cars)
        {
            System.out.printf("%20s%20s%20s%30s%20s%20s%20s%20s\n", car.getModel().getCarCategory().getName(), car.getModel().getMake(), car.getModel().getModel(), car.getLicensePlateNumber(), car.getStatus(), car.getLocation(), car.isIsDisabled(), car.getOutlet().getName());
        }
    }
    
    
    private void menuMain() 
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) 
        {
            System.out.println("*** CaRMS Reservation System ***\n");
            System.out.println("You are login as " + currentCustomer.getName() + "\n");
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
                
                if (response == 1)
                {
                    try
                    {
                        doSearchCar();
                    }
                    catch(OutletNotFoundException ex)
                    {
                        System.out.println(ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else if (response == 3)
                {
                    break;
                }
                else if (response == 4)
                {
                    break;
                }
                else if (response == 5)
                {
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
    
    
    
    
    private void showInputDataValidationErrorsForCustomerEntity(Set<ConstraintViolation<Customer>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
