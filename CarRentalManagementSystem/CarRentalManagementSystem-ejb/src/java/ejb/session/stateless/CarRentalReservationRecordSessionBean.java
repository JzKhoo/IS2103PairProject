/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalReservationRecord;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
}
