/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CategoryNotFoundException;

/**
 *
 * @author khoojingzhi
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Category createNewCategory(Category category) 
    {
        em.persist(category);
        em.flush();
        
        return category;
    }
    
    @Override
    public Category retrieveCategoryByType(String type) throws CategoryNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM Category c WHERE c.type = :inType");
        query.setParameter("inType", type);
        
        try
        {
            return (Category)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CategoryNotFoundException("Category " + type + " does not exist!");
        }
    }
}
