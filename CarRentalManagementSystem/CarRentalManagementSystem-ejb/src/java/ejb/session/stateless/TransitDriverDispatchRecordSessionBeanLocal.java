/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import entity.TransitDriverDispatchRecord;
import java.sql.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.TransitDriverDispatchRecordNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface TransitDriverDispatchRecordSessionBeanLocal {

    // Retrieve Transit Driver Dispatch Records based on date and outlet
    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordsForCurrentDay(Date date, Outlet outlet) throws TransitDriverDispatchRecordNotFoundException;
    
}
