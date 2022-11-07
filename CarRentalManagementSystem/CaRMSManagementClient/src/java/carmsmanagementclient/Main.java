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

/**
 *
 * @author khoojingzhi
 */
public class Main {

    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(carCategorySessionBeanRemote, employeeSessionBeanRemote, modelSessionBeanRemote);
        mainApp.runApp();
    }
    
}
