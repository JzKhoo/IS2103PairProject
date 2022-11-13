/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBean;
import entity.Car;
import entity.CarCategory;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import java.sql.Time;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;
import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Partner;
import entity.RentalRate;
import java.sql.Timestamp;
import util.exception.ModelDisabledException;

/**
 *
 * @author khoojingzhi
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {
    
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @EJB
    private CarCategorySessionBeanLocal categorySessionBeanLocal;
    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;
    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;


    public DataInitializationSessionBean() {
    }
    
    @PostConstruct
    public void postConstruct() 
    {
        try
        {
            outletSessionBeanLocal.retrieveOutletById(1l);
        }
        catch(OutletNotFoundException ex)
        {
            initializeData();
        }
        
    }
    
    
    private void initializeData()  
    {
        try
        {
        outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet A", null, null));
        outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet B", null, null));
        outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet C", Time.valueOf("08:00:00"), Time.valueOf("22:00:00")));
        
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A1", "Password", "Sales Manager", outletSessionBeanLocal.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A2", "Password", "Operations Manager", outletSessionBeanLocal.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A3", "Password", "Customer Services Executive", outletSessionBeanLocal.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A4", "Password", "Employee", outletSessionBeanLocal.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A5", "Password", "Employee", outletSessionBeanLocal.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B1", "Password", "Sales Manager", outletSessionBeanLocal.retrieveOutletByName("Outlet B")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B2", "Password", "Operations Manager", outletSessionBeanLocal.retrieveOutletByName("Outlet B")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B3", "Password", "Customer Services Executive", outletSessionBeanLocal.retrieveOutletByName("Outlet B")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C1", "Password", "Sales Manager", outletSessionBeanLocal.retrieveOutletByName("Outlet C")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C2", "Password", "Operations Manager", outletSessionBeanLocal.retrieveOutletByName("Outlet C")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C3", "Password", "Customer Services Executive", outletSessionBeanLocal.retrieveOutletByName("Outlet C")));
        
        categorySessionBeanLocal.createNewCategory(new CarCategory("Standard Sedan"));
        categorySessionBeanLocal.createNewCategory(new CarCategory("Family Sedan"));
        categorySessionBeanLocal.createNewCategory(new CarCategory("Luxury Sedan"));
        categorySessionBeanLocal.createNewCategory(new CarCategory("SUV and Minivan"));
        
        modelSessionBeanLocal.createNewModel(new Model("Toyota", "Corolla", categorySessionBeanLocal.retrieveCarCategoryByName("Standard Sedan")));
        modelSessionBeanLocal.createNewModel(new Model("Honda", "Civic", categorySessionBeanLocal.retrieveCarCategoryByName("Standard Sedan")));
        modelSessionBeanLocal.createNewModel(new Model("Nissan", "Sunny", categorySessionBeanLocal.retrieveCarCategoryByName("Standard Sedan")));
        modelSessionBeanLocal.createNewModel(new Model("Mercedes", "E Class", categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan")));
        modelSessionBeanLocal.createNewModel(new Model("BMW", "5 Series", categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan")));
        modelSessionBeanLocal.createNewModel(new Model("Audi", "A6", categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan")));
        
        carSessionBeanLocal.createNewCar(new Car("SS00A1TC", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet A"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Toyota", "Corolla")));
        carSessionBeanLocal.createNewCar(new Car("SS00A2TC", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet A"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Toyota", "Corolla")));
        carSessionBeanLocal.createNewCar(new Car("SS00A3TC", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet A"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Toyota", "Corolla")));
        carSessionBeanLocal.createNewCar(new Car("SS00B1HC", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet B"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Honda", "Civic")));
        carSessionBeanLocal.createNewCar(new Car("SS00B2HC", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet B"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Honda", "Civic")));
        carSessionBeanLocal.createNewCar(new Car("SS00B3HC", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet B"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Honda", "Civic")));
        carSessionBeanLocal.createNewCar(new Car("SS00C1NS", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet C"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Nissan", "Sunny")));
        carSessionBeanLocal.createNewCar(new Car("SS00C2NS", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet C"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Nissan", "Sunny")));
        carSessionBeanLocal.createNewCar(new Car("SS00C3NS", "Repair", outletSessionBeanLocal.retrieveOutletByName("Outlet C"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Nissan", "Sunny")));
        carSessionBeanLocal.createNewCar(new Car("LS00A4ME", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet A"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Mercedes", "E Class")));
        carSessionBeanLocal.createNewCar(new Car("LS00B4B5", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet B"), modelSessionBeanLocal.retrieveModelByMakeAndModel("BMW", "5 Series")));
        carSessionBeanLocal.createNewCar(new Car("LS00C4A6", "Available", outletSessionBeanLocal.retrieveOutletByName("Outlet C"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Audi", "A6")));
        
        rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Standard Sedan - Default", "Default", categorySessionBeanLocal.retrieveCarCategoryByName("Standard Sedan"), 100, null, null));
        rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Standard Sedan - Weekend Promo", "Promotion", categorySessionBeanLocal.retrieveCarCategoryByName("Standard Sedan"), 80, Timestamp.valueOf("2022-12-09 12:00:00"), Timestamp.valueOf("2022-12-11 00:00:00")));
        rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Family Sedan - Default", "Default", categorySessionBeanLocal.retrieveCarCategoryByName("Family Sedan"), 200, null, null));
        rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Luxury Sedan - Default", "Default", categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan"), 300, null, null));
        rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Luxury Sedan - Monday", "Peak", categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan"), 310, Timestamp.valueOf("2022-12-05 00:00:00"), Timestamp.valueOf("2022-12-05 23:59:59")));
        rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Luxury Sedan - Tuesday", "Peak", categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan"), 320, Timestamp.valueOf("2022-12-06 00:00:00"), Timestamp.valueOf("2022-12-06 23:59:59")));
        rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Luxury Sedan - Wednesday", "Peak", categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan"), 330, Timestamp.valueOf("2022-12-07 00:00:00"), Timestamp.valueOf("2022-12-07 23:59:59")));
        rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("Luxury Sedan - Weekday Promo", "Promotion", categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan"), 250, Timestamp.valueOf("2022-12-07 12:00:00"), Timestamp.valueOf("2022-12-08 12:00:00")));
        rentalRateSessionBeanLocal.createNewRentalRate(new RentalRate("SUV and Minivan - Default", "Default", categorySessionBeanLocal.retrieveCarCategoryByName("SUV and Minivan"), 400, null, null));
        
        partnerSessionBeanLocal.createNewPartner(new Partner("Holiday.com", "Password"));
        }
        catch(OutletNotFoundException | CarCategoryNotFoundException | ModelNotFoundException | UnknownPersistenceException | InputDataValidationException | ModelDisabledException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
}