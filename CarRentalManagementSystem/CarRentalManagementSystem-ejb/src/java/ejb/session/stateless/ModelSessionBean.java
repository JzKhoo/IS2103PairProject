/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteModelException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateModelException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ModelSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }   

    
    // Create New Model
    @Override
    public Model createNewModel(Model newModel) throws UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<Model>>constraintViolations = validator.validate(newModel);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newModel);
                em.flush();

                return newModel;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    // View All Models
    @Override
    public List<Model> retrieveAllModels()
    {
        Query query = em.createQuery("SELECT m FROM Model m ORDER BY m.carCategory, m.make, m.model ASC");
        
        return query.getResultList();
    }
    
    
    // Retrieve by ID
    @Override
    public Model retrieveModelByModelId(Long modelId) throws ModelNotFoundException
    {
        Model model = em.find(Model.class, modelId);
        
        if(model != null)
        {
            model.getCars().size();
            return model;
        }
        else
        {
            throw new ModelNotFoundException("Model ID " + modelId + " does not exist!");
        }
    }
    
    
    // Retrieve by Make and Model
    @Override
    public Model retrieveModelByMakeAndModel(String make, String model) throws ModelNotFoundException
    {
        Query query = em.createQuery("SELECT m FROM Model m WHERE m.make = :inMake AND m.model = :inModel");
        query.setParameter("inMake", make).setParameter("inModel", model);
        
        try
        {
            return (Model)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new ModelNotFoundException("Make " + make + " , Model " + model + " does not exist!");
        }
    }
    
    
    // Update Model
    @Override
    public void updateModel(Model model) throws ModelNotFoundException, UpdateModelException, InputDataValidationException 
    {
        if(model != null && model.getModelId()!= null)
        {
            Set<ConstraintViolation<Model>>constraintViolations = validator.validate(model);
        
            if(constraintViolations.isEmpty())
            {
                Model modelToUpdate = retrieveModelByModelId(model.getModelId());

                if(modelToUpdate.getModelId().equals(model.getModelId()))
                {
                    modelToUpdate.setMake(model.getMake());
                    modelToUpdate.setModel(model.getModel());
                }
                else
                {
                    throw new UpdateModelException("Model ID of model record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new ModelNotFoundException("Model ID not provided for model to be updated");
        }
    }
    
    
    // Delete Model
    @Override
    public void deleteModel(Long modelId) throws ModelNotFoundException, DeleteModelException
    {
        Model modelToRemove = retrieveModelByModelId(modelId);
        
        List<Car> cars = modelToRemove.getCars();
        
        if(cars.isEmpty())
        {
            modelToRemove.getCarCategory().getModels().remove(modelToRemove);
            
            em.remove(modelToRemove);
        }
        else
        {
            modelToRemove.setIsDisabled(true);
            throw new DeleteModelException("Model ID " + modelId + " is associated with existing cars and cannot be deleted! Model has been marked as disabled.");
        }
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Model>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
