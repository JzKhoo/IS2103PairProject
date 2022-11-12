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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.TransitDriverDispatchRecordNotFoundException;

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
    
    
}
