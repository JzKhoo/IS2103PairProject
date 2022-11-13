/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.CarRentalReservationRecord;
import entity.RentalRate;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale.Category;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
import util.exception.CarCategoryNotFoundException;
import util.exception.DeleteCarException;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @PersistenceContext
    private EntityManager em;
    
    

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RentalRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    // Create New Rental Rate
    @Override
    public RentalRate createNewRentalRate(RentalRate newRentalRate) throws UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<RentalRate>>constraintViolations = validator.validate(newRentalRate);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newRentalRate);
                em.flush();

                return newRentalRate;
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
    // View All Rental Rates
    @Override
    public List<RentalRate> retrieveAllRentalRates() 
    {
        Query query = em.createQuery("SELECT rr FROM RentalRate rr ORDER BY rr.carCategory, rr.startDateTime");
        
        return query.getResultList();
    }
    
    @Override
    public RentalRate retrieveRentalRateById(Long rentalRateId) throws RentalRateNotFoundException 
    {
        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);
        
        if(rentalRate != null)
        {
            return rentalRate;
        }
        else 
        {
            throw new RentalRateNotFoundException("Rental Rate " + rentalRateId + " does not exist!");
        }
    }
    
    @Override
    public RentalRate retrieveRentalRateByName(String name) throws RentalRateNotFoundException
    {
        Query query = em.createQuery("SELECT rr FROM RentalRate rr WHERE rr.name = :inName");
        query.setParameter("inName", name).getSingleResult();
        
        try
        {
            return (RentalRate)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new RentalRateNotFoundException("Rental Rate " + name + " does not exist!");
        }               
    }
    
    
    // Update Rental Rate
    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException
    {
        if(rentalRate != null && rentalRate.getRentalRateId() != null)
        {
            Set<ConstraintViolation<RentalRate>>constraintViolations = validator.validate(rentalRate);
        
            if(constraintViolations.isEmpty())
            {
                RentalRate rentalRateToUpdate = retrieveRentalRateById(rentalRate.getRentalRateId());

                if(rentalRateToUpdate.getRentalRateId().equals(rentalRate.getRentalRateId()))
                {
                    rentalRateToUpdate.setRentalRateType(rentalRate.getRentalRateType());
                    rentalRateToUpdate.setRatePerDay(rentalRate.getRatePerDay());
                    rentalRateToUpdate.setStartDateTime(rentalRate.getStartDateTime());
                    rentalRateToUpdate.setEndDateTime(rentalRate.getEndDateTime());
                }
                else
                {
                    throw new UpdateRentalRateException("Rental Rate ID of rental rate record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new RentalRateNotFoundException("Rental Rate ID not provided for rental rate to be updated");
        }
    }
    
    
    // Delete Rental Rate
    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException, DeleteRentalRateException
    {
        RentalRate rentalRateToRemove = retrieveRentalRateById(rentalRateId);

        List<CarRentalReservationRecord> carRentalReservationRecords = rentalRateToRemove.getCarRentalReservationRecords();
        
        if(carRentalReservationRecords.isEmpty())
        {
            rentalRateToRemove.getCarCategory().getRentalRates().remove(rentalRateToRemove);
            
            em.remove(rentalRateToRemove);
        }
        else
        {
            rentalRateToRemove.setIsDisabled(true);
            throw new DeleteRentalRateException("Rental Rate ID " + rentalRateId + " is associated with existing car rental reservations and cannot be deleted! Car has been marked as disabled.");
        }
    }
    
    @Override
    public int calculateRentalFee(String carCategoryName, Timestamp startDateTime, Timestamp endDateTime) throws CarCategoryNotFoundException
    {
        // Get Car Category
        CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByName(carCategoryName);
        
        int totalRentalFee = 0;
        // Get rental rates for promotions & peak
        Query promoPeakRRQuery = em.createQuery("SELECT rr FROM RentalRate rr WHERE rr.rentalRateType != :type AND rr.carCategory = :carCategory ORDER BY rr.startDateTime");
        promoPeakRRQuery.setParameter("type", "Default");
        promoPeakRRQuery.setParameter("carCategory", carCategory);
        List<RentalRate> promoPeakRRs = promoPeakRRQuery.getResultList();
        
        // Get default rental rate
        Query defaultQuery = em.createQuery("SELECT rr FROM RentalRate rr WHERE rr.rentalRateType = :type AND rr.carCategory = :carCategory");
        defaultQuery.setParameter("type", "Default");
        defaultQuery.setParameter("carCategory", carCategory);
        RentalRate defaultRR = (RentalRate) defaultQuery.getSingleResult();
        
        // Calculate total rental fee
        for (int i = 0; i < promoPeakRRs.size(); i++) 
        {
            if (startDateTime.before(promoPeakRRs.get(i).getStartDateTime()))
            {
                totalRentalFee += defaultRR.getRatePerDay();
                totalRentalFee += promoPeakRRs.get(i).getRatePerDay();
                if (i < promoPeakRRs.size() - 1){
                    if (promoPeakRRs.get(i + 1).getStartDateTime().before(promoPeakRRs.get(i).getEndDateTime()))
                    {
                        if (promoPeakRRs.get(i + 1).getRentalRateType().equals("Promotion"))
                        {
                            totalRentalFee += promoPeakRRs.get(i + 1).getRatePerDay();
                            startDateTime = addSecondsToDate(60, promoPeakRRs.get(i + 1).getEndDateTime());
                        } else {
                            startDateTime = addSecondsToDate(60, promoPeakRRs.get(i).getEndDateTime());
                        }
                    }
                }
            } else {
                totalRentalFee += promoPeakRRs.get(i).getRatePerDay();
                startDateTime = addSecondsToDate(60, promoPeakRRs.get(i).getEndDateTime());
            }
            
        }
        return totalRentalFee;
    }
    
    private Timestamp addSecondsToDate(int numOfSeconds, Timestamp date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, numOfSeconds);
        return new Timestamp(cal.getTime().getTime());
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}