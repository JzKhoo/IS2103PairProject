/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.sql.Timestamp;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zychi
 */
@Remote
public interface EjbTimerSessionBeanRemote {

    public void allocateCarsToCurrentDayReservations(Timestamp todayDate);

    public void generateTransitDriverDispatchRecordsForCurrentDayReservations(Timestamp todayDate) throws InputDataValidationException, UnknownPersistenceException;
    
}
