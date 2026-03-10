package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class ClientDemo {
    public static void main(String[] args) {
        // bootstrapping Hibernate
        Configuration cfg = new Configuration().configure(); // reads hibernate.cfg.xml
        SessionFactory factory = cfg.buildSessionFactory();

        // open session
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();

            // I. Insert a record using a persistent object
            Invoice invoice = new Invoice("Acme Supplies", new Date(), "NEW");
            session.save(invoice);
            // at this point invoice becomes persistent (id is generated on flush)

            tx.commit();

            // II. View all records using HQL (no WHERE clause)
            Query<Invoice> allQuery = session.createQuery("from Invoice", Invoice.class);
            // no positional parameters are required when we are selecting all records
            List<Invoice> list = allQuery.list();

            System.out.println("All invoices:");
            for (Invoice inv : list) {
                System.out.println(inv);
            }

            // Demonstration of positional parameter (not used to restrict results here)
            // You could add a query with a positional parameter if needed:
            Query<Invoice> paramQuery = session.createQuery("from Invoice i where i.status = ?0", Invoice.class);
            paramQuery.setParameter(0, "NEW");
            List<Invoice> filtered = paramQuery.list();
            System.out.println("Invoices with status NEW (example of positional parameter):");
            filtered.forEach(System.out::println);
        }

        factory.close();
    }
}