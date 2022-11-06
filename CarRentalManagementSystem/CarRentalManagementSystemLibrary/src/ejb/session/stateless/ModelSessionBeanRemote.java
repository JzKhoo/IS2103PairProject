/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author khoojingzhi
 */
@Remote
public interface ModelSessionBeanRemote {
    
    public Model createNewModel(Model newModel) throws UnknownPersistenceException, InputDataValidationException;
    
    public List<Model> retrieveAllModels();
}
