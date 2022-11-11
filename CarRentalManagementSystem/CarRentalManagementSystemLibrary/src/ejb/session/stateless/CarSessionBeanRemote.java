/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import javax.ejb.Remote;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ModelDisabledException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
@Remote
public interface CarSessionBeanRemote {
    
    // Create
    public Car createNewCar(Car newCar) throws UnknownPersistenceException, InputDataValidationException, ModelDisabledException;

    // Retrieve
    public Car retrieveCarByLicensePlateNumber(String licensePlateNumber) throws CarNotFoundException;
}
