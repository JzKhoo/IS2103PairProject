/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarRentalReservationRecordSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Car;
import entity.CarRentalReservationRecord;
import entity.Customer;
import entity.RentalRate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.RentalFeeOption;
import util.exception.CarCategoryNotFoundException;
import util.exception.CustomerExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
public class MainApp 
{
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private CarRentalReservationRecordSessionBeanRemote carRentalReservationRecordSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    private Customer currentCustomer;
    
    
    
    public MainApp() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, CarRentalReservationRecordSessionBeanRemote carRentalReservationRecordSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote)
    {   
        this();
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.carRentalReservationRecordSessionBeanRemote = carRentalReservationRecordSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
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
                    doSearchCar();
                } else if (response == 2)
                {
                    doReserveCar();
                } else if (response == 5)
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
    
    private void doSearchCar()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation System :: " + currentCustomer.getName() + " :: Search Car ***\n\n");
        System.out.print("Enter pick up time in the following format: yyyy-mm-dd hh:mm:ss> ");
        Timestamp pickupTime = Timestamp.valueOf(scanner.nextLine().trim());
        System.out.print("Enter pick up outlet> ");
        String pickupOutlet = scanner.nextLine().trim();
        System.out.print("Enter return time in the following format: yyyy-mm-dd hh:mm:ss> ");
        Timestamp returnTime = Timestamp.valueOf(scanner.nextLine().trim());
        System.out.print("Enter return outlet> ");
        String returnOutlet = scanner.nextLine().trim();
        
        List<Car> availableCars = carSessionBeanRemote.searchCar(pickupTime, pickupOutlet, returnTime, returnOutlet);
        
        for (Car availableCar : availableCars)
        {
            System.out.println(availableCar.getModel().getCarCategory().getName());
            int rentalFee = 0;
            try {
                rentalFee = rentalRateSessionBeanRemote.calculateRentalFee(availableCar.getModel().getCarCategory().getName(), pickupTime, returnTime);
            } catch(CarCategoryNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.printf("%30s%30s%30s%30s\n", availableCar.getLicensePlateNumber(), availableCar.getModel().getCarCategory().getName(), availableCar.getModel().getModel(), rentalFee);
        }
    }
    
    private void doReserveCar()
    {
        Scanner scanner = new Scanner(System.in);
        CarRentalReservationRecord rentalRecord = new CarRentalReservationRecord();
        
        System.out.println("*** CaRMS Reservation System :: " + currentCustomer.getName() + " :: Reserve Car ***\n\n");
        System.out.print("Enter pick up time in the following format: yyyy-mm-dd hh:mm:ss> ");
        rentalRecord.setPickupTime(Timestamp.valueOf(scanner.nextLine().trim()));
        System.out.print("Enter pick up outlet> ");
        rentalRecord.setPickupLocation(scanner.nextLine().trim());
        System.out.print("Enter return time in the following format: yyyy-mm-dd hh:mm:ss> ");
        rentalRecord.setReturnTime(Timestamp.valueOf(scanner.nextLine().trim()));
        System.out.print("Enter return outlet> ");
        rentalRecord.setReturnLocation(scanner.nextLine().trim());
        System.out.print("Enter car category (leave blank if you wish to choose model instead)> ");
        rentalRecord.setCategoryTypeChoice(scanner.nextLine().trim());
        System.out.print("Enter reservation details> ");
        rentalRecord.setReservationDetails((scanner.nextLine().trim()));
        System.out.println("Enter: ");
        System.out.println("1 if you wish to pay upfront");
        System.out.println("2 if you wish to pay at time of pickup at outlet");
        System.out.print("> ");
        int payment = scanner.nextInt();
        int rentalFee = 0;
        try 
        {
            rentalFee = rentalRateSessionBeanRemote.calculateRentalFee(rentalRecord.getCategoryTypeChoice(), rentalRecord.getPickupTime(), rentalRecord.getReturnTime());
        } catch (CarCategoryNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
        if (payment == 1)
        {
            rentalRecord.setRentalFeeOption(RentalFeeOption.PAY_UPFRONT);
            double newBalance = currentCustomer.getBalance() - rentalFee;
            currentCustomer.setBalance(newBalance);
            rentalRecord.setCustomer(currentCustomer);
        } else if (payment ==2) {
            rentalRecord.setRentalFeeOption(RentalFeeOption.PAY_AT_PICKUP);
        }
        
        Set<ConstraintViolation<CarRentalReservationRecord>>constraintViolations = validator.validate(rentalRecord);

        if(constraintViolations.isEmpty())
        {
            try
            {
                CarRentalReservationRecord newCarRentalRecord = carRentalReservationRecordSessionBeanRemote.createNewCarRentalReservationRecord(rentalRecord);
                System.out.println("New car rental reservation created successfully! Car Rental Reservation Record ID: " + newCarRentalRecord.getCarRentalReservationRecordId() + "\n");
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
            showInputDataValidationErrorsForCarRentalReservationRecordEntity(constraintViolations);
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
    
    private void showInputDataValidationErrorsForCarRentalReservationRecordEntity(Set<ConstraintViolation<CarRentalReservationRecord>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
