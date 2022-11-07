/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
@Remote
public interface RentalRateSessionBeanRemote {
    
    // Create
    public RentalRate createNewRentalrate(RentalRate newRentalRate) throws UnknownPersistenceException, InputDataValidationException;
}
