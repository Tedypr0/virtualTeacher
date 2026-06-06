package com.example.virtual_teacher.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PublicIdMigrationRunner implements ApplicationRunner {

    private final SessionFactory sessionFactory;

    public PublicIdMigrationRunner(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void run(ApplicationArguments args) {
        String[] tables = {
                "users", "courses", "lectures", "notes", "homeworks",
                "courses_comments", "lectures_comments", "ratings"
        };
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            for (String table : tables) {
                session.createNativeQuery(
                                "UPDATE " + table + " SET public_id = UUID() " +
                                        "WHERE public_id IS NULL OR public_id = ''")
                        .executeUpdate();
            }
            tx.commit();
        } catch (Exception ignored) {
            // Columns may not exist yet on first boot before ddl-auto runs
        }
    }
}
