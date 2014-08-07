package fr.ropiteaux.rom.core.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

import org.eclipse.persistence.config.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.List;

public abstract class AbstractCRUDService<T> implements CRUDService<T>{


    @Inject
    private Provider<EntityManager> emProvider;

    protected EntityManager getEntityManager(){
        return emProvider.get();
    }
    protected abstract Class<? extends T> getEntityClass();

    @SuppressWarnings("rawtypes")
	public T create(T t){
        try {
            t = create(getEntityManager(), t);
        }
        catch (ConstraintViolationException e) {
            e.printStackTrace(System.err);
            for(ConstraintViolation viol : e.getConstraintViolations()){
                System.err.println(viol.toString());
            }
        }
        catch (PersistenceException e) {
            e.printStackTrace(System.err);
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return t;
    }

    @Transactional
    protected T create(EntityManager em, T t){
        em.persist(t);
        return t;
    }

    public T update (T t){
        return update(getEntityManager(), t);
    }

    @Transactional
    protected T update(EntityManager em, T t){

        t = em.merge(t);
        //em.flush();
        return t;
    }

    public void delete(T t) {
        delete(getEntityManager(), t);
    }


    public void delete(List<T> list) {
        for( T t : list ){
            delete(t);
        }
    }
    @Transactional
    protected void delete(EntityManager entitymanager, T t) {
        entitymanager.remove(t);
        //entitymanager.flush();
    }
    public T find(TypedQuery<T> query){
        T result = null;
        try{
            result = query.getSingleResult();
        }
        catch (NoResultException e) {
            //do nothing, no result is considered as a good result
            result = null;
        }
        return result;
    }

    public T findById(long id) {
        return getEntityManager().find(getEntityClass(), id);
    }

    @SuppressWarnings("unchecked")
	public List<T> findAll() {
        CriteriaQuery<T> q = (CriteriaQuery<T>) getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
        TypedQuery<T> query = getEntityManager().createQuery(q);
        query.setHint(QueryHints.RESULT_COLLECTION_TYPE, java.util.ArrayList.class);
        return query.getResultList();
    }
}