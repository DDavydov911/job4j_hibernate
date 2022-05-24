package ru.job4j.many;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.many.model.Author;
import ru.job4j.many.model.Book;
import ru.job4j.many.model.CarMark;
import ru.job4j.many.model.CarModel;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {

    public static void main(String[] args) {
        List<CarMark> carMarks = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            CarMark hyundai = new CarMark("Hyundai");

            CarModel solaris = new CarModel("Solaris");
            solaris.setCarMark(hyundai);
            CarModel creta  = new CarModel("Creta");
            creta.setCarMark(hyundai);
            CarModel sonata = new CarModel("Sonata");
            sonata.setCarMark(hyundai);
            CarModel i40 = new CarModel("i40");
            i40.setCarMark(hyundai);

            hyundai.getModels().add(solaris);
            hyundai.getModels().add(creta);
            hyundai.getModels().add(sonata);
            hyundai.getModels().add(i40);
            session.persist(hyundai);

            carMarks = session.createQuery(
                    "select distinct cm from CarMark cm join fetch cm.models"
            ).list();
            session.getTransaction().commit();
            session.close();

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        for (CarModel model : carMarks.get(0).getModels()) {
            System.out.println(model);
        }

    }
}