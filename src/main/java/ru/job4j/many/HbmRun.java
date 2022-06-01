package ru.job4j.many;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.many.model.*;

public class HbmRun {

    public static void main(String[] args) {
        Candidate result = new Candidate();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            result = session.createQuery(
                            "select distinct c from Candidate c "
                                    + "join fetch c.vacancyBase vb "
                                    + "join fetch vb.vacancies v "
                                    + "where c.id = :cId", Candidate.class
                    )
                    .setParameter("cId", 1)
                    .uniqueResult();

            session.getTransaction().commit();
            session.close();

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        System.out.println("Result: " + result);
    }
}