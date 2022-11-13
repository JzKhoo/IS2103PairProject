/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalReservationRecord;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zychi
 */
@Remote
public interface CarRentalReservationRecordSessionBeanRemote {
    public List<CarRentalReservationRecord> retrieveAllCarRentalReservationRecords();
    public CarRentalReservationRecord createNewCarRentalReservationRecord(CarRentalReservationRecord newCarRentalReservationRecord) throws UnknownPersistenceException, InputDataValidationException;
}
