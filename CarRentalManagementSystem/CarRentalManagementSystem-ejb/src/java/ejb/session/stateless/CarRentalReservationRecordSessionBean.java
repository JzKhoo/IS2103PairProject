/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarRentalReservationRecord;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarRentalReservationRecordNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotFoundException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarRentalReservationRecordException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class CarRentalReservationRecordSessionBean implements CarRentalReservationRecordSessionBeanRemote, CarRentalReservationRecordSessionBeanLocal {    

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;
    

    @PersistenceContext
    private EntityManager em;
    
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CarRentalReservationRecordSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }  
    
    @Override
    public CarRentalReservationRecord createNewCarRentalReservationRecord(CarRentalReservationRecord newCarRentalReservationRecord) throws UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<CarRentalReservationRecord>>constraintViolations = validator.validate(newCarRentalReservationRecord);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newCarRentalReservationRecord);
                em.flush();

                return newCarRentalReservationRecord;
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
    @Override
    public List<Car> searchCar(Date pickupDate, Date returnDate, Outlet pickupOutlet, Outlet returnOutlet)
    {
        // Setting Dates for calculation
        Calendar c = Calendar.getInstance();
        c.setTime(pickupDate);
        c.add(Calendar.HOUR_OF_DAY, -2);
        Date pickupDateMinusTwoHours = c.getTime();
        
        c.setTime(returnDate);
        c.add(Calendar.HOUR_OF_DAY, 2);
        Date returnDatePlusTwoHours = c.getTime();
        
        // Initialise counters
        int numStandardSedan = 0;
        int numFamilySedan = 0;
        int numLuxurySedan = 0;
        int numSUVandMinivan = 0;
        
        // Get all cars
        Query query = em.createQuery("SELECT c FROM Car c");
        List<Car> cars = query.getResultList();
        
        // Output
        List<Car> filteredCars = query.getResultList();

        // Remove cars which are disabled or not available
        for(Car car:cars)
        {
            if(car.isIsDisabled() || !car.getStatus().equals("Available"))
            {
                filteredCars.remove(car);
            }
        }
        
        
        // Get all reservation records
        query = em.createQuery("SELECT r FROM CarRentalReservationRecord r");
        List<CarRentalReservationRecord> reservations = query.getResultList();
        
        
        // Set counters with total qty of cars
        for(Car car:filteredCars)
        {
            if(car.getModel().getCarCategory().getName().equals("Standard Sedan"))
            {
                numStandardSedan++;
            }
            if(car.getModel().getCarCategory().getName().equals("Family Sedan"))
            {
                numFamilySedan++;
            }
            if(car.getModel().getCarCategory().getName().equals("Luxury Sedan"))
            {
                numLuxurySedan++;
            }
            if(car.getModel().getCarCategory().getName().equals("SUV and Minivan"))
            {
                numSUVandMinivan++;
            } 
        }
        
        // Loop through the records adn identify records based on conditions, decrement counter based on category, remove specific cars based on model
        for(CarRentalReservationRecord reservation : reservations) 
        {
            // Set to false at the start, need to decrement/remove if becomes true.
            boolean toCheck = false;
            Model modelToRemove = null;
            
            // No need to check if reservation is cancelled
            if(!reservation.isIsCancelled())
            {
                // Scenario a: pickpDate before reservation pickpDate && returnDate after reservation returnDate
                if(pickupDate.before(reservation.getPickupDate()) && returnDate.after(reservation.getReturnDate())) 
                {
                    toCheck = true;
                }
                // Scenario b: pickUpOutlet != reservation returnOutlet && pickpDate - 2hrs before reservation returnDate 
                else if(!pickupOutlet.getName().equals(reservation.getReturnLocation()) && pickupDateMinusTwoHours.before(reservation.getReturnDate()))
                {
                    toCheck = true;
                }
                // Scenario c: returnOutlet != reservation pickUpOutlet && returnDate + 2hrs after reservation pickpDate
                else if(!returnOutlet.getName().equals(reservation.getPickupLocation()) && returnDatePlusTwoHours.after(reservation.getPickupDate()))
                {
                    toCheck = true;
                }
            }
            
            if(toCheck)
            {
                // Decrement counter based on category
                if(reservation.getCategoryChoice().equals("Standard Sedan"))
                {
                    numStandardSedan--;
                }
                if(reservation.getCategoryChoice().equals("Family Sedan"))
                {
                    numFamilySedan--;
                }
                if(reservation.getCategoryChoice().equals("Luxury Sedan"))
                {
                    numLuxurySedan--;
                }
                if(reservation.getCategoryChoice().equals("SUV and Minivan"))
                {
                    numSUVandMinivan--;
                }
                
                // Remove specific models
                if(!reservation.getMakeChoice().equals("") && !reservation.getModelChoice().equals(""))
                {
                    try
                    {
                        modelToRemove = modelSessionBeanLocal.retrieveModelByMakeAndModel(reservation.getMakeChoice(), reservation.getModelChoice());
                    }
                    catch(ModelNotFoundException ex)
                    {
                        System.out.println("Error while searching: " + ex.getMessage() + "\n");
                    }
                    
                    for(Car car:filteredCars)
                    {
                        if(car.getModel().equals(modelToRemove))
                        {
                            filteredCars.remove(car);
                        }
                    }
                }               
            }
        }
        
        // Remove cars if category counter <= 0
        if(numStandardSedan <= 0)
        {
            for(Car car:filteredCars)
            {
                if(car.getModel().getCarCategory().getName().equals("Standard Sedan"))
                {
                    filteredCars.remove(car);
                }
            }
        }
        
        if(numFamilySedan <= 0)
        {
            for(Car car:filteredCars)
            {
                if(car.getModel().getCarCategory().getName().equals("Family Sedan"))
                {
                    filteredCars.remove(car);
                }
            }
        }
        
        if(numLuxurySedan <= 0)
        {
            for(Car car:filteredCars)
            {
                if(car.getModel().getCarCategory().getName().equals("Luxury Sedan"))
                {
                    filteredCars.remove(car);
                }
            }
        }
        
        if(numSUVandMinivan <= 0)
        {
            for(Car car:filteredCars)
            {
                if(car.getModel().getCarCategory().getName().equals("SUV and Minivan"))
                {
                    filteredCars.remove(car);
                }
            }
        }
        
        return filteredCars;
    } 
    
    // Calculate total rental rate
    @Override
    public List<RentalRate> calculateTotalRentalRate(CarCategory carCategory, Date startDate, Date endDate) throws RentalRateNotFoundException
    {

        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        ArrayList<RentalRate> rentalRates = new ArrayList<>();
        List<RentalRate> carCategoryRates = rentalRateSessionBeanLocal.retrieveRentalRateByCarCategory(carCategory);
        
        while (startCalendar.before(endCalendar)) {
            
            Date curDate = startCalendar.getTime();
            int cheapestRateOfDay = -1337;
            RentalRate chosenRentalRate = null;
            boolean isFound = false;
            
            for (RentalRate rentalRate : carCategoryRates) {
                Date rentalRateStartDate = rentalRate.getStartDate();
                Date rentalRateEndDate = rentalRate.getEndDate();
                
                if (rentalRateStartDate == null && rentalRateStartDate == null || (rentalRateStartDate.before(curDate) || rentalRateStartDate.equals(curDate)) && (rentalRateEndDate.after(curDate)) || rentalRateEndDate.equals(curDate)) {
                    if (rentalRate.isIsDisabled()== false && (cheapestRateOfDay == -1337 || rentalRate.getRatePerDay()< cheapestRateOfDay)) {
                        cheapestRateOfDay = rentalRate.getRatePerDay();
                        chosenRentalRate = rentalRate;
                        isFound = true;
                    }
                }
            }
            if (isFound) {
                rentalRates.add(chosenRentalRate);
            } else {
                throw new RentalRateNotFoundException("Rental Rates not found for current date and category 2");
            }
            isFound = false;
            startCalendar.add(Calendar.DATE, 1);
        }

        if (!rentalRates.isEmpty()) {
            return rentalRates;
        } else {
            throw new RentalRateNotFoundException("Rental Rates not found for current date and category 3");
        }
    }
    
}
