/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarRentalReservationRecord;
import entity.Outlet;
import entity.TransitDriverDispatchRecord;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zychi
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBeanLocal;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private CarRentalReservationRecordSessionBeanLocal carRentalReservationRecordSessionBeanLocal;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;
    
    
    @Schedule(dayOfWeek="*", hour="0", minute="0", second="0", info="generateTransitDriverDispatchRecordsForCurrentDayReservations")
    public void generateTransitDriverDispatchRecordsForCurrentDayReservations()
    {
        String timeStamp = new SimpleDateFormat("yyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.generateTransitDriverDispatchRecordsForCurrentDayReservations(): Timeout at " + timeStamp);
        
        List<CarRentalReservationRecord> carRentalReservationRecords = carRentalReservationRecordSessionBeanLocal.retrieveCurrentDayReservationRecords(new Timestamp(new Date().getTime()));
        
        
        for (CarRentalReservationRecord carRentalReservationRecord : carRentalReservationRecords)
        {
            TransitDriverDispatchRecord transitDriverDispatchRecord = new TransitDriverDispatchRecord();
            transitDriverDispatchRecord.setCar(carRentalReservationRecord.getCar());
            transitDriverDispatchRecord.setTransitStartTime(subtractSecondsToDate(60*60*2, carRentalReservationRecord.getPickupTime()));
            try{
                transitDriverDispatchRecordSessionBeanLocal.createTransitDriverDispatchRecord(transitDriverDispatchRecord);
            } catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new Transit Driver Dispatch Record!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
    }
    
        private Timestamp subtractSecondsToDate(int numOfSeconds, Timestamp date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, numOfSeconds * -1);
        return new Timestamp(cal.getTime().getTime());
    }
    
    @Schedule(dayOfWeek="*", hour="0", minute="0", second="0", info="allocateCarsToCurrentDayReservations")
    public void allocateCarsToCurrentDayReservations()
    {
//        String timeStamp = new SimpleDateFormat("yyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.allocateCarsToCurrentDayReservations(): Timeout at " + new Date());
        
        List<CarRentalReservationRecord> carRentalReservationRecords = carRentalReservationRecordSessionBeanLocal.retrieveCurrentDayReservationRecords(new Timestamp(new Date().getTime()));

        List<Car> allCars = carSessionBeanLocal.retrieveAllCars();
        for (CarRentalReservationRecord carRentalReservationRecord : carRentalReservationRecords)
        {
            Outlet outlet = null;
            try{
                outlet = outletSessionBean.retrieveOutletByName(carRentalReservationRecord.getPickupLocation());
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            
            List<Car> cars = carSessionBeanLocal.retrieveCarsAtOutlet(outlet);
            for (Car car : cars)
            {
                if (carRentalReservationRecord.getCategoryTypeChoice().equals(car.getModel().getCarCategory().getName()) ||
                        carRentalReservationRecord.getModelChoice().equals(car.getModel().getModel()))
                {
                    if (allCars.contains(car))
                    {
                        em.persist(carRentalReservationRecord);
                        carRentalReservationRecord.setCar(car);
                        allCars.remove(car);
                        em.flush();
                        break;
                    }
                }
            }
        }
    }  

    @Override
    public void generateTransitDriverDispatchRecordsForCurrentDayReservations(Timestamp todayDate) throws UnknownPersistenceException, InputDataValidationException
    {
        List<CarRentalReservationRecord> carRentalReservationRecords = carRentalReservationRecordSessionBeanLocal.retrieveCurrentDayReservationRecords(todayDate);
        
        
        for (CarRentalReservationRecord carRentalReservationRecord : carRentalReservationRecords)
        {
            TransitDriverDispatchRecord transitDriverDispatchRecord = new TransitDriverDispatchRecord();
            transitDriverDispatchRecord.setCar(carRentalReservationRecord.getCar());
            transitDriverDispatchRecord.setTransitStartTime(subtractSecondsToDate(60*60*2, carRentalReservationRecord.getPickupTime()));
            try{
                transitDriverDispatchRecordSessionBeanLocal.createTransitDriverDispatchRecord(transitDriverDispatchRecord);
            } catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new Transit Driver Dispatch Record!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
    }

        
    public void allocateCarsToCurrentDayReservations(Timestamp todayDate)
    {        
        List<CarRentalReservationRecord> carRentalReservationRecords = carRentalReservationRecordSessionBeanLocal.retrieveCurrentDayReservationRecords(todayDate);

        List<Car> allCars = carSessionBeanLocal.retrieveAllCars();
        for (CarRentalReservationRecord carRentalReservationRecord : carRentalReservationRecords)
        {
            Outlet outlet = null;
            try{
                outlet = outletSessionBean.retrieveOutletByName(carRentalReservationRecord.getPickupLocation());
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            
            List<Car> cars = carSessionBeanLocal.retrieveCarsAtOutlet(outlet);
            for (Car car : cars)
            {
                if (carRentalReservationRecord.getCategoryTypeChoice().equals(car.getModel().getCarCategory().getName()))
                {
                    if (allCars.contains(car))
                    {
                        em.persist(carRentalReservationRecord);
                        carRentalReservationRecord.setCar(car);
                        allCars.remove(car);
                        em.flush();
                        break;
                    }
                }
            }
        }
    }    
}
