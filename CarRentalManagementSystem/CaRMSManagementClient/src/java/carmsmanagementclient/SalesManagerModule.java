/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.Category;
import entity.Employee;
import entity.RentalRate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.Role;
import util.exception.CategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
public class SalesManagerModule {
    
    private Employee currentEmployee;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;

    public SalesManagerModule() {
    }

    public SalesManagerModule(Employee currentEmployee, RentalRateSessionBeanRemote rentalRateSessionBeanRemote, CategorySessionBeanRemote categorySessionBeanRemote) {
        this.currentEmployee = currentEmployee;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
    }
    
    
    
    public void menuSalesManager() throws InvalidAccessRightException
    {
        if(currentEmployee.getUserRole() != Role.SALES_MANAGER && currentEmployee.getUserRole() != Role.SYSTEM_ADMINISTRATOR)
        {
            throw new InvalidAccessRightException("You don't have Sales Manager rights to access the sales manager module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management System :: Sales Manager ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("6: Back\n");
            response = 0;
            
            while(response < 1 || response > 5)
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
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 6)
            {
                break;
            }
        }
    }
    
    private void createRentalRate()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Management System :: Create Rental Rate ***\n");
        System.out.print("Enter name> ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter rental rate type> ");
        String rentalRateType = scanner.nextLine().trim();
        System.out.print("Enter car category> ");
        String carCategory = scanner.nextLine().trim();
        System.out.print("Enter rate per day> ");
        double ratePerDay = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter start date time in the following format: dd/mm/yyy> ");
        String dateFormat = "dd/MM/yyyy";
        Date startDateTime = null;
        Date endDateTime = null;
        try{
            startDateTime = new SimpleDateFormat(dateFormat).parse(scanner.nextLine().trim());
        } catch (ParseException ex) {
            System.out.println("start date time set to null");
        }
        System.out.print("Enter end date time in the following format: dd/mm/yyy> ");
        try
        {
            endDateTime = new SimpleDateFormat(dateFormat).parse(scanner.nextLine().trim());
        } 
        catch (ParseException ex) 
        {
            System.out.println("start date time set to null");
        } 
        
        
        Category category = null;
        try
        {
            category = categorySessionBeanRemote.retrieveCategoryByType(carCategory);
        } 
        catch (CategoryNotFoundException ex) 
        {
            System.out.println("Creating new car category: " + carCategory + "...");
            category = categorySessionBeanRemote.createNewCategory(new Category(carCategory));
            System.out.println("Car category: " + carCategory + "created");
            
        }
        
        
        try 
        {
            rentalRateSessionBeanRemote.createNewRentalRate(new RentalRate(name, rentalRateType, ratePerDay, startDateTime, endDateTime, false, category));
        } 
        catch (UnknownPersistenceException ex) 
        {
            System.out.println("An unknown error occured when creating new rental rate: " + ex.getMessage());
        } 
        catch (InputDataValidationException ex) 
        {
            System.out.println("An unknown error occured when creating new rental rate: " + ex.getMessage());
        }
    }
    
    private void viewALlRentalRates() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Management System :: View All Rental Rates ***\n");
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRates();
        System.out.println("List of all rental rates: ");
        for (RentalRate rentalRate : rentalRates) 
        {
            System.out.println("Name: " + rentalRate.getName() + "; Category:" + rentalRate.getCategory().getType() + "; start date: " + rentalRate.getValidityStartDate() + "; end date: " + rentalRate.getValidityEndDate());
        }
    }
    
    private void viewRentalRateDetails()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Management System :: View Rental Rate Details ***\n");
        System.out.print("Enter name> ");
        String name = scanner.nextLine().trim();
        
        try{
            RentalRate rentalRate = rentalRateSessionBeanRemote.viewRentalRateDetails(name);
            System.out.println("Rental Rate details for " + name);
            System.out.println("Category:" + rentalRate.getCategory().getType() + "; start date: " + rentalRate.getValidityStartDate() + "; end date: " + rentalRate.getValidityEndDate());
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Invalid rental rate name: " + ex.getMessage());
        }
    }
}
