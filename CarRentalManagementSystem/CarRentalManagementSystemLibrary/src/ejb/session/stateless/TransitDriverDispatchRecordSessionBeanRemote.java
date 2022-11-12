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
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.TransitDriverDispatchRecordNotFoundException;
import util.exception.UpdateTransitDriverDispatchRecordException;

/**
 *
 * @author khoojingzhi
 */
@Remote
public interface TransitDriverDispatchRecordSessionBeanRemote {
    
    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordsForCurrentDay(Date date, Outlet outlet) throws TransitDriverDispatchRecordNotFoundException;
    
    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException;

    public void assignTransitDriver(TransitDriverDispatchRecord transitDriverDispatchRecord) throws TransitDriverDispatchRecordNotFoundException, UpdateTransitDriverDispatchRecordException, InputDataValidationException;
}
