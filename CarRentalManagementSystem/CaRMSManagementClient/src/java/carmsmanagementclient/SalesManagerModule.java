/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.CarCategory;
import entity.Employee;
import entity.RentalRate;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author khoojingzhi
 */
public class SalesManagerModule {
    
    private Employee currentEmployee;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    
    private final Validator validator;
    private final ValidatorFactory validatorFactory;

    public SalesManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SalesManagerModule(Employee currentEmployee, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote) {
        this();
        this.currentEmployee = currentEmployee;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
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
            System.out.println("*** CaRMS Management System :: Sales Manager Module ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    createRentalRate();
                }
                else if(response == 2)
                {
                    viewALlRentalRates();
                }
                else if(response == 3)
                {
                    viewRentalRateDetails();
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
    
    private void createRentalRate()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Management System :: Sales Manager :: Create Rental Rate ***\n");
        System.out.print("Enter name> ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter rental rate type> ");
        String rentalRateType = scanner.nextLine().trim();
        System.out.print("Enter car category> ");
        String carCategory = scanner.nextLine().trim();
        System.out.print("Enter rate per day> ");
        int ratePerDay = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter start date time in the following format: yyyy-mm-dd hh:mm:ss> ");
        Timestamp startDateTime = null;
        Timestamp endDateTime = null;
        startDateTime = Timestamp.valueOf(scanner.nextLine().trim());
        System.out.print("Enter end date time in the following format: yyyy-mm-dd hh:mm:sss> ");
        endDateTime = Timestamp.valueOf(scanner.nextLine().trim());        
        
        CarCategory category = null;
        try
        {
            category = carCategorySessionBeanRemote.retrieveCarCategoryByName(carCategory);
        } 
        catch (CarCategoryNotFoundException ex) 
        {
            System.out.println("Creating new car category: " + carCategory + "...");
            category = carCategorySessionBeanRemote.createNewCategory(new CarCategory(carCategory));
            System.out.println("Car category: " + carCategory + "created");
            
        }
        
        try 
        {
            rentalRateSessionBeanRemote.createNewRentalRate(new RentalRate(name, rentalRateType, ratePerDay, startDateTime, endDateTime, category));
            System.out.println("\nRental Rate created successfully!\n");
        } 
        catch (UnknownPersistenceException ex) 
        {
            System.out.println("\nAn unknown error occured when creating new rental rate: " + ex.getMessage()+"\n");
        } 
        catch (InputDataValidationException ex) 
        {
            System.out.println("\nAn unknown error occured when creating new rental rate: " + ex.getMessage()+"\n");
        }
    }
    
    private void viewALlRentalRates() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Management System :: Sales Manager :: View All Rental Rates ***\n");
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRates();
        System.out.println("List of all rental rates: \n");
        for (RentalRate rentalRate : rentalRates) 
        {
            System.out.println("Name: " + rentalRate.getName());
            System.out.println("Rental Rate Type: " + rentalRate.getRentalRateType());
            System.out.println("Category: " + rentalRate.getCategory().getName());
            System.out.println("Start date: " + rentalRate.getStartDateTime());
            System.out.println("End date: " + rentalRate.getEndDateTime());
            System.out.println();
        }
        System.out.println();
    }
    
    private void viewRentalRateDetails()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Management System :: Sales Manager :: View Rental Rate Details ***\n");
        System.out.print("Enter name> ");
        String name = scanner.nextLine().trim();
        
        try{
            RentalRate rentalRate = rentalRateSessionBeanRemote.viewRentalRateDetails(name);
            System.out.println("\nRental Rate details for " + name + ":");
            System.out.println("Rental Rate Type: " + rentalRate.getRentalRateType());
            System.out.println("Category: " + rentalRate.getCategory().getName());
            System.out.println("Start date: " + rentalRate.getStartDateTime());
            System.out.println("End date: " + rentalRate.getEndDateTime());
            
            System.out.println("------------------------");
            System.out.println("1: Update Rental Rate");
            System.out.println("2: Delete Rental Rate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            int response = scanner.nextInt();

            if(response == 1)
            {
                updateRentalRate(rentalRate);
            }
            else if(response == 2)
            {
                deleteRentalRate(rentalRate);
            }
        } catch (RentalRateNotFoundException ex) {
            System.out.println("\nInvalid rental rate name: " + ex.getMessage() + "\n");
        }
    }
    
    private void updateRentalRate(RentalRate rentalRate)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        Integer integerInput;
        Timestamp timestampInput;
        
        System.out.println("*** CaRMS Management System :: Sales Manager :: View Product Details :: Update Rental Rate ***\n");
        System.out.print("Enter Rental Rate Type (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            rentalRate.setRentalRateType(input);
        }
        
        System.out.print("Enter Rate Per Day (negative number if no change)> ");
        integerInput = scanner.nextInt();
        if(integerInput >= 0)
        {
            rentalRate.setRatePerDay(integerInput);
        }
        
        System.out.print("Enter Start Date Time in format 'yyyy-mm-dd hh:mm:ss' (blank if no change)> ");
        scanner.nextLine();
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            timestampInput = Timestamp.valueOf(input);
            rentalRate.setStartDateTime(timestampInput);
        }
        
        System.out.print("Enter End Date Time in format 'yyyy-mm-dd hh:mm:ss' (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            timestampInput = Timestamp.valueOf(input);
            rentalRate.setStartDateTime(timestampInput);
        }
        
        Set<ConstraintViolation<RentalRate>>constraintViolations = validator.validate(rentalRate);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                rentalRateSessionBeanRemote.updateRentalRate(rentalRate);
                System.out.println("\nRental Rate updated successfully!\n");
            }
            catch (RentalRateNotFoundException | UpdateRentalRateException ex) 
            {
                System.out.println("\nAn error has occurred while updating product: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForRentalRateEntity(constraintViolations);
        }
    }
    
    private void deleteRentalRate(RentalRate rentalRate)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Management System :: Delete Rental Rate ***\n");
        System.out.print("Enter name> ");
        String name = scanner.nextLine().trim();
        
        try{
            rentalRateSessionBeanRemote.deleteRentalRate(name);
            System.out.println("\n" + name + " has been deleted!\n");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("\nInvalid rental rate name: " + ex.getMessage() + "\n");
        } catch (DeleteRentalRateException ex) {
            System.out.println("\nInvalid rental rate name: " + ex.getMessage() + "\n");
        }
    }
    
    private void showInputDataValidationErrorsForRentalRateEntity(Set<ConstraintViolation<RentalRate>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
