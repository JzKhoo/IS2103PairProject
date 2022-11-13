/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalReservationRecord;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zychi
 */
@Local
public interface CarRentalReservationRecordSessionBeanLocal {
    public List<CarRentalReservationRecord> retrieveAllCarRentalReservationRecords();

    public List<CarRentalReservationRecord> retrieveCurrentDayReservationRecords(Timestamp todayDate);

    public CarRentalReservationRecord createNewCarRentalReservationRecord(CarRentalReservationRecord newCarRentalReservationRecord) throws UnknownPersistenceException, InputDataValidationException;
}
