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

public class HbmRun {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Book book1 = new Book("BookName1");
            Book book2 = new Book("BookName2");
            Book book3 = new Book("BookName3");
            Author author1 = new Author("Author1");
            Author author2 = new Author("Author2");
            Author author3 = new Author("Author3");

            book1.getAuthors().add(author1);
            book2.getAuthors().add(author2);
            book3.getAuthors().add(author2);
            book3.getAuthors().add(author3);

            session.persist(book1);
            session.persist(book2);
            session.persist(book3);

            session.getTransaction().commit();
            session.close();

            session.beginTransaction();
            Author authorForDel = session.get(Author.class, 3);
            session.remove(authorForDel);

            session.getTransaction().commit();
            session.close();

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}