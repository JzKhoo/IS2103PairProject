/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarRentalReservationRecordSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.CarRentalReservationRecord;
import entity.Customer;
import entity.Outlet;
import entity.RentalRate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarRentalReservationRecordNotFoundException;
import util.exception.CustomerExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
public class MainApp 
{
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CarRentalReservationRecordSessionBeanRemote carRentalReservationRecordSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    
    private Customer currentCustomer;
    
    
    
    public MainApp() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public MainApp(CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarRentalReservationRecordSessionBeanRemote carRentalReservationRecordSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote)
    {   
        this();
        
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carRentalReservationRecordSessionBeanRemote = carRentalReservationRecordSessionBeanRemote;
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
        // Input fields
        Scanner scanner = new Scanner(System.in);
        Outlet pickupOutlet;
        Outlet returnOutlet;
        
        System.out.println("*** CaRMS Reservation System :: Search Car ***\n\n");
        
        System.out.print("Enter year of pickup date (YYYY)> ");
        int pickUpYear = scanner.nextInt() - 1900;
        System.out.print("Enter month of pickup date (MM) (1-12)> ");
        int pickUpMonth = scanner.nextInt() - 1;
        System.out.print("Enter day of pickup date (DD) (1-31)> ");
        int pickUpDay = scanner.nextInt();
        System.out.print("Enter hour of pickup date (HH) (0-23)> ");
        int pickUpHour = scanner.nextInt();
        System.out.print("Enter minutes of pickup date (HH) (0-59)> ");
        int pickUpMinutes = scanner.nextInt();
        Date pickupDate = new Date(pickUpYear, pickUpMonth, pickUpDay, pickUpHour, pickUpMinutes);
        
        System.out.print("Enter year of return date (YYYY> )");
        int returnYear = scanner.nextInt() - 1900;
        System.out.print("Enter month of return date (MM) (1-12)> ");
        int returnMonth = scanner.nextInt() - 1;
        System.out.print("Enter day of return date (DD) (1-31)> ");
        int returnDay = scanner.nextInt();
        System.out.print("Enter hour of return date (HH) (0-23)> ");
        int returnHour = scanner.nextInt();
        System.out.print("Enter minutes of return date (HH) (0-59)> ");
        int returnMinutes = scanner.nextInt();
        Date returnDate = new Date(returnYear, returnMonth, returnDay, returnHour, returnMinutes);
        
        scanner.nextLine();
        
        try
        {
            System.out.print("Enter pickup Outlet Name> ");
            pickupOutlet = outletSessionBeanRemote.retrieveOutletByName(scanner.nextLine().trim());
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
        
        // Search for cars
        List<Car> cars = carRentalReservationRecordSessionBeanRemote.searchCar(pickupDate, returnDate, pickupOutlet, returnOutlet);
        System.out.printf("%20s%20s%20s%30s%20s%20s%20s%20s\n", "Car Category", "Make", "Model", "License Plate Number", "Status", "Location", "Is Disabled", "Outlet");
        
        for(Car car:cars)
        {
            System.out.printf("%20s%20s%20s%30s%20s%20s%20s%20s\n", car.getModel().getCarCategory().getName(), car.getModel().getMake(), car.getModel().getModel(), car.getLicensePlateNumber(), car.getStatus(), car.getLocation(), car.isIsDisabled(), car.getOutlet().getName());
        }
        
        // Search for Total Rental Rates
        List<CarCategory> carCategories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        for(CarCategory carCategory:carCategories)
        {
            try
            {
                List<RentalRate> rentalRatesByCategory = carRentalReservationRecordSessionBeanRemote.calculateTotalRentalRate(carCategory, pickupDate, returnDate);
                int totalRentalFee = 0;
                for(RentalRate rentalRate:rentalRatesByCategory)
                {
                    totalRentalFee += rentalRate.getRatePerDay();
                }
                
                System.out.printf("CarCategory: %20s Total Rental Fee: %20s\n", carCategory.getName(), totalRentalFee);
            }
            catch(RentalRateNotFoundException ex) 
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
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
                    doReserveCar();
                }
                else if (response == 3)
                {
                    doViewReservationDetails();
                }
                else if (response == 4)
                {
                    doViewMyReservations();
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
    
    private void doReserveCar() 
    {
        Scanner scanner = new Scanner(System.in);
        CarRentalReservationRecord reservation = new CarRentalReservationRecord();
        
        System.out.println("*** CaRMS Reservation System :: Reserve Car ***\n\n");
        
        System.out.print("Enter year of pickup date (YYYY)> ");
        int pickUpYear = scanner.nextInt() - 1900;
        System.out.print("Enter month of pickup date (MM) (1-12)> ");
        int pickUpMonth = scanner.nextInt() - 1;
        System.out.print("Enter day of pickup date (DD) (1-31)> ");
        int pickUpDay = scanner.nextInt();
        System.out.print("Enter hour of pickup date (HH) (0-23)> ");
        int pickUpHour = scanner.nextInt();
        System.out.print("Enter minutes of pickup date (HH) (0-59)> ");
        int pickUpMinutes = scanner.nextInt();
        Date pickupDate = new Date(pickUpYear, pickUpMonth, pickUpDay, pickUpHour, pickUpMinutes);
        reservation.setPickupDate(pickupDate);
        
        System.out.print("Enter year of return date (YYYY> )");
        int returnYear = scanner.nextInt() - 1900;
        System.out.print("Enter month of return date (MM) (1-12)> ");
        int returnMonth = scanner.nextInt() - 1;
        System.out.print("Enter day of return date (DD) (1-31)> ");
        int returnDay = scanner.nextInt();
        System.out.print("Enter hour of return date (HH) (0-23)> ");
        int returnHour = scanner.nextInt();
        System.out.print("Enter minutes of return date (HH) (0-59)> ");
        int returnMinutes = scanner.nextInt();
        Date returnDate = new Date(returnYear, returnMonth, returnDay, returnHour, returnMinutes);
        reservation.setReturnDate(returnDate);
        
        scanner.nextLine();
        
        System.out.print("Enter pick up location> ");
        reservation.setPickupLocation(scanner.nextLine().trim());
        System.out.print("Enter return location> ");
        reservation.setReturnLocation(scanner.nextLine().trim());
        System.out.print("Enter category> ");
        reservation.setCategoryChoice(scanner.nextLine().trim());
        System.out.print("Enter make> ");
        reservation.setMakeChoice(scanner.nextLine().trim());
        System.out.print("Enter model> ");
        reservation.setModelChoice(scanner.nextLine().trim());
        
        System.out.println("Select to pay now or later ('Y' or blank)> ");
        String response = scanner.nextLine();
        if(response.equals("Y"))
        {
            reservation.setIsPaid(true);
        }
        else
        {
            reservation.setIsPaid(false);
        }
        
        System.out.print("Enter credit card number> ");
        reservation.setCreditCardNumber(scanner.nextLine().trim());
        System.out.print("Enter cvv> ");
        reservation.setCvv(scanner.nextLine().trim());
        reservation.setCustomer(currentCustomer);
        
        Set<ConstraintViolation<CarRentalReservationRecord>>constraintViolations = validator.validate(reservation);
        
        if(constraintViolations.isEmpty())
        {        
            try
            {
                reservation = carRentalReservationRecordSessionBeanRemote.createNewCarRentalReservationRecord(reservation);

                System.out.println("New Reservation created successfully!: Reservation ID = " + reservation.getCarRentalReservationRecordId()+ "\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new Reservation!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForCarRentalReservationRecord(constraintViolations);
        }
    }
    
    
    // View Reservation Details
    private void doViewReservationDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        Long idInput;
        
        System.out.println("*** CaRMS Reservation System :: View Reservation Details ***\n");
        System.out.print("Enter ID of Reservation Record> ");
        idInput = new Long(scanner.nextLine().trim());
        
        try
        {
            CarRentalReservationRecord carRentalReservationRecord = carRentalReservationRecordSessionBeanRemote.retrieveCarRentalReservationRecordById(idInput);
            System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s%10s%10s%20s%20s\n", "ID", "Pickup Date", "Pickup Location", "Return Date", "Return Location", "Category Choice", "Make Choice", "Model Choice", "Is Cancelled", "Is Paid", "Car", "Customer");
            System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s%10s%10s%20s%20s\n", carRentalReservationRecord.getCarRentalReservationRecordId(), carRentalReservationRecord.getPickupDate().toString(),
                    carRentalReservationRecord.getPickupLocation(), carRentalReservationRecord.getReturnDate().toString(), carRentalReservationRecord.getReturnLocation(), carRentalReservationRecord.getCategoryChoice(),
                    carRentalReservationRecord.getMakeChoice(), carRentalReservationRecord.getModelChoice(), carRentalReservationRecord.isIsCancelled(), carRentalReservationRecord.isIsPaid(),
                    carRentalReservationRecord.getCar().getLicensePlateNumber(), carRentalReservationRecord.getCustomer().getName());
            System.out.println("------------------------");
            System.out.println("1: Cancel Reservation");
            System.out.println("2: Back\n");
            System.out.print("> ");
            
            response = scanner.nextInt();

            if(response == 1)
            {
                doCancelReservation(carRentalReservationRecord);
            }
        }
        catch(CarRentalReservationRecordNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving reservation record: " + ex.getMessage() + "\n");
        }
    }
    
    // Cancel Reservation
    private void doCancelReservation(CarRentalReservationRecord carRentalReservationRecord)
    {
        Scanner scanner = new Scanner(System.in); 
        String input;
        
        System.out.println("*** CaRMS Reservation System :: Cancel Reservation ***\n");
        System.out.println("Confirm cancel reservation ('Y' or blank)> ");
        input = scanner.nextLine();
        
        if(input.equals('Y'))
        {
            int cost = carRentalReservationRecord.getPrice();
            boolean paid = carRentalReservationRecord.isIsPaid();
            
            System.out.println("Select cancellation penalty scenario \n");
            System.out.println("1: At least 14 days before pickup = no penalty");
            System.out.println("2: Less than 14 days but at least 7 days before pickup – 20% penalty");
            System.out.println("3: Less than 7 days but at least 3 days before pickup – 50% penalty");
            System.out.println("4: Less than 3 days before pickup – 70% penalty");
            int response = scanner.nextInt();
            
            if (response == 4) {
                System.out.println("You will be charged for 70% of your reservation");
                if(paid) {
                    System.out.println(cost * 0.3 + " will be returned to your card");
                } else {
                    System.out.println(cost * 0.7 + " will be deducted from your card");
                }
            } else if (response == 3) {
                System.out.println("You will be charged for 50% of your reservation");
                if(paid) {
                    System.out.println(cost * 0.5 + " will be returned to your card");
                } else {
                    System.out.println(cost * 0.5 + " will be deducted from your card");
                }
            } else if (response == 2) {
                System.out.println("You will be charged for 20% of your reservation");
                if(paid) {
                    System.out.println(cost * 0.8 + " will be returned to your card");
                } else {
                    System.out.println(cost * 0.2 + " will be deducted from your card");
                }
            } else {
                System.out.println("You will not be charged for your reservation");
                if(paid)
                {
                    System.out.println(cost + " will be returned to your card");
                }
            }
            
            carRentalReservationRecordSessionBeanRemote.cancelReservationById(carRentalReservationRecord.getCarRentalReservationRecordId());
        } 
        else
        {
            System.out.println("Reservation NOT cancelled!");
        }
    }
    
    // View All My Reservations
    private void doViewMyReservations() 
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CaRMS Reservation System :: View All My Reservations ***\n");
        
        List<CarRentalReservationRecord> carRentalReservationRecords = carRentalReservationRecordSessionBeanRemote.retrieveCarRentalReservationRecordByCustomerId(this.currentCustomer.getCustomerId());
        System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s%10s%10s%20s%20s\n", "ID", "Pickup Date", "Pickup Location", "Return Date", "Return Location", "Category Choice", "Make Choice", "Model Choice", "Is Cancelled", "Is Paid", "Car", "Customer");
        
        for(CarRentalReservationRecord carRentalReservationRecord:carRentalReservationRecords)
        {
            System.out.printf("%10s%20s%20s%20s%20s%20s%20s%20s%10s%10s%20s%20s\n", carRentalReservationRecord.getCarRentalReservationRecordId(), carRentalReservationRecord.getPickupDate().toString(),
                    carRentalReservationRecord.getPickupLocation(), carRentalReservationRecord.getReturnDate().toString(), carRentalReservationRecord.getReturnLocation(), carRentalReservationRecord.getCategoryChoice(),
                    carRentalReservationRecord.getMakeChoice(), carRentalReservationRecord.getModelChoice(), carRentalReservationRecord.isIsCancelled(), carRentalReservationRecord.isIsPaid(),
                    carRentalReservationRecord.getCar().getLicensePlateNumber(), carRentalReservationRecord.getCustomer().getName());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
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
    
    private void showInputDataValidationErrorsForCarRentalReservationRecord(Set<ConstraintViolation<CarRentalReservationRecord>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
