/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalReservationRecord;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CarRentalReservationRecordNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class CarRentalReservationRecordSessionBean implements CarRentalReservationRecordSessionBeanRemote, CarRentalReservationRecordSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    // Retrieve
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
}
