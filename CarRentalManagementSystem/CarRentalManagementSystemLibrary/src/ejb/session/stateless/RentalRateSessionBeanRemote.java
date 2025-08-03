/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
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
    
    // Create New Rental Rate
    public RentalRate createNewRentalrate(RentalRate newRentalRate) throws UnknownPersistenceException, InputDataValidationException;

    // View All Rental Rates
    public List<RentalRate> retrieveAllRentalRates();
    
    // Retrieve by ID
    public RentalRate retrieveRentalRateById(Long rentalRateId) throws RentalRateNotFoundException;
    
    // Retrieve by Name
    public RentalRate retrieveRentalRateByName(String name) throws RentalRateNotFoundException;
    
    // Retrieve by Category
    public List<RentalRate> retrieveRentalRateByCarCategory(CarCategory carCategory);

    // Update Rental Rate
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException;
    
    // Delete Rental Rate
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException, DeleteRentalRateException;
    
}
