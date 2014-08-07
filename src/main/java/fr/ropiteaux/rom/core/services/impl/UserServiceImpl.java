package fr.ropiteaux.rom.core.services.impl;


import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.inject.persist.Transactional;

import fr.ropiteaux.rom.core.model.User;
import fr.ropiteaux.rom.core.services.AbstractCRUDService;
import fr.ropiteaux.rom.core.services.UserService;

@Transactional
public class UserServiceImpl extends AbstractCRUDService<User> implements UserService{

    @Override
    protected Class<? extends User> getEntityClass() {
        return User.class;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public User findByEmail(String email) {
        CriteriaBuilder b = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq =  b.createQuery();
        Root root = cq.from(getEntityClass());
        cq.where(b.equal(root.get("email"), email));
        return find(getEntityManager().createQuery(cq));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public User findByRequestToken(String userid, String authToken) {
        CriteriaBuilder b = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq =  b.createQuery();
        Root root = cq.from(getEntityClass());
        cq.where(b.equal(root.get("id"), userid),
                b.equal(root.get("authToken"), authToken));
        return find(getEntityManager().createQuery(cq));
    }

    @Override
    public User create(User user) {
        //Create the user
        User u = super.create(user);

        //Update user information
        u.setCreateDate(new Date());
        return super.update(u);
    }

}
