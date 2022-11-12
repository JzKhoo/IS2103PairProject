/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import entity.TransitDriverDispatchRecord;
import java.sql.Date;
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
import util.exception.InputDataValidationException;
import util.exception.TransitDriverDispatchRecordNotFoundException;
import util.exception.UpdateTransitDriverDispatchRecordException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TransitDriverDispatchRecordSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }   
    
    
    // Retrieve
    // View All TransitDriverDispatchRecords for Current Day Reservations
    @Override
    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordsForCurrentDay(Date date, Outlet outlet) throws TransitDriverDispatchRecordNotFoundException
    {
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecord t WHERE t.date = :inDate AND t.car.outlet = :inOutlet");
        query.setParameter("inDate", date).setParameter("inOutlet", outlet);
        
        if(!query.getResultList().isEmpty())
        {
            return query.getResultList();
        }
        else
        {
            throw new TransitDriverDispatchRecordNotFoundException("No transit driver dispatch records found for Date - " +  date.toString() + ", Outlet - " + outlet.getName());
        }
    }
    
    @Override
    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException
    {
        TransitDriverDispatchRecord transitDriverDispatchRecord = em.find(TransitDriverDispatchRecord.class, transitDriverDispatchRecordId);
        
        if(transitDriverDispatchRecord != null)
        {
            return transitDriverDispatchRecord;
        }
        else 
        {
            throw new TransitDriverDispatchRecordNotFoundException("Transit Driver Dispatch Record ID " + transitDriverDispatchRecordId + " does not exist!");
        }
    }
    
    
    // Update
    // Assign Transit Driver
    @Override
    public void assignTransitDriver(TransitDriverDispatchRecord transitDriverDispatchRecord) throws TransitDriverDispatchRecordNotFoundException, UpdateTransitDriverDispatchRecordException, InputDataValidationException 
    {
        if(transitDriverDispatchRecord != null && transitDriverDispatchRecord.getTransitDriverDispatchRecordId()!= null)
        {
            Set<ConstraintViolation<TransitDriverDispatchRecord>>constraintViolations = validator.validate(transitDriverDispatchRecord);
        
            if(constraintViolations.isEmpty())
            {
                TransitDriverDispatchRecord transitDriverDispatchRecordToUpdate = retrieveTransitDriverDispatchRecordById(transitDriverDispatchRecord.getTransitDriverDispatchRecordId());

                if(transitDriverDispatchRecordToUpdate.getTransitDriverDispatchRecordId().equals(transitDriverDispatchRecord.getTransitDriverDispatchRecordId()))
                {
                    transitDriverDispatchRecordToUpdate.setEmployee(transitDriverDispatchRecord.getEmployee());                    
                }
                else
                {
                    throw new UpdateTransitDriverDispatchRecordException("ID of transit driver dispatch record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new TransitDriverDispatchRecordNotFoundException("ID not provided for transit driver dispatch record to be updated");
        }
    }
    
    // Update Transit As Completed
    public void updateTransitAsCompleted(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException, UpdateTransitDriverDispatchRecordException
    {
        if(transitDriverDispatchRecordId!= null)
        {
            TransitDriverDispatchRecord transitDriverDispatchRecordToUpdate = retrieveTransitDriverDispatchRecordById(transitDriverDispatchRecordId);

            if(transitDriverDispatchRecordToUpdate.getTransitDriverDispatchRecordId().equals(transitDriverDispatchRecordId))
            {
                transitDriverDispatchRecordToUpdate.setIsCompleted(true);                    
            }
            else
            {
                throw new UpdateTransitDriverDispatchRecordException("ID of transit driver dispatch record to be updated does not match the existing record");
            }
        }
        else
        {
            throw new TransitDriverDispatchRecordNotFoundException("ID not provided for transit driver dispatch record to be updated");
        }
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<TransitDriverDispatchRecord>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
