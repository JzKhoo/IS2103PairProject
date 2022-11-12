/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalReservationRecord;
import javax.ejb.Remote;
import util.exception.CarRentalReservationRecordNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Remote
public interface CarRentalReservationRecordSessionBeanRemote {
    
    public CarRentalReservationRecord retrieveCarRentalReservationRecordById(Long carRentalReservationRecordId) throws CarRentalReservationRecordNotFoundException;
}
