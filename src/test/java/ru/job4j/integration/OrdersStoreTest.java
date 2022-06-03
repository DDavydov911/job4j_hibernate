package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OrdersStoreTest {
    private final BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_003.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));

        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndFindById() {
        OrdersStore store = new OrdersStore(pool);

        Order first = Order.of("name1", "description1");
        store.save(first);

        Order result = store.findById(1);

        assertThat(result.getId(), is(1));
        assertThat(result.getDescription(), is("description1"));
        assertThat(result, is(first));
    }

    @Test
    public void whenSaveOrderAndFindByName() {
        OrdersStore store = new OrdersStore(pool);

        Order first = Order.of("name1", "description1");
        store.save(first);

        List<Order> all = store.findByName("name1");

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getName(), is("name1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndUpdate() {
        OrdersStore store = new OrdersStore(pool);

        Order first = Order.of("name1", "description1");
        store.save(first);

        Order second = store.findById(1);
        second.setName("name2");
        assertThat(second.getId(), is(1));
        assertThat(second.getDescription(), is("description1"));
        assertThat(second, is(first));
    }

    @After
    public void close() throws SQLException {
        pool.getConnection().prepareStatement("DROP TABLE orders").executeUpdate();
        pool.close();
    }
}