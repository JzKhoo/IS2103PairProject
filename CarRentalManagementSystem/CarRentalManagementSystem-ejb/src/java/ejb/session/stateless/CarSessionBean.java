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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;
import util.exception.InputDataValidationException;
import util.exception.ModelDisabledException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CarSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }  
    
    
    // Create New Car
    @Override
    public Car createNewCar(Car newCar) throws UnknownPersistenceException, InputDataValidationException, ModelDisabledException
    {
        if(newCar.getModel().isIsDisabled())
        {
            throw new ModelDisabledException("Model is disabled.");
        }
        
        
        Set<ConstraintViolation<Car>>constraintViolations = validator.validate(newCar);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newCar);
                em.flush();

                return newCar;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    // Retrieve
    @Override
    public Car retrieveCarByLicensePlateNumber(String licensePlateNumber) throws CarNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.licensePlateNumber = :inLicensePlateNumber");
        query.setParameter("inLicensePlateNumber", licensePlateNumber);
        
        try
        {
            return (Car)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CarNotFoundException("Car " + licensePlateNumber + " does not exist!");
        }
    }
    
    @Override
    public Car retrieveCarByCarId(Long carId) throws CarNotFoundException
    {
        Car car = em.find(Car.class, carId);
        
        if(car != null)
        {
            return car;
        }
        else 
        {
            throw new CarNotFoundException("Car ID " + carId + "does not exist!");
        }
    }
    
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
            
            
    
    // View All Cars
    @Override
    public List<Car> retrieveAllCars()
    {
        Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.model.carCategory, c.model.make, c.model.model, c.licensePlateNumber");
    
        return query.getResultList();
    }
    
    
    // Update Car
    @Override
    public void updateCar(Car car) throws CarNotFoundException, UpdateCarException, InputDataValidationException
    {
        if(car != null && car.getCarId()!= null)
        {
            Set<ConstraintViolation<Car>>constraintViolations = validator.validate(car);
        
            if(constraintViolations.isEmpty())
            {
                Car carToUpdate = retrieveCarByCarId(car.getCarId());

                if(carToUpdate.getCarId().equals(car.getCarId()))
                {
                    carToUpdate.setLicensePlateNumber(car.getLicensePlateNumber());
                    carToUpdate.setStatus(car.getStatus());
                    carToUpdate.setLocation(car.getLocation());
                    
                }
                else
                {
                    throw new UpdateCarException("Car ID of car record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new CarNotFoundException("Car ID not provided for car to be updated");
        }
    }
    
    
    // Delete Car
    @Override
    public void deleteCar(Long carId) throws CarNotFoundException, DeleteCarException
    {
        Car carToRemove = retrieveCarByCarId(carId);
        
        List<CarRentalReservationRecord> carRentalReservationRecords = carToRemove.getCarRentalReservationRecords();
        
        if(carRentalReservationRecords.isEmpty())
        {
            carToRemove.getModel().getCars().remove(carToRemove);
            carToRemove.getOutlet().getCars().remove(carToRemove);
            
            em.remove(carToRemove);
        }
        else
        {
            carToRemove.setIsDisabled(true);
            throw new DeleteCarException("Car ID " + carId + " is associated with existing car rental reservations and cannot be deleted! Car has been marked as disabled.");
        }
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
}
