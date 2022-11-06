/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface ModelSessionBeanLocal {

    public Model createNewModel(Model newModel) throws UnknownPersistenceException, InputDataValidationException;
    
}
