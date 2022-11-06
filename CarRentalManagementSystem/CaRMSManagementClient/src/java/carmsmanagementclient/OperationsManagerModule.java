/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import entity.Employee;
import entity.Model;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.Role;
import util.exception.CategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.ModelNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateModelException;

/**
 *
 * @author khoojingzhi
 */
public class OperationsManagerModule {
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    
    private Employee currentEmployee;

    public OperationsManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OperationsManagerModule(Employee currentEmployee, CategorySessionBeanRemote categorySessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote) {
        this();
        this.currentEmployee = currentEmployee;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
    }
    
    
    
    public void menuOperationsManager() throws InvalidAccessRightException
    {
        if(currentEmployee.getUserRole() != Role.OPERATIONS_MANAGER && currentEmployee.getUserRole() != Role.SYSTEM_ADMINISTRATOR)
        {
            throw new InvalidAccessRightException("You don't have Operations Manager rights to access the operations manager module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management System :: Operations Manager ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: Update Car");
            System.out.println("9: Delete Car");
            System.out.println("10: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("11: Assign Transit Driver");
            System.out.println("12: Update Transit As Completed");
            System.out.println("13: Back\n");
            response = 0;
            
            while(response < 1 || response > 12)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doCreateNewModel();
                    }
                    catch(CategoryNotFoundException ex)
                    {
                        System.out.println("Create new model unsuccessful!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2)
                {
                    doViewAllModels();
                }
                else if(response == 3)
                {
                    try
                    {
                        doUpdateModel();
                    }
                    catch(ModelNotFoundException ex)
                    {
                        System.out.println(ex.getMessage() + "\n");
                    }
                }
                else if(response == 4)
                {
                    break;
                }
                else if(response == 5)
                {
                    break;
                }
                else if(response == 6)
                {
                    break;
                }
                else if(response == 7)
                {
                    break;
                }
                else if(response == 8)
                {
                    break;
                }
                else if(response == 9)
                {
                    break;
                }
                else if(response == 10)
                {
                    break;
                }
                else if(response == 11)
                {
                    break;
                }
                else if(response == 12)
                {
                    break;
                }
                else if(response == 13)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 13)
            {
                break;
            }
        }
    }
    
    
    private void doCreateNewModel() throws CategoryNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        Model newModel = new Model();
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: Create New Model ***\n");
        System.out.print("Enter Make> ");
        newModel.setMake(scanner.nextLine().trim());
        System.out.print("Enter Model> ");
        newModel.setModel(scanner.nextLine().trim());
        
        try
        {
            System.out.print("Enter Category> ");
            newModel.setCategory(categorySessionBeanRemote.retrieveCategoryByType(scanner.nextLine().trim()));
        }
        catch(CategoryNotFoundException ex)
        {
            throw new CategoryNotFoundException(ex.getMessage());
        }
        
        Set<ConstraintViolation<Model>>constraintViolations = validator.validate(newModel);
        
        if(constraintViolations.isEmpty())
        {        
            try
            {
                newModel = modelSessionBeanRemote.createNewModel(newModel);

                System.out.println("New model created successfully!: " + newModel.getModelId()+ "\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new model!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForModel(constraintViolations);
        }
    }
    
    
    private void doViewAllModels()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: View All Models ***\n");
        
        List<Model> models = modelSessionBeanRemote.retrieveAllModels();
        System.out.printf("%20s%20s%20s%20s%20s\n", "Car Category", "Make", "Model", "Model Id", "isDisabled");

        for(Model model:models)
        {
            System.out.printf("%20s%20s%20s%20s%20s\n", model.getCategory().getType(), model.getMake(), model.getModel(), model.getModelId(), model.isIsDisabled());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    private void doUpdateModel() throws ModelNotFoundException
    {
        Scanner scanner = new Scanner(System.in); 
        String input;
        Model model = null;
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: Update Model ***\n");
        
        // Retrieve model object by ID
        System.out.print("Enter Id of Model to Update> ");
        Long modelId = new Long(scanner.nextInt());
        
        scanner.nextLine();
        
        try
        {
            model = modelSessionBeanRemote.retrieveModelByModelId(modelId);
        }
        catch(ModelNotFoundException ex)
        {
            throw new ModelNotFoundException("An error has occurred while retrieving model: " + ex.getMessage() + "\n");
        }
        
        System.out.print("Enter Make (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            model.setMake(input);
        }
        
        System.out.print("Enter Model (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            model.setModel(input);
        }
        
        Set<ConstraintViolation<Model>>constraintViolations = validator.validate(model);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                modelSessionBeanRemote.updateModel(model);
                System.out.println("Model updated successfully!\n");
            }
            catch (ModelNotFoundException | UpdateModelException ex) 
            {
                System.out.println("An error has occurred while updating model: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForModel(constraintViolations);
        }
    }
    
    
    private void showInputDataValidationErrorsForModel(Set<ConstraintViolation<Model>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
