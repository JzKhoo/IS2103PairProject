/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author khoojingzhi
 */
@Remote
public interface RentalRateSessionBeanRemote {
    
    // Create
    public RentalRate createNewRentalRate(RentalRate newRentalRate) throws UnknownPersistenceException, InputDataValidationException;
    public List<RentalRate> retrieveAllRentalRates();
    public RentalRate retrieveRentalRateByName(String rentalRateName) throws RentalRateNotFoundException;
    public RentalRate viewRentalRateDetails(String name) throws RentalRateNotFoundException;
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException;
    public void deleteRentalRate(String name) throws RentalRateNotFoundException, DeleteRentalRateException;
}
