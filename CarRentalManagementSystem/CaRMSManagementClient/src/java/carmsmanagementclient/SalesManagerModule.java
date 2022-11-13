/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Employee;
import entity.RentalRate;
import java.sql.Timestamp;
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
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    private Employee currentEmployee;

    public SalesManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SalesManagerModule(Employee currentEmployee, CarCategorySessionBeanRemote carCategorySessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this();
        
        this.currentEmployee = currentEmployee;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
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
                    try
                    {
                        doCreateRentalRate();
                    }
                    catch(CarCategoryNotFoundException ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                    
                }
                else if(response == 2)
                {
                    doViewAllRentalRates();
                }
                else if(response == 3)
                {
                    doViewRentalRateDetails();
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
    
    
    // Create New Rental Rate
    private void doCreateRentalRate() throws CarCategoryNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        
        System.out.println("*** CaRMS Management System :: Sales Manager Module :: Create Rental Rate ***\n");
        System.out.print("Enter name> ");
        newRentalRate.setName(scanner.nextLine().trim());
        System.out.print("Enter rental rate type> ");
        newRentalRate.setRentalRateType(scanner.nextLine().trim());
        
        try
        {
            System.out.print("Enter car category> ");
            newRentalRate.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByName(scanner.nextLine().trim()));
        } 
        catch(CarCategoryNotFoundException ex) 
        {
            throw new CarCategoryNotFoundException("Create new rental rate unsuccessful!: " + ex.getMessage());
        }
        
        System.out.print("Enter rate per day> ");
        newRentalRate.setRatePerDay(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter start date time in the following format: yyyy-mm-dd hh:mm:ss> ");
        newRentalRate.setStartDateTime(Timestamp.valueOf(scanner.nextLine().trim()));
        System.out.print("Enter end date time in the following format: yyyy-mm-dd hh:mm:sss> ");      
        newRentalRate.setEndDateTime(Timestamp.valueOf(scanner.nextLine().trim()));
        
        
        Set<ConstraintViolation<RentalRate>>constraintViolations = validator.validate(newRentalRate);
        
        if(constraintViolations.isEmpty())
        {        
            try
            {
                newRentalRate = rentalRateSessionBeanRemote.createNewRentalRate(newRentalRate);

                System.out.println("New rental rate created successfully!: Rental rate ID = " + newRentalRate.getRentalRateId() + "\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new rental rate!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForRentalRate(constraintViolations);
        }
        
    }
    
    
    // View All Rental Rates
    private void doViewAllRentalRates()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CaRMS Management System :: Sales Manager Module :: View All Rental Rates ***\n");
        
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRates();
        System.out.printf("%30s%30s%30s%30s%30s%30s%30s%30s\n", "Rental Rate ID", "Name", "Type", "Rate Per Day", "Start Date Time", "Start End Time", "Is Disabled", "Car Category");

        for(RentalRate rentalRate:rentalRates)
        {
            System.out.printf("%30s%30s%30s%30s%30s%30s%30s%30s\n", rentalRate.getRentalRateId(), rentalRate.getName(), rentalRate.getRentalRateType(), rentalRate.getRatePerDay(), rentalRate.getStartDateTime(), rentalRate.getEndDateTime(), rentalRate.isIsDisabled(), rentalRate.getCarCategory().getName());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    // View Rental Rate Details
    private void doViewRentalRateDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** CaRMS Management System :: Sales Manager Module :: View Rental Rate Details ***\n");
        System.out.print("Enter name> ");
        String name = scanner.nextLine().trim();
        
        try
        {
            RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateByName(name);
            System.out.printf("%30s%30s%30s%30s%30s%30s%30s%30s\n", "Rental Rate ID", "Name", "Type", "Rate Per Day", "Start Date Time", "Start End Time", "Is Disabled", "Car Category");
            System.out.printf("%30s%30s%30s%30s%30s%30s%30s%30s\n", rentalRate.getRentalRateId(), rentalRate.getName(), rentalRate.getRentalRateType(), rentalRate.getRatePerDay(), rentalRate.getStartDateTime(), rentalRate.getEndDateTime(), rentalRate.isIsDisabled(), rentalRate.getCarCategory().getName());
            System.out.println("------------------------");
            System.out.println("1: Update Rental Rate");
            System.out.println("2: Delete Rental Rate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateRentalRate(rentalRate);
            }
            else if(response == 2)
            {
                doDeleteRentalRate(rentalRate);
            }
        } 
        catch (RentalRateNotFoundException ex) {
            System.out.println("An error has occurred while retrieving rental rate: " + ex.getMessage() + "\n");
        }
    }
    
    
    // Update Rental Rate
    private void doUpdateRentalRate(RentalRate rentalRate)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        Integer integerInput;
        Timestamp timestampInput;
        
        System.out.println("*** CaRMS Management System :: Sales Manager Module :: View Rental Rate Details :: Update Rental Rate ***\n");
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
        
        scanner.nextLine();
        
        System.out.print("Enter Start Date Time in format 'yyyy-mm-dd hh:mm:ss' (blank if no change)> ");
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
                System.out.println("Rental Rate updated successfully!\n");
            }
            catch (RentalRateNotFoundException | UpdateRentalRateException ex) 
            {
                System.out.println("An error has occurred while updating rental rate: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForRentalRate(constraintViolations);
        }
    }
    
    
    // Delete Rental Rate
    private void doDeleteRentalRate(RentalRate rentalRate)
    {
        Scanner scanner = new Scanner(System.in); 
        String input;
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: View Rental Rate Details :: Delete Rental Rate ***\n");
        System.out.printf("Confirm Delete Rental Rate %s (Enter 'Y' to Delete)> ", rentalRate.getName());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                rentalRateSessionBeanRemote.deleteRentalRate(rentalRate.getRentalRateId());
                System.out.println("Rental Rate deleted successfully!\n");
            }
            catch (RentalRateNotFoundException | DeleteRentalRateException ex) 
            {
                System.out.println("An error has occurred while deleting rental rate: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Rental rate NOT deleted!\n");
        }

    }
    
    
    private void showInputDataValidationErrorsForRentalRate(Set<ConstraintViolation<RentalRate>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
