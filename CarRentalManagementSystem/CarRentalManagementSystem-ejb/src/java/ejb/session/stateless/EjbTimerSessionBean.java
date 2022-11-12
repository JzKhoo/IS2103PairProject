/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarRentalReservationRecord;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author zychi
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private CarRentalReservationRecordSessionBeanLocal carRentalReservationRecordSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @EJB
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBeanLocal;
    
    
    
    @Schedule(hour="*", minute="*", second="*/5", info="generateTransitDriverDispatchRecordsForCurrentDayReservations")
    public void generateTransitDriverDispatchRecordsForCurrentDayReservations()
    {
        String timeStamp = new SimpleDateFormat("yyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.generateTransitDriverDispatchRecordsForCurrentDayReservations(): Timeout at " + timeStamp);
        
        
        
        
    }
    
    @Schedule(hour="*", minute="*", second="*/5", info="allocateCarsToCurrentDayReservations")
    public void allocateCarsToCurrentDayReservations()
    {
        String timeStamp = new SimpleDateFormat("yyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.generateTransitDriverDispatchRecordsForCurrentDayReservations(): Timeout at " + timeStamp);
        
        List<CarRentalReservationRecord> carRentalReservationRecords = carRentalReservationRecordSessionBeanLocal.retrieveCurrentDayReservationRecords();
        
        
        List<Car> cars = carSessionBeanLocal.retrieveAllCars();
        for (CarRentalReservationRecord carRentalReservationRecord : carRentalReservationRecords)
        {
            for (Car car : cars)
            {
                if (carRentalReservationRecord.getCategoryTypeChoice().equals(car.getModel().getCarCategory()) && 
                        carRentalReservationRecord.getMakeChoice().equals(car.getModel().getMake()) && 
                        carRentalReservationRecord.getModelChoice().equals(car.getModel()))
            }
        }
        
    }

    
}
