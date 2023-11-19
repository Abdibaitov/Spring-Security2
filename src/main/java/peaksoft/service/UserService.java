package peaksoft.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peaksoft.model.Role;
import peaksoft.model.User;

import java.util.Collections;
import java.util.List;


@Service
@Transactional
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private RoleService roleService;
    public void save(User user){
        Role adminRole = roleService.findByName("ADMIN");
        Role userRole = roleService.findByName("USER");
        if(findAll().isEmpty()){
            user.setRoles(Collections.singletonList(adminRole));
            adminRole.setUsers(Collections.singletonList(user));
        }else {
            userRole.setUsers(Collections.singletonList(user));

            user.setRoles(Collections.singletonList(userRole));
        }

        entityManager.persist(user);
    }


    public User findByEmail(String email){
        return entityManager.createQuery("select u from User u where u.email=:email", User.class)
                .setParameter("email",email).getSingleResult();
    }

    public List<User> findAll(){
        return entityManager.createQuery("from User ", User.class).getResultList();
    }
}
