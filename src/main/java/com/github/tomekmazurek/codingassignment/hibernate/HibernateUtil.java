package com.github.tomekmazurek.codingassignment.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
logger.debug(">> Inside getSessionFactory");
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                logger.error(">> Error" + e.getMessage());
                if (registry != null) {
                    logger.debug(">> Destroying registry");
                    shutdown();
                }
            }
        }
        logger.debug(">> SessionFactory built");
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
