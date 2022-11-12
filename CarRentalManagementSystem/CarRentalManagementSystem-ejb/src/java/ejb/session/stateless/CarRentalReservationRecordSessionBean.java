/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalReservationRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author zychi
 */
@Stateless
public class CarRentalReservationRecordSessionBean implements CarRentalReservationRecordSessionBeanRemote, CarRentalReservationRecordSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<CarRentalReservationRecord> retrieveAllCarRentalReservationRecords()
    {
        Query query = em.createQuery("SELECT crrr FROM CarRentalReservationRecord crrr");
        return query.getResultList();
    }
    
    @Override
    public List<CarRentalReservationRecord> retrieveCurrentDayReservationRecords()
    {
        Query query = em.createQuery("SELECT crrr FROM CarRentalReservationRecord crrr WHERE crrr.date = :today");
        query.setParameter("today", new Date(), TemporalType.DATE);
        
        return query.getResultList();
    }
}
