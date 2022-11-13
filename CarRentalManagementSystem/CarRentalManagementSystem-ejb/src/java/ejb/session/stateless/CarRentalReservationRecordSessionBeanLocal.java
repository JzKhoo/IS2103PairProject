/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarRentalReservationRecord;
import entity.Outlet;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarRentalReservationRecordNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface CarRentalReservationRecordSessionBeanLocal {

    // Retrieve
    public CarRentalReservationRecord retrieveCarRentalReservationRecordById(Long carRentalReservationRecordId) throws CarRentalReservationRecordNotFoundException;
    public List<CarRentalReservationRecord> retrieveCarRentalReservationRecordByCustomerId(Long customerId);
    
    // Search Car
    public List<Car> searchCar(Date pickUpDateTime, Date returnDateTime, Outlet pickUpOutlet, Outlet returnOutlet);

    // Cancel reservation
    public void cancelReservationById(Long carRentalReservationRecordId);
    
}
