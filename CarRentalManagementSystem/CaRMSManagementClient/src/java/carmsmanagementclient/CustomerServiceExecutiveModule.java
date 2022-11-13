/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarRentalReservationRecordSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Car;
import entity.CarRentalReservationRecord;
import entity.Employee;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarNotFoundException;
import util.exception.CarRentalReservationRecordNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.OutletNotFoundException;
import util.exception.UpdateCarException;
import util.exception.UpdateCarRentalReservationRecordException;

/**
 *
 * @author khoojingzhi
 */
public class CustomerServiceExecutiveModule {
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CarRentalReservationRecordSessionBeanRemote carRentalReservationRecordSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    
    private Employee currentEmployee;

    public CustomerServiceExecutiveModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public CustomerServiceExecutiveModule(Employee currentEmployee, CarRentalReservationRecordSessionBeanRemote carRentalReservationRecordSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this();
        
        this.currentEmployee = currentEmployee;
        this.carRentalReservationRecordSessionBeanRemote = carRentalReservationRecordSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
    }
    
    // Main Navigation Page
    public void menuCustomerServiceExecutive() throws InvalidAccessRightException
    {
        if(!currentEmployee.getRole().equals("Customer Services Executive"))
        {
            throw new InvalidAccessRightException("You don't have Customer Service Executive rights to access the customer service executive module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management System :: Customer Service Executive Module ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Back\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doPickUpCar();
                }
                else if(response == 2)
                {
                    try
                    {
                        doReturnCar();
                    }
                    catch(OutletNotFoundException ex)
                    {
                        System.out.println("An error has occurred while recording return car: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 3)
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
    
    
    // PickUp Car
    private void doPickUpCar()
    {
        Scanner scanner = new Scanner(System.in);
        Long idInput;
        Integer response = 1;
        
        System.out.println("*** CaRMS Management System :: Customer Service Executive Module :: PickUp Car ***\n");
        
        // Retrieve reservation
        System.out.print("Enter ID of Car Rental Reservation Record> ");
        idInput = new Long(scanner.nextLine().trim());
        
        try
        {
            CarRentalReservationRecord carRentalReservationRecord = carRentalReservationRecordSessionBeanRemote.retrieveCarRentalReservationRecordById(idInput);
            
            // Check if paid
            if(!carRentalReservationRecord.isIsPaid())
            {
                System.out.println("Payment has not been made");
                System.out.println("Please recieve payment before allowing for car collection");
                System.out.println("Once payment has been made, please enter 1 to continue");
                System.out.println("To cancel this operation, press 2");
                
                response = scanner.nextInt();
                if(response == 1)
                {
                    try
                    {    
                    carRentalReservationRecord.setIsPaid(true);
                    carRentalReservationRecordSessionBeanRemote.updateCarRentalReservationRecord(carRentalReservationRecord);
                    }
                    catch(UpdateCarRentalReservationRecordException | InputDataValidationException ex)
                    {
                        System.out.println("Pickup operation unsuccessful!: " + ex.getMessage() + "\n");
                    }
                }
                if(response == 2)
                {
                    System.out.println("Pickup operation has been terminated");
                }
            }
            
            
            if(response == 1)
            {
                // Retrieve and update car status and location
                Car carToUpdate = carRentalReservationRecord.getCar();
                carToUpdate.setLocation(carRentalReservationRecord.getCustomer().getName());
                carToUpdate.setStatus("On Rental");
                
                try
                {
                    carSessionBeanRemote.updateCar(carToUpdate);
                    System.out.println("Pickup operation has been completed, car location and status have been updated");
                }
                catch(CarNotFoundException | UpdateCarException ex)
                {
                    System.out.println("An error has occurred while recording pickup car: " + ex.getMessage() + "\n");
                }
                catch(InputDataValidationException ex)
                {
                    System.out.println(ex.getMessage() + "\n");
                }
            }       
        }
        catch(CarRentalReservationRecordNotFoundException ex)
        {
            System.out.println("An error has occurred while recording pickup car: " + ex.getMessage() + "\n");
        }
    }
    
    
    // Return Car
    private void doReturnCar() throws OutletNotFoundException 
    {
        Scanner scanner = new Scanner(System.in);
        Long idInput;
        
        System.out.println("*** CaRMS Management System :: Customer Service Executive Module :: Return Car ***\n");
        
        System.out.print("Enter ID of Car Rental Reservation Record> ");
        idInput = new Long(scanner.nextLine().trim());
        
        try
        {
            CarRentalReservationRecord carRentalReservationRecord = carRentalReservationRecordSessionBeanRemote.retrieveCarRentalReservationRecordById(idInput);
            
            Car carToUpdate = carRentalReservationRecord.getCar();
            carToUpdate.setStatus("Available");
            carToUpdate.setLocation(carRentalReservationRecord.getReturnLocation());
            
            if(!carRentalReservationRecord.getPickupLocation().equals(carRentalReservationRecord.getReturnLocation()))
            {
                try
                {
                carToUpdate.setOutlet(outletSessionBeanRemote.retrieveOutletByName(carRentalReservationRecord.getReturnLocation()));
                }
                catch(OutletNotFoundException ex) 
                {
                    throw new OutletNotFoundException("An error has occurred while recording return car: " + ex.getMessage() + "\n");
                }
            }
            
            
            try
            {
                carSessionBeanRemote.updateCar(carToUpdate);
            }
            catch(CarNotFoundException | UpdateCarException ex)
            {
                System.out.println("An error has occurred while recording return car: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        catch(CarRentalReservationRecordNotFoundException ex)
        {
            System.out.println("An error has occurred while recording return car: " + ex.getMessage() + "\n");
        }
    }
}
