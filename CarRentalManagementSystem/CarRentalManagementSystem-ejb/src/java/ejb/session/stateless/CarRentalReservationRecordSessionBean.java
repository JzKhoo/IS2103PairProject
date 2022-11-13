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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarRentalReservationRecordNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class CarRentalReservationRecordSessionBean implements CarRentalReservationRecordSessionBeanRemote, CarRentalReservationRecordSessionBeanLocal {    

    @PersistenceContext
    private EntityManager em;
    

    // Retrieve
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
    
    @Override
    public List<CarRentalReservationRecord> retrieveCarRentalReservationRecordByCustomerId(Long customerId)
    {
        Query query = em.createQuery("SELECT r FROM CarRentalReservationRecord r WHERE r.customer.customerId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        
        return query.getResultList();
    }
    
    
    // Update 
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
                        if(pickUpDateTime.before(carRentalReservationRecord.getPickUpDate()) && returnDateTime.after(carRentalReservationRecord.getReturnDate())) 
                        {
                            filteredCars.remove(car);
                        }
                        // Scenario b: pickUpOutlet != reservation returnOutlet && pickUpDateTime - 2hrs before reservation returnDateTime 
                        else if(!pickUpOutlet.getName().equals(carRentalReservationRecord.getReturnLocation()) && pickUpDateTimeMinusTwoHours.before(carRentalReservationRecord.getReturnDate()))
                        {
                            filteredCars.remove(car);
                        }
                        // Scenario c: returnOutlet != reservation pickUpOutlet && returnDateTime + 2hrs after reservation pickUpDateTime
                        else if(!returnOutlet.getName().equals(carRentalReservationRecord.getPickupLocation()) && returnDateTimePlusTwoHours.after(carRentalReservationRecord.getPickUpDate()))
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
