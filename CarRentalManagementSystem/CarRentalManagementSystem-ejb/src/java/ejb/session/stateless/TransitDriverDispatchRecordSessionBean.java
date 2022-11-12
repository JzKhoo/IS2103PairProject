/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
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
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public TransitDriverDispatchRecordSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long TransitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException 
    {
        TransitDriverDispatchRecord transitDriverDispatchRecord = em.find(TransitDriverDispatchRecord.class, TransitDriverDispatchRecordId);
        
        if(transitDriverDispatchRecord != null) 
        {
            return transitDriverDispatchRecord;
        }
        else 
        {
            throw new TransitDriverDispatchRecordNotFoundException("Transit Driver Dispatch Record does not exist: " + TransitDriverDispatchRecordId);
        }
    }
    
    @Override
    public Long createNewTransitDriverDispatchRecord(TransitDriverDispatchRecord newTransitDriverDispatchRecord) throws UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<TransitDriverDispatchRecord>>constraintViolations = validator.validate(newTransitDriverDispatchRecord);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newTransitDriverDispatchRecord);
                em.flush();

                return newTransitDriverDispatchRecord.getTransitDriverDispatchRecordId();
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
