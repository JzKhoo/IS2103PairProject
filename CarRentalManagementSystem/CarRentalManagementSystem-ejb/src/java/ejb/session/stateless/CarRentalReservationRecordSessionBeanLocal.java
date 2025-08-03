/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarRentalReservationRecord;
import entity.Outlet;
import entity.RentalRate;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarRentalReservationRecordNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarRentalReservationRecordException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface CarRentalReservationRecordSessionBeanLocal {

    // Retrieve by ID
    public CarRentalReservationRecord retrieveCarRentalReservationRecordById(Long carRentalReservationRecordId) throws CarRentalReservationRecordNotFoundException;
    
    // Update reservation
    public void updateCarRentalReservationRecord(CarRentalReservationRecord carRentalReservationRecord) throws CarRentalReservationRecordNotFoundException, UpdateCarRentalReservationRecordException, InputDataValidationException;
    
    // Cancel reservation
    public void cancelReservationById(Long carRentalReservationRecordId);
    
    // Retrieve by Customer ID
    public List<CarRentalReservationRecord> retrieveCarRentalReservationRecordByCustomerId(Long customerId);
    
    // Search Car
    public List<Car> searchCar(Date pickupDate, Date returnDate, Outlet pickupOutlet, Outlet returnOutlet);   

    // Calculate total rental rate
    public List<RentalRate> calculateTotalRentalRate(CarCategory carCategory, Date startDate, Date endDate) throws RentalRateNotFoundException;

    public CarRentalReservationRecord createNewCarRentalReservationRecord(CarRentalReservationRecord newCarRentalReservationRecord) throws UnknownPersistenceException, InputDataValidationException;
}
