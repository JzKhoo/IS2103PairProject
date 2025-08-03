/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.CarRentalReservationRecord;
import entity.RentalRate;
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
    public RentalRate createNewRentalrate(RentalRate newRentalRate) throws UnknownPersistenceException, InputDataValidationException
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
    
    
    // View All Rental Rates
    @Override
    public List<RentalRate> retrieveAllRentalRates() 
    {
        Query query = em.createQuery("SELECT rr FROM RentalRate rr ORDER BY rr.carCategory, rr.startDate");
        
        return query.getResultList();
    }
    
    // Retrieve by ID
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
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }
    
    // Retrieve by Name
    @Override
    public RentalRate retrieveRentalRateByName(String name) throws RentalRateNotFoundException
    {
        Query query = em.createQuery("SELECT rr FROM RentalRate rr WHERE rr.name = :inName");
        query.setParameter("inName", name);
        
        try
        {
            return (RentalRate)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new RentalRateNotFoundException("Rental Rate " + name + " does not exist!");
        }               
    }
    
    // Retrieve by Category
    @Override
    public List<RentalRate> retrieveRentalRateByCarCategory(CarCategory carCategory)
    {
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE r.carCategory = :inCarCategory");
        query.setParameter("inCarCategory", carCategory);
        
        return query.getResultList();
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
                    rentalRateToUpdate.setName(rentalRate.getName());
                    rentalRateToUpdate.setRentalRateType(rentalRate.getRentalRateType());
                    rentalRateToUpdate.setRatePerDay(rentalRate.getRatePerDay());
                    rentalRateToUpdate.setStartDate(rentalRate.getStartDate());
                    rentalRateToUpdate.setEndDate(rentalRate.getEndDate());
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
