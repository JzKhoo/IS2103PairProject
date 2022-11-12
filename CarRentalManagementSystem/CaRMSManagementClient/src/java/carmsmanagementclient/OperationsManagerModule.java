/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.ModelSessionBeanRemote;
import entity.Employee;
import entity.Model;
import entity.Car;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.ModelNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateModelException;
import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;
import util.exception.DeleteModelException;
import util.exception.ModelDisabledException;
import util.exception.OutletNotFoundException;
import util.exception.UpdateCarException;

/**
 *
 * @author khoojingzhi
 */
public class OperationsManagerModule {
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CarSessionBeanRemote carSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    
    private Employee currentEmployee;
    
    public OperationsManagerModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OperationsManagerModule(Employee currentEmployee, CarSessionBeanRemote carSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this();
        this.currentEmployee = currentEmployee;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
    }
    
    // Main Navigation Page
    public void menuOperationsManager() throws InvalidAccessRightException
    {
        if(!currentEmployee.getRole().equals("Operations Manager"))
        {
            throw new InvalidAccessRightException("You don't have Operations Manager rights to access the operations manager module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS Management System :: Operations Manager Module ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("9: Assign Transit Driver");
            System.out.println("10: Update Transit As Completed");
            System.out.println("11: Back\n");
            response = 0;
            
            while(response < 1 || response > 10)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doCreateNewModel();
                    }
                    catch(CarCategoryNotFoundException ex)
                    {
                        System.out.println(ex.getMessage() + "\n");
                    } 
                }
                else if(response == 2)
                {
                    doViewAllModels();
                }
                else if(response == 3)
                {
                    doUpdateModel();
                }
                else if(response == 4)
                {
                    doDeleteModel();
                }
                else if(response == 5)
                {
                    try
                    {
                        doCreateNewCar();
                    }
                    catch( OutletNotFoundException | ModelNotFoundException ex)
                    {
                        System.out.println(ex.getMessage() + "\n");
                    }
                }
                else if(response == 6)
                {
                    doViewAllCars();
                }
                else if(response == 7)
                {
                    doViewCarDetails();
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
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 11)
            {
                break;
            }
        }
    }
    
    // Create New Model
    private void doCreateNewModel() throws CarCategoryNotFoundException
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
            newModel.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByName(scanner.nextLine().trim()));
        }
        catch(CarCategoryNotFoundException ex)
        {
            throw new CarCategoryNotFoundException("Create new model unsuccessful!: " + ex.getMessage() + "\n");
        }
        
        Set<ConstraintViolation<Model>>constraintViolations = validator.validate(newModel);
        
        if(constraintViolations.isEmpty())
        {        
            try
            {
                newModel = modelSessionBeanRemote.createNewModel(newModel);

                System.out.println("New model created successfully!: Model ID = " + newModel.getModelId()+ "\n");
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
    
    
    // View All Models
    private void doViewAllModels()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: View All Models ***\n");
        
        List<Model> models = modelSessionBeanRemote.retrieveAllModels();
        System.out.printf("%20s%20s%20s%20s%20s\n", "Car Category", "Make", "Model", "Model Id", "isDisabled");

        for(Model model:models)
        {
            System.out.printf("%20s%20s%20s%20s%20s\n", model.getCarCategory().getName(), model.getMake(), model.getModel(), model.getModelId(), model.isIsDisabled());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    // Update Model
    private void doUpdateModel()
    {
        Scanner scanner = new Scanner(System.in); 
        String input;
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: Update Model ***\n");
        
        // Retrieve model object by Make & Model
        System.out.print("Enter Make of Model to Update> ");
        String make = scanner.nextLine().trim();
        System.out.print("Enter Model of Model to Update> ");
        String modelName = scanner.nextLine().trim();
        
        try
        {
            Model model = modelSessionBeanRemote.retrieveModelByMakeAndModel(make, modelName);
            
            System.out.printf("Updating (Make: %s) (Model: %s) \n", model.getMake(), model.getModel());

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
        catch(ModelNotFoundException ex)
        {
            System.out.println("An error has occurred while updating model: " + ex.getMessage() + "\n");
        }       
    }
    
    
// Updating of attributes
//            System.out.print("Enter CarCategory (blank if no change)> ");
//            input = scanner.nextLine().trim();
//            if(input.length() > 0)
//            {
//                try
//                {
//                    CarCategory carCategory = carCategorySessionBeanRemote.retrieveCarCategoryByName(input);
//                    model.setCarCategory(carCategory);
//                }
//                catch(CarCategoryNotFoundException ex)
//                {
//                    throw new CarCategoryNotFoundException(ex.getMessage());
//                }
//            }
//            
//            System.out.print("Enter number of Cars to add to model (0 if no change)> ");
//            int numCars = scanner.nextInt();
//            
//            List<Car> cars = new ArrayList<>();
//            for (int i = 0; i < numCars; i++)
//            {
//                System.out.print("Enter Car license plate number> ");
//                input = scanner.nextLine().trim();
//                if(input.length() > 0)
//                {
//                    try
//                    {
//                        Car car = carSessionBeanRemote.retrieveCarByLicensePlateNumber(modelName);
//                        cars.add(car);
//                    }
//                    catch(CarNotFoundException ex)
//                    {
//                        throw new CarNotFoundException(ex.getMessage());
//                    }
//                }
//            }
//            
//            model.setCars(cars);
    
    
    // Delete model
    private void doDeleteModel()
    {
        Scanner scanner = new Scanner(System.in); 
        String input;
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: Delete Model ***\n");
        
        // Retrieve model object by Make & Model
        System.out.print("Enter Make of Model to Delete> ");
        String make = scanner.nextLine().trim();
        System.out.print("Enter Model of Model to Delete> ");
        String modelName = scanner.nextLine().trim();
        
        try
        {
            Model model = modelSessionBeanRemote.retrieveModelByMakeAndModel(make, modelName);
            
            System.out.printf("Confirm Delete (Make: %s) (Model: %s) (Enter 'Y' to Delete)> ", model.getMake(), model.getModel());
            input = scanner.nextLine().trim();
            
            if(input.equals("Y"))
            {
                modelSessionBeanRemote.deleteModel(model.getModelId());
                System.out.println("Model deleted successfully!\n");
            }
            else
            {
                System.out.println("Model NOT deleted!\n");
            }
        }
        catch(ModelNotFoundException | DeleteModelException ex)
        {
            System.out.println("An error has occurred while deleting model: " + ex.getMessage() + "\n");
        }
    }
    
    
    // Create New Car
    private void doCreateNewCar() throws OutletNotFoundException, ModelNotFoundException
    {
        Scanner scanner = new Scanner(System.in);
        Car newCar = new Car();
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: Create New Car ***\n");
        System.out.print("Enter License Plate Number> ");
        newCar.setLicensePlateNumber(scanner.nextLine().trim());
        System.out.print("Enter Status> ");
        newCar.setStatus(scanner.nextLine().trim());
        System.out.print("Enter Location> ");
        newCar.setLocation(scanner.nextLine().trim());
        
        try
        {
            System.out.print("Enter Outlet Name> ");
            newCar.setOutlet(outletSessionBeanRemote.retrieveOutletByName(scanner.nextLine().trim()));
        }
        catch(OutletNotFoundException ex)
        {
            throw new OutletNotFoundException("Create new car unsuccessful!: " + ex.getMessage() + "\n");
        }
        
        try 
        {
            System.out.print("Enter Make of Car> ");
            String make = scanner.nextLine().trim();
            System.out.print("Enter Model of Car> ");
            String modelName = scanner.nextLine().trim();
            
            newCar.setModel(modelSessionBeanRemote.retrieveModelByMakeAndModel(make, modelName));
        }
        catch(ModelNotFoundException ex)
        {
            throw new ModelNotFoundException("Create new car unsuccessful!: " + ex.getMessage() + "\n");
        }
        
        
        Set<ConstraintViolation<Car>>constraintViolations = validator.validate(newCar);
        
        if(constraintViolations.isEmpty())
        {        
            try
            {
                newCar = carSessionBeanRemote.createNewCar(newCar);

                System.out.println("New car created successfully!: Car ID = " + newCar.getCarId()+ "\n");
            }
            catch(ModelDisabledException ex)
            {
                System.out.println(ex.getMessage());
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new car!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForCar(constraintViolations);
        }
    }
    
    
    // View All Cars
    private void doViewAllCars()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: View All Cars ***\n");
        
        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        System.out.printf("%20s%20s%20s%30s%20s%20s%20s%20s\n", "Car Category", "Make", "Model", "License Plate Number", "Status", "Location", "Is Disabled", "Outlet");

        for(Car car:cars)
        {
            System.out.printf("%20s%20s%20s%30s%20s%20s%20s%20s\n", car.getModel().getCarCategory().getName(), car.getModel().getMake(), car.getModel().getModel(), car.getLicensePlateNumber(), car.getStatus(), car.getLocation(), car.isIsDisabled(), car.getOutlet().getName());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    // View Car Details
    private void doViewCarDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: View Car Details ***\n");
        System.out.print("Enter License Plate Number> ");
        String licensePlateNumber = scanner.nextLine().trim();
        
        try
        {
            Car car = carSessionBeanRemote.retrieveCarByLicensePlateNumber(licensePlateNumber);
            System.out.printf("%20s%20s%20s%30s%20s%20s%20s%20s\n", "Car Category", "Make", "Model", "License Plate Number", "Status", "Location", "Is Disabled", "Outlet");
            System.out.printf("%20s%20s%20s%30s%20s%20s%20s%20s\n", car.getModel().getCarCategory().getName(), car.getModel().getMake(), car.getModel().getModel(), car.getLicensePlateNumber(), car.getStatus(), car.getLocation(), car.isIsDisabled(), car.getOutlet().getName());
            System.out.println("------------------------");
            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");
            
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateCar(car);
            }
            else if(response == 2)
            {
                doDeleteCar(car);
            }
        } 
        catch (CarNotFoundException ex) {
            System.out.println("An error has occurred while retrieving car: " + ex.getMessage() + "\n");
        }
    }
    
    
    // Update Car
    private void doUpdateCar(Car car)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: View Car Details :: Update Car ***\n");
        System.out.print("Enter License Plate Number (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            car.setLicensePlateNumber(input);
        }

        System.out.print("Enter Status (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            car.setStatus(input);
        }

        System.out.print("Enter Location (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            car.setLocation(input);
        }

        
        Set<ConstraintViolation<Car>>constraintViolations = validator.validate(car);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                carSessionBeanRemote.updateCar(car);
                System.out.println("Car updated successfully!\n");
            }
            catch (CarNotFoundException | UpdateCarException ex) 
            {
                System.out.println("An error has occurred while updating car: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForCar(constraintViolations);
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
    
    
    // Delete Car
    private void doDeleteCar(Car car)
    {
        Scanner scanner = new Scanner(System.in); 
        String input;
        
        System.out.println("*** CaRMS Management System :: Operations Manager Module :: View Car Details :: Delete Car ***\n");
        System.out.printf("Confirm Delete Car %s (Enter 'Y' to Delete)> ", car.getLicensePlateNumber());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try
            {
                carSessionBeanRemote.deleteCar(car.getCarId());
                System.out.println("Car deleted successfully!\n");
            }
            catch (CarNotFoundException | DeleteCarException ex) 
            {
                System.out.println("An error has occurred while deleting car: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Car NOT deleted!\n");
        }

    }
    
    
    
    private void showInputDataValidationErrorsForCar(Set<ConstraintViolation<Car>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
