/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarRentalReservationRecordSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author khoojingzhi
 */
public class Main {

    @EJB(name = "CarCategorySessionBeanRemote")
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    @EJB
    private static CarRentalReservationRecordSessionBeanRemote carRentalReservationRecordSessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    
    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(carCategorySessionBeanRemote, carRentalReservationRecordSessionBeanRemote, customerSessionBeanRemote, outletSessionBeanRemote);
        mainApp.runApp();
    }
    
}
