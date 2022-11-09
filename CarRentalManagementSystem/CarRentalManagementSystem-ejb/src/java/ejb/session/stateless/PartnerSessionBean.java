/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    // Create (Backend data initialization only)
    @Override
    public Partner createNewPartner(Partner newPartner) 
    {
        em.persist(newPartner);
        em.flush();
        
        return newPartner;
    }
}
