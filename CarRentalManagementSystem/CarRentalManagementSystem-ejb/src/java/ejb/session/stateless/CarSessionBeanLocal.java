/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;
import util.exception.InputDataValidationException;
import util.exception.ModelDisabledException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface CarSessionBeanLocal {

    // Create New Car
    public Car createNewCar(Car newCar) throws UnknownPersistenceException, InputDataValidationException, ModelDisabledException;
    
    // Retrieve
    public Car retrieveCarByLicensePlateNumber(String licensePlateNumber) throws CarNotFoundException;
    public Car retrieveCarByCarId(Long carId) throws CarNotFoundException;
    public List<Car> retrieveAllCars();

    // Update Car
    public void updateCar(Car car) throws CarNotFoundException, UpdateCarException, InputDataValidationException;

    // Delete Car
    public void deleteCar(Long carId) throws CarNotFoundException, DeleteCarException;
}
