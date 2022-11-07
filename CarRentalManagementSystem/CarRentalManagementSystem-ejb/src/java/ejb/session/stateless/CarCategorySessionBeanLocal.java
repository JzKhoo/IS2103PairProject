/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface CarCategorySessionBeanLocal {

    // Create (Backend data initialization only)
    public CarCategory createNewCategory(CarCategory newCarCategory);
    
    // Retrieve
    public CarCategory retrieveCarCategoryByName(String name) throws CarCategoryNotFoundException;
}
