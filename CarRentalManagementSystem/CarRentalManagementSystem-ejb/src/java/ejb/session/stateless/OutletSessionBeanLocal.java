/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Local;
import util.exception.OutletNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface OutletSessionBeanLocal {

    // Create (Backend data initialization only)
    public Outlet createNewOutlet(Outlet newOutlet);

    // Retrieve by ID
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException;
    
    // Rerieve by Name
    public Outlet retrieveOutletByName(String name) throws OutletNotFoundException;
    
}
