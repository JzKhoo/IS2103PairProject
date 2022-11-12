/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalReservationRecord;
import javax.ejb.Local;
import util.exception.CarRentalReservationRecordNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface CarRentalReservationRecordSessionBeanLocal {

    public CarRentalReservationRecord retrieveCarRentalReservationRecordById(Long carRentalReservationRecordId) throws CarRentalReservationRecordNotFoundException;
    
}
