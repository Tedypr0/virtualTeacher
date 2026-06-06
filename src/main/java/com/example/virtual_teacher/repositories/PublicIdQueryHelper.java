package com.example.virtual_teacher.repositories;

import com.example.virtual_teacher.exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.function.Predicate;

public final class PublicIdQueryHelper {

    private PublicIdQueryHelper() {
    }

    public static <T> T findByPublicId(Session session,
                                       Class<T> entityClass,
                                       String publicId,
                                       String entityName,
                                       Predicate<T> validator) {
        Query<T> query = session.createQuery(
                "from " + entityClass.getSimpleName() + " e where e.publicId = :publicId",
                entityClass);
        query.setParameter("publicId", publicId);
        List<T> results = query.getResultList();
        if (results.isEmpty()) {
            throw new EntityNotFoundException(entityName, "publicId", publicId);
        }
        T entity = results.get(0);
        if (validator != null && !validator.test(entity)) {
            throw new EntityNotFoundException(entityName, "publicId", publicId);
        }
        return entity;
    }
}
