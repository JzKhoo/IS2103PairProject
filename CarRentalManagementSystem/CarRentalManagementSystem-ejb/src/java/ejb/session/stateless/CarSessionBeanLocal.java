/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface CarSessionBeanLocal {

    // Create
    public Car createNewCar(Car newCar) throws UnknownPersistenceException, InputDataValidationException;
    
}
