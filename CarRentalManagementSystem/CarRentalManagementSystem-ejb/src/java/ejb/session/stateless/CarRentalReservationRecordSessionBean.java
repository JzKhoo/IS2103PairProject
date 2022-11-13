/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalReservationRecord;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zychi
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
    
    // Create New Car Rental Reservation 
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
    
    @Override
    public List<CarRentalReservationRecord> retrieveAllCarRentalReservationRecords()
    {
        Query query = em.createQuery("SELECT crrr FROM CarRentalReservationRecord crrr");
        return query.getResultList();
    }
    
    @Override
    public List<CarRentalReservationRecord> retrieveCurrentDayReservationRecords(Timestamp startToday)
    {
        Query query = em.createQuery("SELECT crrr FROM CarRentalReservationRecord crrr WHERE crrr.pickupTime > :startToday AND crrr.pickupTime < :endToday ORDER BY crrr.pickupLocation");
        Timestamp endToday = addSecondsToDate(60*60*24, startToday);
        query.setParameter("startToday", startToday);
        query.setParameter("endToday", endToday);
        
        return query.getResultList();
    }
    
    private Timestamp addSecondsToDate(int numOfSeconds, Timestamp date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, numOfSeconds);
        return new Timestamp(cal.getTime().getTime());
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
}
