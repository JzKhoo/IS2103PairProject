/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;

/**
 *
 * @author khoojingzhi
 */
public class Main {

    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;
    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;
    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;
    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    @EJB
    private static TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(carSessionBeanRemote, carCategorySessionBeanRemote, employeeSessionBeanRemote, modelSessionBeanRemote, outletSessionBeanRemote, rentalRateSessionBeanRemote, transitDriverDispatchRecordSessionBeanRemote);
        mainApp.runApp();
    }
    
}
