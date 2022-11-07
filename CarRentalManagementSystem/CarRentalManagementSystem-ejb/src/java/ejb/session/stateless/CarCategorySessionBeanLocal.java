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

    // Create
    public CarCategory createNewCategory(CarCategory category);
    
    // Retrieve
    public CarCategory retrieveCategoryByName(String type) throws CarCategoryNotFoundException;
}
