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
    private CarRentalReservationRecordSessionBeanLocal carRentalReservationRecordSessionBeanLocal;

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
    
    
    // Create (incomplete)
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
    
    // sorted in ascending order by car category, validity start date and validity end date
    @Override
    public List<RentalRate> retrieveAllRentalRates() {
        Query query = em.createQuery("SELECT rr FROM RentalRate rr ORDER BY rr.carCategory, rr.startDateTime");
        return query.getResultList();
    }
    
    @Override
    public RentalRate retrieveRentalRateByName(String rentalRateName) throws RentalRateNotFoundException
    {
        Query query = em.createQuery("SELECT rr FROM RentalRate rr WHERE rr.name = :inName");
        RentalRate rentalRate = (RentalRate) query.setParameter("inName", rentalRateName).getSingleResult();
        
        if(rentalRate != null)
        {
            return rentalRate;
        }
        else
        {
            throw new RentalRateNotFoundException("Rental Rate name " + rentalRateName + " does not exist!");
        }               
    }
    
    @Override
    public RentalRate viewRentalRateDetails(String name) throws RentalRateNotFoundException{
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
    
    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException
    {
        if(rentalRate != null && rentalRate.getName()!= null)
        {
            Set<ConstraintViolation<RentalRate>>constraintViolations = validator.validate(rentalRate);
        
            if(constraintViolations.isEmpty())
            {
                RentalRate rentalrateToUpdate = retrieveRentalRateByName(rentalRate.getName());

                if(rentalrateToUpdate.getName().equals(rentalRate.getName()))
                {
//                    rentalrateToUpdate.setName(rentalRate.getName());
                    rentalrateToUpdate.setRentalRateType(rentalRate.getRentalRateType());
                    rentalrateToUpdate.setRatePerDay(rentalRate.getRatePerDay());
                    rentalrateToUpdate.setStartDateTime(rentalRate.getStartDateTime());
                    rentalrateToUpdate.setEndDateTime(rentalRate.getEndDateTime());
                    
                }
                else
                {
                    throw new UpdateRentalRateException("Name of rental rate to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new RentalRateNotFoundException("Rental Rate Name not provided for rental rate to be updated");
        }
    }
    
    @Override
    public void deleteRentalRate(String name) throws RentalRateNotFoundException, DeleteRentalRateException
    {
        RentalRate rentalRateToRemove = this.viewRentalRateDetails(name);
        
        List<CarRentalReservationRecord> carRentalReservationRecords = carRentalReservationRecordSessionBeanLocal.retrieveAllCarRentalReservationRecords();
        boolean isUsed = false;
        
        for (CarRentalReservationRecord carRentalReservationRecord : carRentalReservationRecords)
        {
            if (carRentalReservationRecord.getDate().compareTo(rentalRateToRemove.getStartDateTime()) > 0 && carRentalReservationRecord.getDate().compareTo(rentalRateToRemove.getEndDateTime()) < 0)
                    {
                        isUsed = true;
                        rentalRateToRemove.setIsDisabled(true);
                        throw new DeleteRentalRateException("Rental Rate has been used! Rental rate will be marked as disabled!");
                        
                    }
        }
        
        if (!isUsed) {
            em.remove(rentalRateToRemove);
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

