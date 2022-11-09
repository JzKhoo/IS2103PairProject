/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;

/**
 *
 * @author khoojingzhi
 */
@Local
public interface PartnerSessionBeanLocal {

    // Create (Backend data initialization only)
    public Partner createNewPartner(Partner newPartner);
    
}
