/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.OutletNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
@LocalBean
public class OutletSessionBean {

    @PersistenceContext
    private EntityManager em;

    // Create
    public Outlet createNewOutlet(Outlet outlet) 
    {
        em.persist(outlet);
        em.flush();
        
        return outlet;
    }
    
    
    // Retrieve
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException 
    {
        Outlet outlet = em.find(Outlet.class, outletId);
        
        if(outlet != null) 
        {
            return outlet;
        }
        else 
        {
            throw new OutletNotFoundException("Outlet ID " + outletId + " does not exist!");
        }
    }
    
    
    public Outlet retrieveOutletByName(String name) throws OutletNotFoundException
    {
        Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.name = :inName");
        query.setParameter("inName", name);
        
        try
        {
            return (Outlet)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new OutletNotFoundException("Outlet " + name + " does not exist!");
        }
    }
}
