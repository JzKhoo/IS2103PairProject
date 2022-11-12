/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarRentalReservationRecord;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author zychi
 */
@Local
public interface CarRentalReservationRecordSessionBeanLocal {
    public List<CarRentalReservationRecord> retrieveAllCarRentalReservationRecords();

    public List<CarRentalReservationRecord> retrieveCurrentDayReservationRecords();
}
