package ru.job4j.many;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.many.model.CarMark;
import ru.job4j.many.model.CarModel;

public class HbmRun {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            CarModel i20 = new CarModel("i20");
            session.save(i20);
            CarModel solaris = new CarModel("Solaris");
            session.save(solaris);
            CarModel creta = new CarModel("Creta");
            session.save(creta);
            CarModel sonata = new CarModel("Sonata");
            session.save(sonata);
            CarModel santafe = new CarModel("SantaFe");
            session.save(santafe);

            CarMark hyundai = new CarMark("Hyundai");
            hyundai.addCarModel(session.load(CarModel.class, 1));
            hyundai.addCarModel(session.load(CarModel.class, 2));
            hyundai.addCarModel(session.load(CarModel.class, 3));
            hyundai.addCarModel(session.load(CarModel.class, 4));
            hyundai.addCarModel(session.load(CarModel.class, 5));

            session.save(hyundai);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}