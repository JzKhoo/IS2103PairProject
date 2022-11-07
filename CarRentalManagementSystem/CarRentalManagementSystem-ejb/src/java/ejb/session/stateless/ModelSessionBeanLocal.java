/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.ModelNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateModelException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface ModelSessionBeanLocal {

    // Create
    public Model createNewModel(Model newModel) throws UnknownPersistenceException, InputDataValidationException;

    // Retrieve
    public List<Model> retrieveAllModels();
    public Model retrieveModelByModelId(Long modelId) throws ModelNotFoundException;
    public Model retrieveModelByMakeAndModel(String make, String model) throws ModelNotFoundException;

    // Update
    public void updateModel(Model model) throws ModelNotFoundException, UpdateModelException, InputDataValidationException;    
}
