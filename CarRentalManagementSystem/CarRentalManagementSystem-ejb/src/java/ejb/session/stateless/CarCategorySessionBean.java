/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
    
    // Create
    @Override
    public CarCategory createNewCategory(CarCategory carCategory) 
    {
        em.persist(carCategory);
        em.flush();
        
        return carCategory;
    }
    
    // Retrieve
    @Override
    public CarCategory retrieveCategoryByName(String name) throws CarCategoryNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM CarCategory c WHERE c.name = :inName");
        query.setParameter("inName", name);
        
        try
        {
            return (CarCategory)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CarCategoryNotFoundException("Category " + name + " does not exist!");
        }
    }
}
