/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import entity.CarRentalReservationRecord;
import entity.Outlet;
import entity.TransitDriverDispatchRecord;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
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

    @EJB
    private CarRentalReservationRecordSessionBeanLocal carRentalReservationRecordSessionBeanLocal;

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
    
    // View All Cars
    @Override
    public List<Car> retrieveAllCars()
    {
        Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.model.carCategory, c.model.make, c.model.model, c.licensePlateNumber");
    
        return query.getResultList();
    }
    
    // View All Cars in required order of outlet
    @Override
    public List<Car> retrieveCarsAtOutlet(Outlet outlet)
    {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.outlet = :inOutlet");
        query.setParameter("inOutlet", outlet);
    
        return query.getResultList();
    }
    
    // View All Cars in required order of outlet
    @Override
    public List<Car> retrieveCarsNotAtOutlet(Outlet outlet)
    {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.outlet != :inOutlet");
        query.setParameter("inOutlet", outlet);
    
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
    
    //Search Car
    @Override
    public List<Car> searchCar(Timestamp pickupTime, String pickupOutletName, Timestamp returnTime, String returnOutletName)
    {
        Query query = em.createQuery("SELECT c FROM Car c");
        List<Car> cars = query.getResultList();
        List<Car> listOfAvailCars = new ArrayList<Car>();
        
        for (Car car : cars)
        {
            boolean isAvailable = true;
            //Check for availability status
            if (!car.getStatus().equals("Available"))
            {
                isAvailable = false;
            }
            
            // Check for transits
            if (car.getTransitDriverDispatchRecords() != null)
            {
                List<TransitDriverDispatchRecord> transitDriverDispatchRecords = car.getTransitDriverDispatchRecords();
                for (TransitDriverDispatchRecord transitDriverDispatchRecord : transitDriverDispatchRecords)
                {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(transitDriverDispatchRecord.getTransitStartTime().getTime());
                    cal.add(Calendar.HOUR, 2);
                    Timestamp transitEndTime = new Timestamp(cal.getTime().getTime());
                    if ((pickupTime.before(transitEndTime) && pickupTime.after(transitDriverDispatchRecord.getTransitStartTime())) ||
                            (returnTime.after(transitDriverDispatchRecord.getTransitStartTime()) && returnTime.before(transitEndTime)))
                    {
                        isAvailable = false;
                    }
                }
            }
            
            // If pickup time and return time falls between other reservation timings
            if (car.getCarRentalReservationRecords() != null)
            {
                List<CarRentalReservationRecord> carRentalReservationRecords = car.getCarRentalReservationRecords();
                for (CarRentalReservationRecord carRentalReservationRecord : carRentalReservationRecords)
                {
                    if ((pickupTime.before(carRentalReservationRecord.getReturnTime()) && pickupTime.after(carRentalReservationRecord.getPickupTime())) || 
                            (returnTime.after(carRentalReservationRecord.getPickupTime()) && returnTime.before(carRentalReservationRecord.getReturnTime())))
                    {
                        isAvailable = false;
                    }
                }
            }
            
            if (isAvailable)
            {
                listOfAvailCars.add(car);
            }
        }
        return listOfAvailCars;
    }
    
//    @Override
//    public Car reserveCar(Car car)
//    {
//        carRentalReservationRecordSessionBeanLocal.createNewCarRentalReservationRecord(new ( ))
//    }
    
    
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
