/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarRentalReservationRecord;
import entity.Outlet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarRentalReservationRecordNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCarRentalReservationRecordException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class CarRentalReservationRecordSessionBean implements CarRentalReservationRecordSessionBeanRemote, CarRentalReservationRecordSessionBeanLocal {    

    @PersistenceContext
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CarRentalReservationRecordSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }  
    

    // Retrieve by ID
    @Override
    public CarRentalReservationRecord retrieveCarRentalReservationRecordById(Long carRentalReservationRecordId) throws CarRentalReservationRecordNotFoundException
    {
       CarRentalReservationRecord carRentalReservationRecord = em.find(CarRentalReservationRecord.class, carRentalReservationRecordId);
       
       if(carRentalReservationRecord != null)
       {
           return carRentalReservationRecord;
       }
       else 
       {
           throw new CarRentalReservationRecordNotFoundException("Car Rental Reservation Record ID " + carRentalReservationRecordId + " does not exist!");
       }
    }
    
    // Update Reservation
    @Override
    public void updateCarRentalReservationRecord(CarRentalReservationRecord carRentalReservationRecord) throws CarRentalReservationRecordNotFoundException, UpdateCarRentalReservationRecordException, InputDataValidationException
    {
        if(carRentalReservationRecord != null && carRentalReservationRecord.getCarRentalReservationRecordId()!= null)
        {
            Set<ConstraintViolation<CarRentalReservationRecord>>constraintViolations = validator.validate(carRentalReservationRecord);
        
            if(constraintViolations.isEmpty())
            {
                CarRentalReservationRecord carRentalReservationRecordToUpdate = retrieveCarRentalReservationRecordById(carRentalReservationRecord.getCarRentalReservationRecordId());

                if(carRentalReservationRecordToUpdate.getCarRentalReservationRecordId().equals(carRentalReservationRecordToUpdate.getCarRentalReservationRecordId()))
                {
                    carRentalReservationRecordToUpdate.setIsPaid(carRentalReservationRecord.isIsPaid());
                }
                else
                {
                    throw new UpdateCarRentalReservationRecordException("Reservation ID of reservation record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new CarRentalReservationRecordNotFoundException("Reservation ID not provided for reservation to be updated");
        }
    }
    
    // Cancel reservation 
    @Override
    public void cancelReservationById(Long carRentalReservationRecordId)
    {
        try
        {
            CarRentalReservationRecord carRentalReservationRecordToUpdate = retrieveCarRentalReservationRecordById(carRentalReservationRecordId);
            carRentalReservationRecordToUpdate.setIsCancelled(true);
        }
        catch(CarRentalReservationRecordNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    // Retrieve by Customer ID
    @Override
    public List<CarRentalReservationRecord> retrieveCarRentalReservationRecordByCustomerId(Long customerId)
    {
        Query query = em.createQuery("SELECT r FROM CarRentalReservationRecord r WHERE r.customer.customerId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        
        return query.getResultList();
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarRentalReservationRecord>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
    
    
    
    
    
    
    
     
    
    
    
    
    
    
    
    
    
    
    // Search Car
    public List<Car> searchCar(Date pickUpDateTime, Date returnDateTime, Outlet pickUpOutlet, Outlet returnOutlet)
    {
        // Setting Timestamps for calculation
        Calendar c = Calendar.getInstance();
        c.setTime(pickUpDateTime);
        c.add(Calendar.HOUR_OF_DAY, -2);
        Date pickUpDateTimeMinusTwoHours = c.getTime();
        
        c.setTime(returnDateTime);
        c.add(Calendar.HOUR_OF_DAY, 2);
        Date returnDateTimePlusTwoHours = c.getTime();
        
        
        Query query = em.createQuery("SELECT c FROM Car c");
        
        List<Car> cars = query.getResultList();
        // Copy 
        List<Car> filteredCars = query.getResultList();
        // Filter by existing reservation records
        
        for(Car car:cars) 
        {
            // Filter cars that are not available || isDisabled
            if(car.isIsDisabled() || !car.getStatus().equals("Available")) 
            {
                filteredCars.remove(car);
            }
            else 
            {
                List<CarRentalReservationRecord> carRentalReservationRecords = car.getCarRentalReservationRecords();

                for(CarRentalReservationRecord carRentalReservationRecord : carRentalReservationRecords) 
                {
                    // Do not include in filter if reservation is cancelled
                    if(!carRentalReservationRecord.isIsCancelled())
                    {
                        // Scenario a: pickUpDateTime before reservation pickUpDateTime && returnDateTime after reservation returnDateTime
                        if(pickUpDateTime.before(carRentalReservationRecord.getPickupDate()) && returnDateTime.after(carRentalReservationRecord.getReturnDate())) 
                        {
                            filteredCars.remove(car);
                        }
                        // Scenario b: pickUpOutlet != reservation returnOutlet && pickUpDateTime - 2hrs before reservation returnDateTime 
                        else if(!pickUpOutlet.getName().equals(carRentalReservationRecord.getReturnLocation()) && pickUpDateTimeMinusTwoHours.before(carRentalReservationRecord.getReturnDate()))
                        {
                            filteredCars.remove(car);
                        }
                        // Scenario c: returnOutlet != reservation pickUpOutlet && returnDateTime + 2hrs after reservation pickUpDateTime
                        else if(!returnOutlet.getName().equals(carRentalReservationRecord.getPickupLocation()) && returnDateTimePlusTwoHours.after(carRentalReservationRecord.getPickupDate()))
                        {
                            filteredCars.remove(car);
                        }
                    }
                }
            }
        }
        
        return filteredCars;
    }
    
}
