/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface CategorySessionBeanLocal {

    public Category retrieveCategoryByType(String type) throws CategoryNotFoundException;

    public Category createNewCategory(Category category);
    
}
