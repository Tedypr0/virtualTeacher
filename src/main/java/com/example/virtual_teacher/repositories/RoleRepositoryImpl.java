package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import com.example.virtual_teacher.models.User;
import com.example.virtual_teacher.models.UserRole;
import com.example.virtual_teacher.repositories.contracts.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<UserRole> getAll() {
        try(Session session = sessionFactory.openSession()){
            Query<UserRole> query = session.createQuery("from UserRole order by id", UserRole.class);
            return query.getResultList();
        }
    }

    @Override
    public UserRole getById(int id) {
        try(Session session = sessionFactory.openSession()){
            Query<UserRole> query = session.createQuery("from UserRole where id = :id", UserRole.class);
            query.setParameter("id",id);
            List<UserRole> roles = query.getResultList();
            if(roles.size() == 0){
                throw new EntityNotFoundException("UserRole",id);
            }
            return roles.get(0);
        }
    }

    @Override
    public UserRole getByRoleName(String name) {
        try(Session session = sessionFactory.openSession()){
            Query<UserRole> query = session.createQuery("from UserRole where role = :name", UserRole.class);
            query.setParameter("name",name);
            List<UserRole> roles = query.getResultList();
            if(roles.size() == 0){
                throw new EntityNotFoundException("UserRole",name);
            }
            return roles.get(0);
        }
    }

    @Override
    public UserRole create(UserRole userRole){
        try(Session session = sessionFactory.openSession()){
            session.save(userRole);
        }
        return userRole;
    }

    @Override
    public UserRole update(UserRole userRole) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(userRole);
            session.getTransaction().commit();
        }
        return userRole;
    }

    @Override
    public UserRole delete(int id) {
        try(Session session = sessionFactory.openSession()){
            UserRole roleToDelete = session.get(UserRole.class,id);
            if(roleToDelete==null){
                throw new EntityNotFoundException("UserRole",id);
            }
            session.beginTransaction();
            session.delete(roleToDelete);
            session.getTransaction().commit();
            return roleToDelete;
        }
    }
}
