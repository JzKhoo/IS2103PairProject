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
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Partner;
import entity.RentalRate;
import java.sql.Timestamp;

/**
 *
 * @author khoojingzhi
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private OutletSessionBean outletSessionBean;
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @EJB
    private CarCategorySessionBeanLocal categorySessionBeanLocal;
    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;
    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;
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
            outletSessionBean.retrieveOutletById(1l);
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
        outletSessionBean.createNewOutlet(new Outlet("Outlet A", null, null));
        outletSessionBean.createNewOutlet(new Outlet("Outlet B", null, null));
        outletSessionBean.createNewOutlet(new Outlet("Outlet C", Time.valueOf("10:00:00"), Time.valueOf("22:00:00")));
        
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A1", "Password", "Sales Manager", outletSessionBean.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A2", "Password", "Operations Manager", outletSessionBean.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A3", "Password", "Customer Services Executive", outletSessionBean.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A4", "Password", "Employee", outletSessionBean.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A5", "Password", "Employee", outletSessionBean.retrieveOutletByName("Outlet A")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B1", "Password", "Sales Manager", outletSessionBean.retrieveOutletByName("Outlet B")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B2", "Password", "Operations Manager", outletSessionBean.retrieveOutletByName("Outlet B")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B3", "Password", "Customer Services Executive", outletSessionBean.retrieveOutletByName("Outlet B")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C1", "Password", "Sales Manager", outletSessionBean.retrieveOutletByName("Outlet C")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C2", "Password", "Operations Manager", outletSessionBean.retrieveOutletByName("Outlet C")));
        employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C3", "Password", "Customer Services Executive", outletSessionBean.retrieveOutletByName("Outlet C")));
        
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
        
        carSessionBeanLocal.createNewCar(new Car("SS00A1TC", "Available", outletSessionBean.retrieveOutletByName("Outlet A"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Toyota", "Corolla")));
        carSessionBeanLocal.createNewCar(new Car("SS00A2TC", "Available", outletSessionBean.retrieveOutletByName("Outlet A"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Toyota", "Corolla")));
        carSessionBeanLocal.createNewCar(new Car("SS00A3TC", "Available", outletSessionBean.retrieveOutletByName("Outlet A"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Toyota", "Corolla")));
        carSessionBeanLocal.createNewCar(new Car("SS00B1HC", "Available", outletSessionBean.retrieveOutletByName("Outlet B"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Honda", "Civic")));
        carSessionBeanLocal.createNewCar(new Car("SS00B2HC", "Available", outletSessionBean.retrieveOutletByName("Outlet B"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Honda", "Civic")));
        carSessionBeanLocal.createNewCar(new Car("SS00B3HC", "Available", outletSessionBean.retrieveOutletByName("Outlet B"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Honda", "Civic")));
        carSessionBeanLocal.createNewCar(new Car("SS00C1NS", "Available", outletSessionBean.retrieveOutletByName("Outlet C"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Nissan", "Sunny")));
        carSessionBeanLocal.createNewCar(new Car("SS00C2NS", "Available", outletSessionBean.retrieveOutletByName("Outlet C"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Nissan", "Sunny")));
        carSessionBeanLocal.createNewCar(new Car("SS00C3NS", "Repair", outletSessionBean.retrieveOutletByName("Outlet C"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Nissan", "Sunny")));
        carSessionBeanLocal.createNewCar(new Car("LS00A4ME", "Available", outletSessionBean.retrieveOutletByName("Outlet A"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Mercedes", "E Class")));
        carSessionBeanLocal.createNewCar(new Car("LS00B4B5", "Available", outletSessionBean.retrieveOutletByName("Outlet B"), modelSessionBeanLocal.retrieveModelByMakeAndModel("BMW", "5 Series")));
        carSessionBeanLocal.createNewCar(new Car("LS00C4A6", "Available", outletSessionBean.retrieveOutletByName("Outlet C"), modelSessionBeanLocal.retrieveModelByMakeAndModel("Audi", "A6")));
        
        rentalRateSessionBeanLocal.createNewRentalrate(new RentalRate("Default", 100, null, null, categorySessionBeanLocal.retrieveCarCategoryByName("Standard Sedan")));
        rentalRateSessionBeanLocal.createNewRentalrate(new RentalRate("Promotion", 80, Timestamp.valueOf("2022-12-09 12:00:00"), Timestamp.valueOf("2022-12-11 00:00:00"), categorySessionBeanLocal.retrieveCarCategoryByName("Standard Sedan")));
        rentalRateSessionBeanLocal.createNewRentalrate(new RentalRate("Default", 200, null, null, categorySessionBeanLocal.retrieveCarCategoryByName("Family Sedan")));
        rentalRateSessionBeanLocal.createNewRentalrate(new RentalRate("Default", 300, null, null, categorySessionBeanLocal.retrieveCarCategoryByName("Family Sedan")));
        rentalRateSessionBeanLocal.createNewRentalrate(new RentalRate("Peak", 310, Timestamp.valueOf("2022-12-05 00:00:00"), Timestamp.valueOf("2022-12-05 23:59:59"), categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan")));
        rentalRateSessionBeanLocal.createNewRentalrate(new RentalRate("Peak", 320, Timestamp.valueOf("2022-12-06 00:00:00"), Timestamp.valueOf("2022-12-06 23:59:59"), categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan")));
        rentalRateSessionBeanLocal.createNewRentalrate(new RentalRate("Peak", 330, Timestamp.valueOf("2022-12-07 00:00:00"), Timestamp.valueOf("2022-12-07 23:59:59"), categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan")));
        rentalRateSessionBeanLocal.createNewRentalrate(new RentalRate("Promotion", 250, Timestamp.valueOf("2022-12-07 12:00:00"), Timestamp.valueOf("2022-12-08 12:00:00"), categorySessionBeanLocal.retrieveCarCategoryByName("Luxury Sedan")));
        rentalRateSessionBeanLocal.createNewRentalrate(new RentalRate("Default", 400, null, null, categorySessionBeanLocal.retrieveCarCategoryByName("SUV and Minivan")));
        
        partnerSessionBeanLocal.createNewPartner(new Partner("Holiday.com", "Password"));
        }
        catch(OutletNotFoundException | CarCategoryNotFoundException | ModelNotFoundException | UnknownPersistenceException | InputDataValidationException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
}
